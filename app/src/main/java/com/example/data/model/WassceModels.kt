package com.example.data.model

enum class ShsTrack(val displayName: String, val description: String) {
    GENERAL_SCIENCE("General Science", "Elective Maths, Physics, Chemistry, Biology"),
    GENERAL_ARTS("General Arts", "Government, Economics, Literature, Geography"),
    BUSINESS("Business", "Financial Accounting, Business Management, Costing, Economics"),
    ALL_ROUND("All Programmes", "Core subjects and electives across all SHS tracks")
}

enum class SubjectCategory {
    CORE,
    ELECTIVE
}

enum class TopicStatus {
    NOT_STARTED,
    IN_PROGRESS,
    MASTERED
}

enum class ExamWeight(val label: String, val tips: String) {
    HIGH("High Frequency", "Appears in every WASSCE Paper 1 & 2"),
    MEDIUM("Frequent", "Core WAEC testing area"),
    STANDARD("Standard", "Fundamental prerequisite concept")
}

data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val category: SubjectCategory,
    val tracks: List<ShsTrack>,
    val iconName: String,
    val colorHex: String,
    val description: String,
    val passMarkPercentage: Int = 50
)

data class Topic(
    val id: String,
    val subjectId: String,
    val unitNumber: Int,
    val title: String,
    val keyPoints: List<String>,
    val examWeight: ExamWeight,
    val status: TopicStatus = TopicStatus.NOT_STARTED,
    val confidenceRating: Int = 0 // 0 to 5
)

data class PastQuestion(
    val id: String,
    val subjectId: String,
    val year: Int,
    val questionNumber: Int,
    val topicTitle: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val examinerTip: String
)

data class ExamAttempt(
    val id: String,
    val subjectId: String,
    val subjectName: String,
    val year: Int,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val grade: String, // A1, B2, B3, C4, C5, C6, D7, E8, F9
    val timeTakenSeconds: Int,
    val completedAtMillis: Long
)

data class TextbookDocument(
    val id: String,
    val title: String,
    val subjectId: String,
    val authorOrSource: String,
    val isBundled: Boolean = false,
    val uriString: String? = null,
    val pageCount: Int,
    val sampleChapterTitle: String,
    val sampleChapterExcerpt: String
)

data class AiStudyGuide(
    val id: String,
    val textbookId: String,
    val subjectId: String,
    val chapterTitle: String,
    val summaryNotes: String,
    val keyFormulasAndDefinitions: List<String>,
    val chiefExaminerWarnings: List<String>,
    val generatedQuestions: List<StudyGuideQuizQuestion>,
    val createdAtMillis: Long
)

data class StudyGuideQuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val reason: String
)

data class StudyBuddy(
    val id: String,
    val name: String,
    val school: String,
    val avatarInitials: String,
    val avatarColorHex: String,
    val weeklyXp: Int,
    val streakDays: Int,
    val activeSubject: String,
    val statusMessage: String,
    val isOnline: Boolean
)

data class StudyRoom(
    val id: String,
    val roomCode: String,
    val title: String,
    val subjectName: String,
    val participantCount: Int,
    val pomodoroMinutesLeft: Int,
    val hostName: String,
    val school: String
)

data class QuizBattleQuestion(
    val question: PastQuestion,
    val userSelectedOption: Int? = null,
    val opponentSelectedOption: Int? = null
)
