package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.ui.theme.ForestEmerald
import com.example.ui.theme.GhanaGold
import com.example.ui.viewmodel.ResearchAndAudioViewModel
import com.example.ui.viewmodel.SearchState
import com.example.ui.viewmodel.TranscribeState

@Composable
fun ResearchAndAudioScreen(
    viewModel: ResearchAndAudioViewModel,
    modifier: Modifier = Modifier
) {
    val searchState by viewModel.searchState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val transcribeState by viewModel.transcribeState.collectAsState()
    val currentTranscript by viewModel.currentTranscript.collectAsState()
    val syncedNotes by viewModel.syncedAudioNotes.collectAsState()

    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Search Grounding, 1: Audio Transcribe

    // Audio recording permission launcher
    var hasAudioPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (isGranted) {
            viewModel.startRecording()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("research_and_audio_screen")
    ) {
        // Sub-Tab Switcher
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("Search Grounding") },
                icon = { Icon(Icons.Default.TravelExplore, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("Audio Transcriber") },
                icon = { Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        if (selectedSubTab == 0) {
            // --- 1. GOOGLE SEARCH GROUNDING TAB ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Google Search Grounded Research",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Powered by gemini-3.8-flash with Google Search Grounding for verified WAEC & WASSCE facts",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Search Box
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                placeholder = { Text("Ask any WASSCE question or syllabus update...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("search_grounding_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.performGoogleSearchResearch("WASSCE Ghana") },
                                enabled = searchQuery.isNotBlank() && searchState !is SearchState.Searching,
                                colors = ButtonDefaults.buttonColors(containerColor = GhanaGold, contentColor = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_search_btn")
                            ) {
                                if (searchState is SearchState.Searching) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.Black, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Searching Google via Gemini...")
                                } else {
                                    Icon(Icons.Default.TravelExplore, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Search Grounded Answers", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Suggested Search Queries
                item {
                    Text("Suggested Research Queries:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val suggestions = listOf(
                        "Latest 2024 Chief Examiner warnings for Elective Maths",
                        "WAEC Ghana Integrated Science grading boundaries",
                        "WASSCE English summary writing rules and penalties",
                        "Core Maths Bearing and 3D Trigonometry rules"
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        suggestions.forEach { sug ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.updateSearchQuery(sug)
                                        viewModel.performGoogleSearchResearch("WASSCE Ghana")
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.ArrowOutward, contentDescription = null, modifier = Modifier.size(16.dp), tint = GhanaGold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(sug, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                // Search Results
                when (val state = searchState) {
                    is SearchState.Searching -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.TravelExplore, contentDescription = null, tint = GhanaGold)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Google Search Grounding Active...", fontWeight = FontWeight.Bold, color = GhanaGold)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(state.streamedText.ifEmpty { "Grounding query against web sources..." }, fontSize = 14.sp, lineHeight = 20.sp)
                                }
                            }
                        }
                    }
                    is SearchState.Success -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ForestEmerald.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "✓ Verified via Google Search Grounding",
                                            color = ForestEmerald,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(state.fullAnswer, fontSize = 14.sp, lineHeight = 22.sp)
                                }
                            }
                        }
                    }
                    is SearchState.Error -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(state.message, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    is SearchState.Idle -> {}
                }
            }
        } else {
            // --- 2. AUDIO TRANSCRIPTION TAB ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Classroom Audio Transcriber",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Record your teacher during prep or class and transcribe with gemini-3.5-transcribe",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Microphone Input Controller Card
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val isRecording = transcribeState is TranscribeState.Recording
                            val isTranscribing = transcribeState is TranscribeState.Transcribing

                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(if (isRecording) Color(0xFFEF4444) else GhanaGold)
                                    .clickable {
                                        if (isRecording) {
                                            viewModel.stopRecordingAndTranscribe()
                                        } else {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    }
                                    .testTag("microphone_record_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                    contentDescription = "Microphone",
                                    tint = Color.Black,
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = when {
                                    isRecording -> "Recording Microphone... Tap to Stop & Transcribe"
                                    isTranscribing -> "Transcribing with gemini-3.5-transcribe..."
                                    else -> "Tap Microphone to Record Audio"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Captures audio in high fidelity and generates verbatim English transcripts",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {
                                    viewModel.transcribeSampleClassLecture("Core Mathematics: Circle Theorems")
                                },
                                modifier = Modifier.testTag("transcribe_sample_btn")
                            ) {
                                Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Transcribe Sample Classroom Lecture", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Transcription Result Card
                when (val state = transcribeState) {
                    is TranscribeState.Transcribing -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = GhanaGold)
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text("Transcribing Audio...", fontWeight = FontWeight.Bold)
                                        Text("Model: gemini-3.5-transcribe", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                    is TranscribeState.Success -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
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
                                                text = "✓ Transcribed via gemini-3.5-transcribe",
                                                color = ForestEmerald,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        IconButton(onClick = { viewModel.resetTranscribe() }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = state.transcript,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }
                    is TranscribeState.Error -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(state.message, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    else -> {}
                }

                // Synced Audio Notes from Firestore
                if (syncedNotes.isNotEmpty()) {
                    item {
                        Text(
                            text = "Synced Audio Notes (Firestore)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(syncedNotes) { note ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(note.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(note.transcriptText, fontSize = 12.sp, maxLines = 3, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
