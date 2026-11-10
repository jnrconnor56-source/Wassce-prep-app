package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TopicProgressEntity
import com.example.data.local.WassceDatabase
import com.example.data.model.*
import com.example.data.repository.WassceDataBank
import com.example.data.repository.WassceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExamSessionState(
    val isActive: Boolean = false,
    val subjectId: String = "",
    val subjectName: String = "",
    val isTimedMode: Boolean = true,
    val totalTimeSeconds: Int = 1800, // 30 minutes
    val remainingSeconds: Int = 1800,
    val questions: List<PastQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> selectedOptionIndex
    val isSubmitted: Boolean = false,
    val finalScore: Int = 0,
    val finalGrade: String = "",
    val bookmarkedQuestions: Set<Int> = emptySet()
)

class WassceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WassceRepository

    init {
        val db = WassceDatabase.getDatabase(application)
        repository = WassceRepository(db.wassceDao())
    }

    private val _selectedTrack = MutableStateFlow(ShsTrack.ALL_ROUND)
    val selectedTrack: StateFlow<ShsTrack> = _selectedTrack.asStateFlow()

    private val _selectedSubjectId = MutableStateFlow("core_math")
    val selectedSubjectId: StateFlow<String> = _selectedSubjectId.asStateFlow()

    private val _topicProgressMap = MutableStateFlow<Map<String, TopicProgressEntity>>(emptyMap())
    val topicProgressMap: StateFlow<Map<String, TopicProgressEntity>> = _topicProgressMap.asStateFlow()

    private val _examAttempts = MutableStateFlow<List<ExamAttempt>>(emptyList())
    val examAttempts: StateFlow<List<ExamAttempt>> = _examAttempts.asStateFlow()

    private val _examSession = MutableStateFlow(ExamSessionState())
    val examSession: StateFlow<ExamSessionState> = _examSession.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getTopicProgress().collect { list ->
                _topicProgressMap.value = list.associateBy { it.topicId }
            }
        }
        viewModelScope.launch {
            repository.getExamAttempts().collect { list ->
                _examAttempts.value = list
            }
        }
    }

    fun setTrack(track: ShsTrack) {
        _selectedTrack.value = track
        val available = repository.getSubjectsForTrack(track)
        if (available.none { it.id == _selectedSubjectId.value }) {
            _selectedSubjectId.value = available.firstOrNull()?.id ?: "core_math"
        }
    }

    fun selectSubject(subjectId: String) {
        _selectedSubjectId.value = subjectId
    }

    fun getSubjects(): List<Subject> = repository.getSubjectsForTrack(_selectedTrack.value)

    fun getTopicsForCurrentSubject(): List<Topic> {
        val topics = repository.getTopicsForSubject(_selectedSubjectId.value)
        val progress = _topicProgressMap.value
        return topics.map { topic ->
            val entity = progress[topic.id]
            if (entity != null) {
                topic.copy(
                    status = try { TopicStatus.valueOf(entity.status) } catch (e: Exception) { TopicStatus.NOT_STARTED },
                    confidenceRating = entity.confidenceRating
                )
            } else {
                topic
            }
        }
    }

    fun toggleTopicStatus(topic: Topic) {
        viewModelScope.launch {
            val nextStatus = when (topic.status) {
                TopicStatus.NOT_STARTED -> TopicStatus.IN_PROGRESS
                TopicStatus.IN_PROGRESS -> TopicStatus.MASTERED
                TopicStatus.MASTERED -> TopicStatus.NOT_STARTED
            }
            val newRating = if (nextStatus == TopicStatus.MASTERED) 5 else if (nextStatus == TopicStatus.IN_PROGRESS) 3 else 0
            repository.updateTopicProgress(topic.id, topic.subjectId, nextStatus, newRating)
        }
    }

    fun updateConfidence(topic: Topic, rating: Int) {
        viewModelScope.launch {
            val status = if (rating >= 4) TopicStatus.MASTERED else if (rating >= 1) TopicStatus.IN_PROGRESS else TopicStatus.NOT_STARTED
            repository.updateTopicProgress(topic.id, topic.subjectId, status, rating)
        }
    }

    fun calculateOverallReadiness(): Int {
        val allTopics = WassceDataBank.topics
        if (allTopics.isEmpty()) return 0
        val progress = _topicProgressMap.value
        var score = 0
        for (t in allTopics) {
            val entity = progress[t.id]
            if (entity != null) {
                when (entity.status) {
                    TopicStatus.MASTERED.name -> score += 100
                    TopicStatus.IN_PROGRESS.name -> score += 50
                    else -> score += 0
                }
            }
        }
        return score / allTopics.size
    }

    // Pasco Exam Session Methods
    fun startExamSession(subjectId: String, isTimed: Boolean, topicFilter: String? = null) {
        timerJob?.cancel()
        val subject = repository.getSubjectById(subjectId) ?: repository.getAllSubjects().first()
        val questions = if (topicFilter != null) {
            repository.getPastQuestionsForTopic(subjectId, topicFilter)
        } else {
            repository.getPastQuestions(subjectId)
        }.ifEmpty {
            repository.getPastQuestions()
        }

        _examSession.value = ExamSessionState(
            isActive = true,
            subjectId = subject.id,
            subjectName = subject.name,
            isTimedMode = isTimed,
            totalTimeSeconds = if (isTimed) 1500 else 0, // 25 mins
            remainingSeconds = if (isTimed) 1500 else 0,
            questions = questions,
            currentQuestionIndex = 0,
            userAnswers = emptyMap(),
            isSubmitted = false,
            finalScore = 0,
            finalGrade = "",
            bookmarkedQuestions = emptySet()
        )

        if (isTimed) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_examSession.value.remainingSeconds > 0 && !_examSession.value.isSubmitted) {
                delay(1000L)
                _examSession.value = _examSession.value.copy(
                    remainingSeconds = _examSession.value.remainingSeconds - 1
                )
            }
            if (_examSession.value.remainingSeconds <= 0 && !_examSession.value.isSubmitted) {
                submitExam()
            }
        }
    }

    fun selectOption(questionIndex: Int, optionIndex: Int) {
        if (_examSession.value.isSubmitted) return
        val currentAnswers = _examSession.value.userAnswers.toMutableMap()
        currentAnswers[questionIndex] = optionIndex
        _examSession.value = _examSession.value.copy(userAnswers = currentAnswers)
    }

    fun nextQuestion() {
        val curr = _examSession.value.currentQuestionIndex
        if (curr < _examSession.value.questions.size - 1) {
            _examSession.value = _examSession.value.copy(currentQuestionIndex = curr + 1)
        }
    }

    fun prevQuestion() {
        val curr = _examSession.value.currentQuestionIndex
        if (curr > 0) {
            _examSession.value = _examSession.value.copy(currentQuestionIndex = curr - 1)
        }
    }

    fun jumpToQuestion(index: Int) {
        if (index in _examSession.value.questions.indices) {
            _examSession.value = _examSession.value.copy(currentQuestionIndex = index)
        }
    }

    fun toggleBookmark(index: Int) {
        val marks = _examSession.value.bookmarkedQuestions.toMutableSet()
        if (marks.contains(index)) marks.remove(index) else marks.add(index)
        _examSession.value = _examSession.value.copy(bookmarkedQuestions = marks)
    }

    fun submitExam() {
        timerJob?.cancel()
        val session = _examSession.value
        if (session.isSubmitted) return

        var correct = 0
        session.questions.forEachIndexed { idx, q ->
            val userChoice = session.userAnswers[idx]
            if (userChoice != null && userChoice == q.correctOptionIndex) {
                correct++
            }
        }

        val timeTaken = session.totalTimeSeconds - session.remainingSeconds
        viewModelScope.launch {
            val attempt = repository.saveExamAttempt(
                subjectId = session.subjectId,
                subjectName = session.subjectName,
                year = 2024,
                score = correct,
                total = session.questions.size,
                timeTakenSeconds = if (session.isTimedMode) timeTaken else 60
            )
            _examSession.value = session.copy(
                isSubmitted = true,
                finalScore = correct,
                finalGrade = attempt.grade
            )
        }
    }

    fun exitExam() {
        timerJob?.cancel()
        _examSession.value = ExamSessionState()
    }
}
