package com.example.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.InlineDataPart
import com.google.firebase.ai.type.TextPart
import com.google.firebase.ai.type.Tool
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

private const val TAG_SEARCH = "GeminiSearchService"
private const val TAG_AUDIO = "GeminiTranscribeService"

class GeminiSearchService {

    // Mandated: Use gemini-3.8-flash with Tool.googleSearch() for Google Search Grounding
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            tools = listOf(Tool.googleSearch())
        )
    }

    /**
     * Queries Google Search through Gemini 3.8 Flash to get up-to-date, grounded WASSCE syllabus,
     * exam timetables, chief examiner reports, and real-time academic facts.
     */
    fun streamSearchResearch(query: String, subjectName: String): Flow<String> = flow {
        val prompt = """
            You are a Ghanaian WASSCE Education & WAEC Curriculum Specialist.
            Use Google Search grounding to find the most accurate, current, and verified information for:
            
            Subject: $subjectName
            Query: $query
            
            Provide a clear, detailed, and structured response including:
            1. Verified facts and up-to-date syllabus guidelines.
            2. Key formulas, rules, or examiners' expectations.
            3. Actionable study tips for Ghanaian SHS candidates.
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
            Log.e(TAG_SEARCH, "Search grounding timed out", e)
            emit("\n[Search request timed out. Please try again.]")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG_SEARCH, "Search grounding failed", e)
            emit("\n[Search Grounding Notice: ${e.localizedMessage ?: "Unable to complete search. Check connection."}]")
        }
    }
}

class GeminiAudioTranscribeService {

    // Mandated: Use gemini-3.5-transcribe for audio transcription
    private val transcribeModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.5-transcribe"
        )
    }

    /**
     * Transcribes recorded microphone audio bytes into verbatim English text using gemini-3.5-transcribe.
     */
    suspend fun transcribeAudio(audioBytes: ByteArray, mimeType: String = "audio/mp4"): String {
        return try {
            val audioContent = Content(
                parts = listOf(
                    InlineDataPart(audioBytes, mimeType),
                    TextPart("Transcribe this classroom audio recording verbatim into clear text. Do not summarize or add markdown commentary, provide only the transcribed words spoken.")
                )
            )

            withTimeout(60_000L) {
                val response = transcribeModel.generateContent(audioContent)
                response.text ?: "Transcription completed with no text."
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG_AUDIO, "Audio transcription timed out", e)
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG_AUDIO, "Audio transcription failed", e)
            throw e
        }
    }
}
