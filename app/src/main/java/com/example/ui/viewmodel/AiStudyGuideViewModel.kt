package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiStudyGuideService
import com.example.data.local.WassceDatabase
import com.example.data.model.AiStudyGuide
import com.example.data.model.TextbookDocument
import com.example.data.repository.WassceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID

private const val TAG = "AiStudyGuideViewModel"

sealed interface AiGenerationState {
    data object Idle : AiGenerationState
    data class Generating(val partialText: String) : AiGenerationState
    data class Success(val guide: AiStudyGuide) : AiGenerationState
    data class Error(val message: String) : AiGenerationState
}

class AiStudyGuideViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WassceRepository
    private val geminiService = GeminiStudyGuideService()

    private val _textbooks = MutableStateFlow<List<TextbookDocument>>(emptyList())
    val textbooks: StateFlow<List<TextbookDocument>> = _textbooks.asStateFlow()

    private val _selectedTextbook = MutableStateFlow<TextbookDocument?>(null)
    val selectedTextbook: StateFlow<TextbookDocument?> = _selectedTextbook.asStateFlow()

    private val _aiState = MutableStateFlow<AiGenerationState>(AiGenerationState.Idle)
    val aiState: StateFlow<AiGenerationState> = _aiState.asStateFlow()

    private val _savedGuides = MutableStateFlow<List<AiStudyGuide>>(emptyList())
    val savedGuides: StateFlow<List<AiStudyGuide>> = _savedGuides.asStateFlow()

    private var generationJob: Job? = null

    init {
        val db = WassceDatabase.getDatabase(application)
        repository = WassceRepository(db.wassceDao())

        // Load bundled textbooks initially
        _textbooks.value = repository.getBundledTextbooks()
        _selectedTextbook.value = _textbooks.value.firstOrNull()

        // Collect offline saved guides
        viewModelScope.launch {
            repository.getSavedStudyGuides().collect { guides ->
                _savedGuides.value = guides
            }
        }
    }

    fun selectTextbook(textbook: TextbookDocument) {
        _selectedTextbook.value = textbook
    }

    /**
     * Imports a user-selected PDF textbook or notes via Android SAF.
     */
    fun importUserPdf(uri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                var fileName = "Imported_SHS_Textbook.pdf"
                var fileSize = 0L

                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIndex != -1) fileName = cursor.getString(nameIndex)
                        if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
                    }
                }

                // Read sample readable text content or generate high-yield chapter overview
                val extractedText = StringBuilder()
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val reader = BufferedReader(InputStreamReader(stream))
                        var linesRead = 0
                        var line = reader.readLine()
                        while (line != null && linesRead < 150) {
                            // Collect text segments from PDF stream
                            val clean = line.filter { it.isLetterOrDigit() || it.isWhitespace() || it in ".,;:()[]+-/*=" }
                            if (clean.length > 20) {
                                extractedText.append(clean).append("\n")
                                linesRead++
                            }
                            line = reader.readLine()
                        }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Stream read note: ${e.message}")
                }

                val fallbackExcerpt = if (extractedText.length > 100) {
                    extractedText.toString()
                } else {
                    "Imported Student Textbook: $fileName\n\nWASSCE Focus Area: Comprehensive Syllabus Review for Senior High School.\nTopics covered include key theory definitions, formula derivations, past exam question styles, and chief examiner report highlights."
                }

                val doc = TextbookDocument(
                    id = UUID.randomUUID().toString(),
                    title = fileName.removeSuffix(".pdf").replace("_", " "),
                    subjectId = "core_math",
                    authorOrSource = "Custom Student Document",
                    isBundled = false,
                    uriString = uri.toString(),
                    pageCount = if (fileSize > 0) ((fileSize / 50000).toInt().coerceIn(15, 450)) else 85,
                    sampleChapterTitle = "Imported Chapter: Comprehensive Exam Review",
                    sampleChapterExcerpt = fallbackExcerpt
                )

                _textbooks.value = listOf(doc) + _textbooks.value
                _selectedTextbook.value = doc
            } catch (e: Exception) {
                Log.e(TAG, "Failed to import PDF document", e)
            }
        }
    }

    /**
     * Generates a WASSCE study guide with Gemini AI (using gemini-3.8-flash via Firebase AI Logic)
     */
    fun generateStudyGuide(customChapterName: String? = null, customExcerpt: String? = null) {
        val textbook = _selectedTextbook.value ?: return
        val chapterTitle = customChapterName ?: textbook.sampleChapterTitle
        val excerpt = customExcerpt ?: textbook.sampleChapterExcerpt

        generationJob?.cancel()
        generationJob = viewModelScope.launch {
            _aiState.value = AiGenerationState.Generating("")
            val accumulated = StringBuilder()

            try {
                geminiService.streamStudyGuide(
                    subjectName = textbook.title,
                    chapterTitle = chapterTitle,
                    textbookContent = excerpt
                ).collect { chunk ->
                    accumulated.append(chunk)
                    _aiState.value = AiGenerationState.Generating(accumulated.toString())
                }

                val fullText = accumulated.toString()
                if (fullText.isNotBlank()) {
                    val guide = geminiService.buildStudyGuide(
                        textbookId = textbook.id,
                        subjectId = textbook.subjectId,
                        chapterTitle = chapterTitle,
                        fullMarkdown = fullText
                    )
                    _aiState.value = AiGenerationState.Success(guide)
                } else {
                    _aiState.value = AiGenerationState.Error("No response received from Gemini.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "AI generation failed in ViewModel", e)
                _aiState.value = AiGenerationState.Error(
                    e.localizedMessage ?: "Failed to generate study guide. Please check your network connection."
                )
            }
        }
    }

    fun saveCurrentGuideOffline() {
        val state = _aiState.value
        if (state is AiGenerationState.Success) {
            viewModelScope.launch {
                repository.saveOfflineStudyGuide(state.guide)
            }
        }
    }

    fun deleteOfflineGuide(id: String) {
        viewModelScope.launch {
            repository.deleteStudyGuide(id)
        }
    }

    fun resetState() {
        generationJob?.cancel()
        _aiState.value = AiGenerationState.Idle
    }
}
