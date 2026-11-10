package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import java.util.UUID

class WassceRepository(private val dao: WassceDao) {

    fun getAllSubjects(): List<Subject> = WassceDataBank.subjects

    fun getSubjectsForTrack(track: ShsTrack): List<Subject> {
        return if (track == ShsTrack.ALL_ROUND) {
            WassceDataBank.subjects
        } else {
            WassceDataBank.subjects.filter {
                it.category == SubjectCategory.CORE || it.tracks.contains(track)
            }
        }
    }

    fun getSubjectById(subjectId: String): Subject? {
        return WassceDataBank.subjects.find { it.id == subjectId }
    }

    fun getTopicsForSubject(subjectId: String): List<Topic> {
        return WassceDataBank.topics.filter { it.subjectId == subjectId }
    }

    fun getTopicProgress(): Flow<List<TopicProgressEntity>> = dao.getAllProgress()

    suspend fun updateTopicProgress(topicId: String, subjectId: String, status: TopicStatus, rating: Int) {
        dao.saveTopicProgress(
            TopicProgressEntity(
                topicId = topicId,
                subjectId = subjectId,
                status = status.name,
                confidenceRating = rating,
                lastUpdatedMillis = System.currentTimeMillis()
            )
        )
    }

    fun getPastQuestions(subjectId: String? = null, year: Int? = null): List<PastQuestion> {
        return WassceDataBank.pastQuestions.filter {
            (subjectId == null || it.subjectId == subjectId) &&
            (year == null || it.year == year)
        }
    }

    fun getPastQuestionsForTopic(subjectId: String, topicTitle: String): List<PastQuestion> {
        return WassceDataBank.pastQuestions.filter {
            it.subjectId == subjectId && it.topicTitle.contains(topicTitle, ignoreCase = true)
        }
    }

    fun getExamAttempts(): Flow<List<ExamAttempt>> {
        return dao.getAllAttempts().map { list ->
            list.map { entity ->
                ExamAttempt(
                    id = entity.id,
                    subjectId = entity.subjectId,
                    subjectName = entity.subjectName,
                    year = entity.year,
                    score = entity.score,
                    totalQuestions = entity.totalQuestions,
                    percentage = entity.percentage,
                    grade = entity.grade,
                    timeTakenSeconds = entity.timeTakenSeconds,
                    completedAtMillis = entity.completedAtMillis
                )
            }
        }
    }

    suspend fun saveExamAttempt(
        subjectId: String,
        subjectName: String,
        year: Int,
        score: Int,
        total: Int,
        timeTakenSeconds: Int
    ): ExamAttempt {
        val pct = if (total > 0) (score * 100) / total else 0
        val grade = when {
            pct >= 80 -> "A1 (Excellent)"
            pct >= 75 -> "B2 (Very Good)"
            pct >= 70 -> "B3 (Good)"
            pct >= 65 -> "C4 (Credit)"
            pct >= 60 -> "C5 (Credit)"
            pct >= 50 -> "C6 (Credit)"
            pct >= 45 -> "D7 (Pass)"
            pct >= 40 -> "E8 (Pass)"
            else -> "F9 (Fail)"
        }

        val attempt = ExamAttempt(
            id = UUID.randomUUID().toString(),
            subjectId = subjectId,
            subjectName = subjectName,
            year = year,
            score = score,
            totalQuestions = total,
            percentage = pct,
            grade = grade,
            timeTakenSeconds = timeTakenSeconds,
            completedAtMillis = System.currentTimeMillis()
        )

        dao.insertAttempt(
            ExamAttemptEntity(
                id = attempt.id,
                subjectId = attempt.subjectId,
                subjectName = attempt.subjectName,
                year = attempt.year,
                score = attempt.score,
                totalQuestions = attempt.totalQuestions,
                percentage = attempt.percentage,
                grade = attempt.grade,
                timeTakenSeconds = attempt.timeTakenSeconds,
                completedAtMillis = attempt.completedAtMillis
            )
        )
        return attempt
    }

    fun getBundledTextbooks(): List<TextbookDocument> = WassceDataBank.bundledTextbooks

    fun getStudyBuddies(): List<StudyBuddy> = WassceDataBank.studyBuddies

    fun getStudyRooms(): List<StudyRoom> = WassceDataBank.studyRooms

    fun getSavedStudyGuides(): Flow<List<AiStudyGuide>> {
        return dao.getAllStudyGuides().map { list ->
            list.map { entity ->
                val formulas = parseJsonArray(entity.formulasJson)
                val warnings = parseJsonArray(entity.examinerWarningsJson)
                AiStudyGuide(
                    id = entity.id,
                    textbookId = entity.textbookId,
                    subjectId = entity.subjectId,
                    chapterTitle = entity.chapterTitle,
                    summaryNotes = entity.summaryNotes,
                    keyFormulasAndDefinitions = formulas,
                    chiefExaminerWarnings = warnings,
                    generatedQuestions = emptyList(),
                    createdAtMillis = entity.createdAtMillis
                )
            }
        }
    }

    suspend fun saveOfflineStudyGuide(guide: AiStudyGuide) {
        val formulasJson = JSONArray(guide.keyFormulasAndDefinitions).toString()
        val warningsJson = JSONArray(guide.chiefExaminerWarnings).toString()
        dao.insertStudyGuide(
            OfflineStudyGuideEntity(
                id = guide.id,
                textbookId = guide.textbookId,
                subjectId = guide.subjectId,
                chapterTitle = guide.chapterTitle,
                summaryNotes = guide.summaryNotes,
                formulasJson = formulasJson,
                examinerWarningsJson = warningsJson,
                quizQuestionsJson = "[]",
                createdAtMillis = guide.createdAtMillis
            )
        )
    }

    suspend fun deleteStudyGuide(id: String) {
        dao.deleteStudyGuide(id)
    }

    private fun parseJsonArray(json: String): List<String> {
        return try {
            val arr = JSONArray(json)
            val list = mutableListOf<String>()
            for (i in 0 until arr.length()) {
                list.add(arr.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
