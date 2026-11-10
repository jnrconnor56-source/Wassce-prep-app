package com.example.ai

import android.util.Log
import com.example.data.model.AiStudyGuide
import com.example.data.model.StudyGuideQuizQuestion
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.util.UUID

private const val TAG = "GeminiStudyGuideService"

class GeminiStudyGuideService {

    private val model by lazy {
        Firebase.ai.generativeModel("gemini-3.8-flash")
    }

    /**
     * Streams the generated study guide content from Gemini in real time to minimize perceived latency.
     */
    fun streamStudyGuide(
        subjectName: String,
        chapterTitle: String,
        textbookContent: String
    ): Flow<String> = flow {
        val prompt = """
            You are a Senior WAEC / WASSCE Chief Examiner for Ghanaian Senior High Schools (SHS).
            Analyze the following textbook excerpt for the subject '$subjectName' and chapter '$chapterTitle'.
            
            Produce a comprehensive, structured WASSCE Revision & Study Guide following this exact format:
            
            # $chapterTitle - WASSCE Study Guide ($subjectName)
            
            ## 1. Executive Summary & Core Concept
            [Provide a clear, high-yield conceptual explanation in 2-3 engaging paragraphs suitable for an SHS candidate.]
            
            ## 2. Key Definitions, Formulas & Equations
            [Bullet points of essential formulas, definitions, and theorems that candidates must memorize for Paper 1 & 2.]
            
            ## 3. WAEC Chief Examiner Warnings & Common Mistakes
            [Bullet points detailing exact mistakes Ghanaian candidates routinely make on this topic and how to avoid losing marks.]
            
            ## 4. WASSCE Practice Questions with Solutions
            [Provide 3-5 authentic multiple-choice questions with (A, B, C, D) options, state the correct answer, and provide step-by-step working.]
            
            TEXTBOOK EXCERPT:
            $textbookContent
        """.trimIndent()

        try {
            withTimeout(60_000L) {
                model.generateContentStream(prompt).collect { chunk ->
                    val text = chunk.text.orEmpty()
                    if (text.isNotEmpty()) {
                        emit(text)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Study guide generation timed out after 60s", e)
            emit("\n\n[Note: Generation timed out. You can save the partially generated guide or retry.]")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini study guide generation failed", e)
            emit("\n\n[AI Generation Error: ${e.localizedMessage ?: "Network or attestation issue. Check internet connection or try again."}]")
        }
    }

    /**
     * Creates a parsed AiStudyGuide object from accumulated markdown for offline database storage.
     */
    fun buildStudyGuide(
        textbookId: String,
        subjectId: String,
        chapterTitle: String,
        fullMarkdown: String
    ): AiStudyGuide {
        val formulas = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val questions = mutableListOf<StudyGuideQuizQuestion>()

        // Extract key bullet points for quick-glance chips
        val lines = fullMarkdown.lines()
        var currentSection = ""
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("## 2") || trimmed.contains("Formulas", ignoreCase = true)) {
                currentSection = "formulas"
            } else if (trimmed.startsWith("## 3") || trimmed.contains("Examiner", ignoreCase = true)) {
                currentSection = "warnings"
            } else if (trimmed.startsWith("## 4") || trimmed.contains("Practice", ignoreCase = true)) {
                currentSection = "questions"
            } else if (trimmed.startsWith("##")) {
                currentSection = "other"
            } else if (trimmed.startsWith("-") || trimmed.startsWith("*")) {
                val item = trimmed.removePrefix("-").removePrefix("*").trim()
                if (item.length > 5) {
                    if (currentSection == "formulas" && formulas.size < 8) formulas.add(item)
                    if (currentSection == "warnings" && warnings.size < 6) warnings.add(item)
                }
            }
        }

        if (formulas.isEmpty()) {
            formulas.add("Review core formulas and definitions from textbook chapter")
        }
        if (warnings.isEmpty()) {
            warnings.add("Always state geometric or algebraic reasons in brackets during Paper 2")
            warnings.add("Show all intermediate calculation steps for method marks")
        }

        return AiStudyGuide(
            id = UUID.randomUUID().toString(),
            textbookId = textbookId,
            subjectId = subjectId,
            chapterTitle = chapterTitle,
            summaryNotes = fullMarkdown,
            keyFormulasAndDefinitions = formulas,
            chiefExaminerWarnings = warnings,
            generatedQuestions = questions,
            createdAtMillis = System.currentTimeMillis()
        )
    }
}
