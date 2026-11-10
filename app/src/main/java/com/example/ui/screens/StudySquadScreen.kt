package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyBuddy
import com.example.data.model.StudyRoom
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ForestEmerald
import com.example.ui.theme.GhanaGold
import com.example.ui.viewmodel.StudySquadViewModel

@Composable
fun StudySquadScreen(
    viewModel: StudySquadViewModel,
    modifier: Modifier = Modifier
) {
    val buddies by viewModel.studyBuddies.collectAsState()
    val rooms by viewModel.studyRooms.collectAsState()
    val activeBattle by viewModel.activeBattle.collectAsState()
    val activeRoom by viewModel.activeRoom.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Battles, 1: Rooms, 2: Leaderboard

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("study_squad_screen")
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Quiz Battles") },
                icon = { Icon(Icons.Default.SportsEsports, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Study Rooms") },
                icon = { Icon(Icons.Default.MeetingRoom, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                text = { Text("Leaderboard") },
                icon = { Icon(Icons.Default.Leaderboard, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        when (selectedTabIndex) {
            0 -> {
                // --- 1. QUIZ BATTLES TAB ---
                if (activeBattle.isActive) {
                    ActiveBattleArena(
                        battle = activeBattle,
                        onOptionSelected = { viewModel.submitUserBattleAnswer(it) },
                        onExit = { viewModel.exitBattle() },
                        onPlayAgain = { viewModel.startBattle(activeBattle.opponent) }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .background(CoralRed),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                                        }
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column {
                                            Text(
                                                text = "1v1 WASSCE Quiz Duel",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 17.sp
                                            )
                                            Text(
                                                text = "5 rapid past questions • 15s timer • Win XP",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { viewModel.startBattle() },
                                        colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("auto_match_battle_btn")
                                    ) {
                                        Icon(Icons.Default.Shuffle, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Quick Match (Random Classmate)", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Classmates & Study Buddies",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(buddies) { buddy ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(
                                                try {
                                                    Color(android.graphics.Color.parseColor(buddy.avatarColorHex))
                                                } catch (e: Exception) {
                                                    GhanaGold
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = buddy.avatarInitials,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = buddy.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                            if (buddy.isOnline) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(ForestEmerald)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "${buddy.school} • ${buddy.activeSubject}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "\"${buddy.statusMessage}\"",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1
                                        )
                                    }

                                    Button(
                                        onClick = { viewModel.startBattle(buddy) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.testTag("challenge_${buddy.id}_btn")
                                    ) {
                                        Text("Challenge", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // --- 2. STUDY ROOMS TAB ---
                if (activeRoom.isJoined && activeRoom.room != null) {
                    ActiveRoomView(
                        roomSession = activeRoom,
                        onToggleTimer = { viewModel.toggleRoomTimer() },
                        onSendCheer = { viewModel.sendCheer(it) },
                        onLeave = { viewModel.leaveRoom() }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Groups, contentDescription = null, tint = GhanaGold, modifier = Modifier.size(28.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Virtual Prep Hall",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Join synchronized Pomodoro focus sessions with classmates across Ghana to stay accountable during evening prep.",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Active Prep Rooms",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(rooms) { room ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = ForestEmerald.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "${room.participantCount} Students Studying",
                                                color = ForestEmerald,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        Text(
                                            text = "${room.pomodoroMinutesLeft}m remaining",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GhanaGold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = room.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "Subject: ${room.subjectName} • Host: ${room.hostName} (${room.school})",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Button(
                                        onClick = { viewModel.joinRoom(room) },
                                        colors = ButtonDefaults.buttonColors(containerColor = ForestEmerald),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("join_room_${room.id}_btn")
                                    ) {
                                        Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Join Study Session", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // --- 3. LEADERBOARD TAB ---
                val sortedBuddies = buddies.sortedByDescending { it.weeklyXp }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Ghana SHS Weekly Leaderboard",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rankings based on syllabus completion, quiz battles & Pasco scores",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    itemsIndexed(sortedBuddies) { index, buddy ->
                        val rank = index + 1
                        val medal = when (rank) {
                            1 -> "🥇"
                            2 -> "🥈"
                            3 -> "🥉"
                            else -> "#$rank"
                        }

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (rank == 1) GhanaGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = medal,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(36.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(GhanaGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = buddy.avatarInitials,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = buddy.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = buddy.school,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${buddy.weeklyXp} XP",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = GhanaGold
                                    )
                                    Text(
                                        text = "🔥 ${buddy.streakDays}d streak",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveBattleArena(
    battle: com.example.ui.viewmodel.ActiveBattleState,
    onOptionSelected: (Int) -> Unit,
    onExit: () -> Unit,
    onPlayAgain: () -> Unit
) {
    val currentQ = battle.questions.getOrNull(battle.currentQuestionIndex)

    if (battle.isBattleFinished) {
        // Victory / Finished Dialog Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🏆", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = battle.winnerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Final Score: You ${battle.userScore} — ${battle.opponentScore} ${battle.opponent?.name}",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "+${battle.xpEarned} XP Earned!",
                        color = GhanaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = onExit, modifier = Modifier.weight(1f)) {
                            Text("Exit")
                        }
                        Button(
                            onClick = onPlayAgain,
                            colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Rematch")
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Split Scoreboard Header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // You
                    Column {
                        Text("You", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${battle.userScore} pts", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = GhanaGold)
                    }

                    // 15s Countdown Bar
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${battle.questionSecondsRemaining}s",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (battle.questionSecondsRemaining <= 5) CoralRed else MaterialTheme.colorScheme.onSurface
                        )
                        Text("Question ${battle.currentQuestionIndex + 1} of 5", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    // Opponent
                    Column(horizontalAlignment = Alignment.End) {
                        Text(battle.opponent?.name ?: "Opponent", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${battle.opponentScore} pts", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestEmerald)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Question Card
            if (currentQ != null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {
                            Text(
                                text = "${currentQ.topicTitle} • WASSCE Question",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GhanaGold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentQ.questionText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        itemsIndexed(currentQ.options) { optIdx, optText ->
                            val letter = ('A' + optIdx).toString()
                            val isSelected = battle.userSelectedOption == optIdx
                            val isOpponentSelected = battle.opponentSelectedOption == optIdx
                            val isCorrect = optIdx == currentQ.correctOptionIndex

                            val cardBg = when {
                                battle.isRoundComplete && isCorrect -> ForestEmerald.copy(alpha = 0.25f)
                                battle.isRoundComplete && isSelected && !isCorrect -> CoralRed.copy(alpha = 0.25f)
                                isSelected -> GhanaGold.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surface
                            }

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBg),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, GhanaGold) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = battle.userSelectedOption == null && !battle.isRoundComplete) {
                                        onOptionSelected(optIdx)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$letter.",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = optText,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isOpponentSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = ForestEmerald.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "${battle.opponent?.avatarInitials} answered",
                                                fontSize = 10.sp,
                                                color = ForestEmerald,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onExit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quit Duel")
            }
        }
    }
}

@Composable
fun ActiveRoomView(
    roomSession: com.example.ui.viewmodel.ActiveRoomSession,
    onToggleTimer: () -> Unit,
    onSendCheer: (String) -> Unit,
    onLeave: () -> Unit
) {
    val room = roomSession.room ?: return
    val mins = roomSession.secondsLeft / 60
    val secs = roomSession.secondsLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Room Title & Host Info
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = room.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = onLeave,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Leave", fontSize = 12.sp)
                    }
                }
                Text(
                    text = "${room.subjectName} • ${room.participantCount} Classmates Active",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Circular Pomodoro Timer Display
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(GhanaGold.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        fontWeight = FontWeight.Bold,
                        fontSize = 44.sp,
                        color = GhanaGold
                    )
                    Text(
                        text = if (roomSession.isTimerRunning) "Focusing (Silent Prep)" else "Timer Paused",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            FilledTonalButton(onClick = onToggleTimer) {
                Icon(
                    imageVector = if (roomSession.isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (roomSession.isTimerRunning) "Pause Session" else "Resume Focus")
            }
        }

        // Encouragement Emoji Bar & Cheers Feed
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Cheer on your study partners:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("👏 High Five", "🔥 On Fire", "💡 Eureka", "📚 Keep Pushing").forEach { emoji ->
                        FilledTonalButton(
                            onClick = { onSendCheer(emoji) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(emoji, fontSize = 12.sp)
                        }
                    }
                }

                if (roomSession.cheersReceived.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = roomSession.cheersReceived.first(),
                        fontSize = 11.sp,
                        color = ForestEmerald,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
