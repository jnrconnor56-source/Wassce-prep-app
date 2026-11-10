package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.WassceDatabase
import com.example.data.model.*
import com.example.data.repository.WassceDataBank
import com.example.data.repository.WassceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ActiveBattleState(
    val isActive: Boolean = false,
    val opponent: StudyBuddy? = null,
    val questions: List<PastQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val questionSecondsRemaining: Int = 15,
    val userScore: Int = 0,
    val opponentScore: Int = 0,
    val userSelectedOption: Int? = null,
    val opponentSelectedOption: Int? = null,
    val isRoundComplete: Boolean = false,
    val isBattleFinished: Boolean = false,
    val winnerName: String = "",
    val xpEarned: Int = 0
)

data class ActiveRoomSession(
    val room: StudyRoom? = null,
    val isJoined: Boolean = false,
    val isTimerRunning: Boolean = true,
    val secondsLeft: Int = 25 * 60, // 25 min pomodoro
    val cheersReceived: List<String> = emptyList()
)

class StudySquadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WassceRepository

    init {
        val db = WassceDatabase.getDatabase(application)
        repository = WassceRepository(db.wassceDao())
    }

    private val _studyBuddies = MutableStateFlow<List<StudyBuddy>>(emptyList())
    val studyBuddies: StateFlow<List<StudyBuddy>> = _studyBuddies.asStateFlow()

    private val _studyRooms = MutableStateFlow<List<StudyRoom>>(emptyList())
    val studyRooms: StateFlow<List<StudyRoom>> = _studyRooms.asStateFlow()

    private val _activeBattle = MutableStateFlow(ActiveBattleState())
    val activeBattle: StateFlow<ActiveBattleState> = _activeBattle.asStateFlow()

    private val _activeRoom = MutableStateFlow(ActiveRoomSession())
    val activeRoom: StateFlow<ActiveRoomSession> = _activeRoom.asStateFlow()

    private var battleJob: Job? = null
    private var pomodoroJob: Job? = null

    init {
        _studyBuddies.value = repository.getStudyBuddies()
        _studyRooms.value = repository.getStudyRooms()
    }

    // Battle Arena Methods
    fun startBattle(opponent: StudyBuddy? = null) {
        battleJob?.cancel()
        val chosenOpponent = opponent ?: _studyBuddies.value.randomOrNull() ?: WassceDataBank.studyBuddies.first()
        val questions = WassceDataBank.pastQuestions.shuffled().take(5)

        _activeBattle.value = ActiveBattleState(
            isActive = true,
            opponent = chosenOpponent,
            questions = questions,
            currentQuestionIndex = 0,
            questionSecondsRemaining = 15,
            userScore = 0,
            opponentScore = 0,
            userSelectedOption = null,
            opponentSelectedOption = null,
            isRoundComplete = false,
            isBattleFinished = false
        )

        runQuestionTimer()
    }

    private fun runQuestionTimer() {
        battleJob?.cancel()
        battleJob = viewModelScope.launch {
            // Opponent answers after realistic delay (3-8 seconds)
            val opponentDelay = (3..8).random()
            var secondsPassed = 0

            while (_activeBattle.value.questionSecondsRemaining > 0 && !_activeBattle.value.isRoundComplete) {
                delay(1000L)
                secondsPassed++
                val curr = _activeBattle.value
                val newRemaining = curr.questionSecondsRemaining - 1

                var newOpponentChoice = curr.opponentSelectedOption
                if (secondsPassed >= opponentDelay && newOpponentChoice == null) {
                    val q = curr.questions.getOrNull(curr.currentQuestionIndex)
                    if (q != null) {
                        // 85% probability opponent answers correctly
                        newOpponentChoice = if ((1..100).random() <= 85) q.correctOptionIndex else (0..3).filter { it != q.correctOptionIndex }.random()
                    }
                }

                _activeBattle.value = curr.copy(
                    questionSecondsRemaining = newRemaining,
                    opponentSelectedOption = newOpponentChoice
                )

                if (newRemaining <= 0) {
                    endCurrentRound()
                    break
                }
            }
        }
    }

    fun submitUserBattleAnswer(optionIndex: Int) {
        val curr = _activeBattle.value
        if (curr.userSelectedOption != null || curr.isRoundComplete) return
        _activeBattle.value = curr.copy(userSelectedOption = optionIndex)

        // If opponent also answered or timer low, complete round after 1 sec delay
        viewModelScope.launch {
            delay(1200L)
            endCurrentRound()
        }
    }

    private fun endCurrentRound() {
        battleJob?.cancel()
        val curr = _activeBattle.value
        val q = curr.questions.getOrNull(curr.currentQuestionIndex) ?: return

        var uScore = curr.userScore
        var oScore = curr.opponentScore

        if (curr.userSelectedOption == q.correctOptionIndex) {
            uScore += 100 + (curr.questionSecondsRemaining * 5) // speed bonus
        }
        if (curr.opponentSelectedOption == q.correctOptionIndex) {
            oScore += 100 + 40
        }

        _activeBattle.value = curr.copy(
            userScore = uScore,
            opponentScore = oScore,
            isRoundComplete = true
        )

        // Auto advance after 2.5 seconds
        viewModelScope.launch {
            delay(2500L)
            advanceToNextQuestionOrFinish()
        }
    }

    private fun advanceToNextQuestionOrFinish() {
        val curr = _activeBattle.value
        if (curr.currentQuestionIndex < curr.questions.size - 1) {
            _activeBattle.value = curr.copy(
                currentQuestionIndex = curr.currentQuestionIndex + 1,
                questionSecondsRemaining = 15,
                userSelectedOption = null,
                opponentSelectedOption = null,
                isRoundComplete = false
            )
            runQuestionTimer()
        } else {
            // Battle finished
            val won = curr.userScore >= curr.opponentScore
            val winner = if (won) "You Won!" else "${curr.opponent?.name} Won!"
            val xp = if (won) 350 else 120

            _activeBattle.value = curr.copy(
                isBattleFinished = true,
                winnerName = winner,
                xpEarned = xp
            )
        }
    }

    fun exitBattle() {
        battleJob?.cancel()
        _activeBattle.value = ActiveBattleState()
    }

    // Study Room Methods
    fun joinRoom(room: StudyRoom) {
        pomodoroJob?.cancel()
        _activeRoom.value = ActiveRoomSession(
            room = room,
            isJoined = true,
            isTimerRunning = true,
            secondsLeft = room.pomodoroMinutesLeft * 60,
            cheersReceived = listOf("Joined ${room.title} with ${room.participantCount} classmates!")
        )
        startRoomTimer()
    }

    private fun startRoomTimer() {
        pomodoroJob?.cancel()
        pomodoroJob = viewModelScope.launch {
            while (_activeRoom.value.secondsLeft > 0 && _activeRoom.value.isTimerRunning) {
                delay(1000L)
                val curr = _activeRoom.value
                _activeRoom.value = curr.copy(secondsLeft = (curr.secondsLeft - 1).coerceAtLeast(0))
            }
        }
    }

    fun toggleRoomTimer() {
        val curr = _activeRoom.value
        val nextRunning = !curr.isTimerRunning
        _activeRoom.value = curr.copy(isTimerRunning = nextRunning)
        if (nextRunning) {
            startRoomTimer()
        } else {
            pomodoroJob?.cancel()
        }
    }

    fun sendCheer(emoji: String) {
        val curr = _activeRoom.value
        val cheers = listOf("You sent $emoji to the prep room!") + curr.cheersReceived
        _activeRoom.value = curr.copy(cheersReceived = cheers.take(5))
    }

    fun leaveRoom() {
        pomodoroJob?.cancel()
        _activeRoom.value = ActiveRoomSession()
    }
}
