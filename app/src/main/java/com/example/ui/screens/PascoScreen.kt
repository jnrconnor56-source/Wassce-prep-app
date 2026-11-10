package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.data.model.PastQuestion
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ForestEmerald
import com.example.ui.theme.GhanaGold
import com.example.ui.viewmodel.WassceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PascoScreen(
    viewModel: WassceViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.examSession.collectAsState()
    val attempts by viewModel.examAttempts.collectAsState()
    val subjects = viewModel.getSubjects()
    val selectedSubjectId by viewModel.selectedSubjectId.collectAsState()

    var selectedModeIndex by remember { mutableIntStateOf(0) } // 0: Timed Mock, 1: Topical Drill
    var showQuestionSheet by remember { mutableStateOf(false) }

    if (!session.isActive) {
        // --- 1. PASCO HUB LAUNCHER ---
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("pasco_launcher_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "WASSCE Past Questions (Pasco)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Practice real WAEC Paper 1 questions offline with step-by-step solutions",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Mode Selector Tabs
            item {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = selectedModeIndex == 0,
                        onClick = { selectedModeIndex = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Text("Timed Mock Exam", fontSize = 13.sp)
                    }
                    SegmentedButton(
                        selected = selectedModeIndex == 1,
                        onClick = { selectedModeIndex = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Text("Topical Drill", fontSize = 13.sp)
                    }
                }
            }

            // Subject Selector Chips
            item {
                Column {
                    Text(
                        text = "Choose Subject",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(subjects) { subj ->
                            val isSelected = subj.id == selectedSubjectId
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectSubject(subj.id) },
                                label = { Text(subj.name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GhanaGold,
                                    selectedLabelColor = Color.Black
                                ),
                                modifier = Modifier.testTag("pasco_subject_chip_${subj.id}")
                            )
                        }
                    }
                }
            }

            // Launch Action Card
            item {
                val currentSubj = subjects.find { it.id == selectedSubjectId } ?: subjects.firstOrNull()
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
                                    .background(GhanaGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (selectedModeIndex == 0) Icons.Default.Timer else Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = if (selectedModeIndex == 0) "Full Exam Simulation" else "Focused Topical Drill",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = "${currentSubj?.name ?: "Subject"} • 2015-2024 Bank",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (selectedModeIndex == 0)
                                "• Timed exam conditions (25 minutes)\n• Immediate score calculation and grade estimate (A1–F9)\n• Full step-by-step solutions after submission"
                            else
                                "• Untimed practice by syllabus topic\n• Detailed explanations and examiner tips\n• Master problem areas at your own pace",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                viewModel.startExamSession(
                                    subjectId = selectedSubjectId,
                                    isTimed = (selectedModeIndex == 0)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GhanaGold, contentColor = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_pasco_session_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedModeIndex == 0) "Begin Timed Mock Exam" else "Begin Topical Practice",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            // Past Attempts & Score History
            if (attempts.isNotEmpty()) {
                item {
                    Text(
                        text = "Score History & WASSCE Grades",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(attempts) { attempt ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
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
                            Column {
                                Text(
                                    text = attempt.subjectName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Score: ${attempt.score}/${attempt.totalQuestions} • ${attempt.percentage}%",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (attempt.percentage >= 70) ForestEmerald else GhanaGold
                            ) {
                                Text(
                                    text = attempt.grade,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else if (session.isSubmitted) {
        // --- 2. EXAM RESULTS & COMPREHENSIVE SOLUTIONS REVIEW ---
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("pasco_results_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Scorecard Header
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val pct = if (session.questions.isNotEmpty()) (session.finalScore * 100) / session.questions.size else 0
                        Surface(
                            shape = CircleShape,
                            color = if (pct >= 70) ForestEmerald else GhanaGold,
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${pct}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = session.finalGrade,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${session.subjectName} • ${session.finalScore} of ${session.questions.size} correct",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.exitExam() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Exit")
                            }
                            Button(
                                onClick = {
                                    viewModel.startExamSession(session.subjectId, session.isTimedMode)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GhanaGold, contentColor = Color.Black),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Retake Exam")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Detailed Step-by-Step Solutions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Question solutions breakdown
            itemsIndexed(session.questions) { idx, q ->
                val userChoice = session.userAnswers[idx]
                val isCorrect = userChoice == q.correctOptionIndex

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
                            Text(
                                text = "Question ${idx + 1} (${q.year})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isCorrect) ForestEmerald.copy(alpha = 0.2f) else CoralRed.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (isCorrect) "Correct ✓" else "Incorrect ✗",
                                    color = if (isCorrect) ForestEmerald else CoralRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = q.questionText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        // Options view
                        q.options.forEachIndexed { optIdx, optText ->
                            val letter = ('A' + optIdx).toString()
                            val isThisCorrect = optIdx == q.correctOptionIndex
                            val isThisUserChoice = userChoice == optIdx

                            val optBg = when {
                                isThisCorrect -> ForestEmerald.copy(alpha = 0.15f)
                                isThisUserChoice -> CoralRed.copy(alpha = 0.15f)
                                else -> Color.Transparent
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(optBg)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$letter.",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isThisCorrect) ForestEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = optText,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        // Step-by-step Explanation Box
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Step-by-Step Working:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GhanaGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = q.explanation,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "WAEC Examiner Tip: ${q.examinerTip}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // --- 3. ACTIVE EXAM / QUESTION PLAYER ---
        val currentQ = session.questions.getOrNull(session.currentQuestionIndex)
        val selectedOption = session.userAnswers[session.currentQuestionIndex]
        val isBookmarked = session.bookmarkedQuestions.contains(session.currentQuestionIndex)

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("active_pasco_arena")
        ) {
            // Top Status Bar: Subject, Question Index & Countdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = session.subjectName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Question ${session.currentQuestionIndex + 1} of ${session.questions.size}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Timer if timed mode
                    if (session.isTimedMode) {
                        val mins = session.remainingSeconds / 60
                        val secs = session.remainingSeconds % 60
                        val timerColor = if (session.remainingSeconds < 300) CoralRed else GhanaGold

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = timerColor.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = timerColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%02d:%02d", mins, secs),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = timerColor
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Bookmark toggle
                    IconButton(
                        onClick = { viewModel.toggleBookmark(session.currentQuestionIndex) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GhanaGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Submit button
                    Button(
                        onClick = { viewModel.submitExam() },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestEmerald),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("submit_exam_btn")
                    ) {
                        Text("Submit", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Question Card & Options
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
                            .padding(18.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "${currentQ.topicTitle} • WAEC ${currentQ.year}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = currentQ.questionText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        // Options A, B, C, D
                        itemsIndexed(currentQ.options) { optIdx, optText ->
                            val letter = ('A' + optIdx).toString()
                            val isSelected = selectedOption == optIdx

                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) GhanaGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, GhanaGold) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { viewModel.selectOption(session.currentQuestionIndex, optIdx) }
                                    .testTag("option_${letter.lowercase()}_card")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) GhanaGold else MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = letter,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = optText,
                                        fontSize = 15.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Navigation Footer (Prev, Sheet, Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.prevQuestion() },
                    enabled = session.currentQuestionIndex > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Previous")
                }

                Spacer(modifier = Modifier.width(12.dp))

                FilledTonalIconButton(
                    onClick = { showQuestionSheet = !showQuestionSheet },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.GridOn, contentDescription = "Question Grid")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { viewModel.nextQuestion() },
                    enabled = session.currentQuestionIndex < session.questions.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = GhanaGold, contentColor = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }

            // Question Navigation Drawer Sheet
            if (showQuestionSheet) {
                ModalBottomSheet(onDismissRequest = { showQuestionSheet = false }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Question Sheet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.heightIn(max = 240.dp)
                        ) {
                            items(session.questions.size) { idx ->
                                val answered = session.userAnswers.containsKey(idx)
                                val isCurrent = session.currentQuestionIndex == idx
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when {
                                                isCurrent -> GhanaGold
                                                answered -> ForestEmerald.copy(alpha = 0.3f)
                                                else -> MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        )
                                        .clickable {
                                            viewModel.jumpToQuestion(idx)
                                            showQuestionSheet = false
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${idx + 1}",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.Black else MaterialTheme.colorScheme.onSurface
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
