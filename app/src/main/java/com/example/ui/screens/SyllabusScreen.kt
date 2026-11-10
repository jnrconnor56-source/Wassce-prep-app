package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamWeight
import com.example.data.model.Subject
import com.example.data.model.Topic
import com.example.data.model.TopicStatus
import com.example.ui.theme.ForestEmerald
import com.example.ui.theme.GhanaGold
import com.example.ui.viewmodel.WassceViewModel

@Composable
fun SyllabusScreen(
    viewModel: WassceViewModel,
    onPracticeTopicInPasco: (subjectId: String, topicTitle: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val subjects = viewModel.getSubjects()
    val selectedSubjectId by viewModel.selectedSubjectId.collectAsState()
    val topics = viewModel.getTopicsForCurrentSubject()

    var searchQuery by remember { mutableStateOf("") }
    var expandedTopicId by remember { mutableStateOf<String?>(null) }

    val filteredTopics = if (searchQuery.isBlank()) {
        topics
    } else {
        topics.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.keyPoints.any { pt -> pt.contains(searchQuery, ignoreCase = true) }
        }
    }

    val masteredCount = topics.count { it.status == TopicStatus.MASTERED }
    val inProgressCount = topics.count { it.status == TopicStatus.IN_PROGRESS }
    val currentSubject = subjects.find { it.id == selectedSubjectId } ?: subjects.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("syllabus_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Horizontal Subject Selector Row
        item {
            Column {
                Text(
                    text = "Select WASSCE Subject",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(subjects) { subject ->
                        val isSelected = subject.id == selectedSubjectId
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectSubject(subject.id) },
                            label = { Text(subject.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GhanaGold,
                                selectedLabelColor = Color.Black
                            ),
                            modifier = Modifier.testTag("subject_chip_${subject.id}")
                        )
                    }
                }
            }
        }

        // Subject Overview Card with Mastery Ring
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentSubject?.name ?: "Subject",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentSubject?.description ?: "",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ForestEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "$masteredCount/${topics.size} Mastered",
                                color = ForestEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar
                    val pct = if (topics.isNotEmpty()) (masteredCount * 100) / topics.size else 0
                    LinearProgressIndicator(
                        progress = { pct / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ForestEmerald,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        // Topic Search OutlinedTextField
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search syllabus topics (e.g. circle, calculus)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("syllabus_search_input")
            )
        }

        // Topic Cards List
        items(filteredTopics, key = { it.id }) { topic ->
            val isExpanded = expandedTopicId == topic.id
            TopicCard(
                topic = topic,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedTopicId = if (isExpanded) null else topic.id
                },
                onStatusClick = { viewModel.toggleTopicStatus(topic) },
                onRatingChange = { rating -> viewModel.updateConfidence(topic, rating) },
                onPracticeInPasco = {
                    onPracticeTopicInPasco(topic.subjectId, topic.title)
                }
            )
        }
    }
}

@Composable
private fun TopicCard(
    topic: Topic,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onStatusClick: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onPracticeInPasco: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("topic_card_${topic.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Unit ${topic.unitNumber}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (topic.examWeight == ExamWeight.HIGH) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GhanaGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "★ High Yield",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GhanaGold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = topic.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Status Toggle Chip
                val (chipColor, textColor, textLabel) = when (topic.status) {
                    TopicStatus.MASTERED -> Triple(ForestEmerald, Color.White, "Mastered ✓")
                    TopicStatus.IN_PROGRESS -> Triple(GhanaGold, Color.Black, "Revising ⏳")
                    TopicStatus.NOT_STARTED -> Triple(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurfaceVariant, "Not Started")
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = chipColor,
                    modifier = Modifier
                        .clickable { onStatusClick() }
                        .testTag("topic_status_toggle_${topic.id}")
                ) {
                    Text(
                        text = textLabel,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5-Star Confidence Rating Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Confidence:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                for (star in 1..5) {
                    val isFilled = star <= topic.confidenceRating
                    Icon(
                        imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Rate $star",
                        tint = if (isFilled) GhanaGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onRatingChange(star) }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand Topic Details"
                    )
                }
            }

            // Accordion Body
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "WAEC Syllabus Checklist:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    topic.keyPoints.forEach { pt ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("• ", color = GhanaGold, fontWeight = FontWeight.Bold)
                            Text(text = pt, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onPracticeInPasco,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Practice Topic Pasco", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
