package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.data.remote.FirestoreRepository
import com.example.ui.screens.*
import com.example.ui.theme.ForestEmerald
import com.example.ui.theme.GhanaGold
import com.example.ui.theme.WassceTheme
import com.example.ui.viewmodel.AiStudyGuideViewModel
import com.example.ui.viewmodel.ResearchAndAudioViewModel
import com.example.ui.viewmodel.StudySquadViewModel
import com.example.ui.viewmodel.WassceViewModel
import com.example.util.attemptAutoSignIn
import com.example.util.configureAppCheck
import com.example.util.onGoogleSignInClicked
import com.example.util.signOutUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

enum class AppDestination(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    SYLLABUS("Syllabus", Icons.Default.Checklist),
    PASCO("Pasco", Icons.Default.Quiz),
    AI_GUIDE("Textbooks", Icons.Default.AutoAwesome),
    RESEARCH("Search & Mic", Icons.Default.TravelExplore)
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val wassceViewModel: WassceViewModel by viewModels()
    private val aiStudyGuideViewModel: AiStudyGuideViewModel by viewModels()
    private val studySquadViewModel: StudySquadViewModel by viewModels()
    private val researchAndAudioViewModel: ResearchAndAudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration with launch intent before UI/Firebase
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            WassceTheme {
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val credentialManager = remember { CredentialManager.create(context) }
                val firestoreRepo = remember { FirestoreRepository(context) }

                var currentDestination by remember { mutableStateOf(AppDestination.HOME) }
                var showSquadView by remember { mutableStateOf(false) }
                var showAuthDialog by remember { mutableStateOf(false) }
                var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
                var syncFeedback by remember { mutableStateOf("") }

                DisposableEffect(Unit) {
                    val listener = FirebaseAuth.AuthStateListener { auth ->
                        currentUser = auth.currentUser
                    }
                    Firebase.auth.addAuthStateListener(listener)

                    // Silent auto-sign-in attempt on startup
                    attemptAutoSignIn(
                        context = context,
                        credentialManager = credentialManager,
                        onAuthSuccess = { currentUser = Firebase.auth.currentUser },
                        onUnauthenticated = {},
                        scope = coroutineScope
                    )

                    onDispose {
                        Firebase.auth.removeAuthStateListener(listener)
                    }
                }

                BackHandler(enabled = showSquadView || currentDestination != AppDestination.HOME) {
                    if (showSquadView) {
                        showSquadView = false
                    } else {
                        currentDestination = AppDestination.HOME
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = if (showSquadView) "Study Squad & Battles" else "WASSCE Prep Ghana",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            actions = {
                                // Squad Shortcut Button
                                IconButton(onClick = { showSquadView = !showSquadView }) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = "Squad",
                                        tint = if (showSquadView) GhanaGold else MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                // Google Sign-in / Cloud Account Button
                                val user = currentUser
                                if (user != null) {
                                    Surface(
                                        shape = CircleShape,
                                        color = ForestEmerald,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clickable { showAuthDialog = true }
                                            .testTag("google_profile_btn")
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = user.displayName?.take(1) ?: "U",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { showAuthDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = GhanaGold, contentColor = Color.Black),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("google_signin_action_btn")
                                    ) {
                                        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Sign In", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        if (!showSquadView) {
                            NavigationBar(
                                modifier = Modifier.testTag("main_navigation_bar")
                            ) {
                                AppDestination.entries.forEach { dest ->
                                    val selected = currentDestination == dest
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { currentDestination = dest },
                                        icon = {
                                            Icon(
                                                imageVector = dest.icon,
                                                contentDescription = dest.label
                                            )
                                        },
                                        label = { Text(dest.label) },
                                        modifier = Modifier.testTag("nav_item_${dest.name.lowercase()}")
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    if (showSquadView) {
                        StudySquadScreen(
                            viewModel = studySquadViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        when (currentDestination) {
                            AppDestination.HOME -> {
                                HomeScreen(
                                    viewModel = wassceViewModel,
                                    onNavigateToSyllabus = { currentDestination = AppDestination.SYLLABUS },
                                    onNavigateToPasco = { currentDestination = AppDestination.PASCO },
                                    onNavigateToAiGuide = { currentDestination = AppDestination.AI_GUIDE },
                                    onNavigateToSquad = { showSquadView = true },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppDestination.SYLLABUS -> {
                                SyllabusScreen(
                                    viewModel = wassceViewModel,
                                    onPracticeTopicInPasco = { subjectId, topicTitle ->
                                        wassceViewModel.startExamSession(subjectId, isTimed = false, topicFilter = topicTitle)
                                        currentDestination = AppDestination.PASCO
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppDestination.PASCO -> {
                                PascoScreen(
                                    viewModel = wassceViewModel,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppDestination.AI_GUIDE -> {
                                AiStudyGuideScreen(
                                    viewModel = aiStudyGuideViewModel,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppDestination.RESEARCH -> {
                                ResearchAndAudioScreen(
                                    viewModel = researchAndAudioViewModel,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }

                    // Google Auth & Firestore Cloud Sync Dialog
                    if (showAuthDialog) {
                        AlertDialog(
                            onDismissRequest = { showAuthDialog = false },
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = GhanaGold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (currentUser != null) "Cloud Account & Sync" else "Google Sign-In")
                                }
                            },
                            text = {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    val user = currentUser
                                    if (user != null) {
                                        Text(
                                            text = "Signed in as: ${user.displayName ?: "Student"}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = user.email ?: "",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Your syllabus progress, exam attempts, and audio notes are automatically backed up to Firestore Enterprise cloud storage.",
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )

                                        if (syncFeedback.isNotBlank()) {
                                            Text(syncFeedback, color = ForestEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    firestoreRepo.saveUserProfile(
                                                        displayName = user.displayName ?: "SHS Candidate",
                                                        email = user.email ?: "",
                                                        school = "Senior High School (Ghana)",
                                                        track = wassceViewModel.selectedTrack.value.displayName
                                                    )
                                                    syncFeedback = "✓ Synced with Firestore Cloud!"
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = ForestEmerald),
                                            modifier = Modifier.fillMaxWidth().testTag("sync_to_firestore_btn")
                                        ) {
                                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Sync All to Cloud Now")
                                        }
                                    } else {
                                        Text(
                                            text = "Sign in with your Google account to back up your WASSCE syllabus mastery, sync past paper scores, and access your lecture notes across devices.",
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Button(
                                            onClick = {
                                                onGoogleSignInClicked(
                                                    context = context,
                                                    credentialManager = credentialManager,
                                                    onAuthSuccess = {
                                                        currentUser = Firebase.auth.currentUser
                                                        showAuthDialog = false
                                                    },
                                                    onAuthError = { err -> syncFeedback = err },
                                                    scope = coroutineScope
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = GhanaGold, contentColor = Color.Black),
                                            modifier = Modifier.fillMaxWidth().testTag("dialog_google_signin_btn")
                                        ) {
                                            Icon(Icons.Default.Login, contentDescription = null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Sign in with Google", fontWeight = FontWeight.Bold)
                                        }

                                        if (syncFeedback.isNotBlank()) {
                                            Text(syncFeedback, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                if (currentUser != null) {
                                    OutlinedButton(
                                        onClick = {
                                            signOutUser(
                                                credentialManager = credentialManager,
                                                scope = coroutineScope,
                                                onSignOutComplete = {
                                                    currentUser = null
                                                    showAuthDialog = false
                                                }
                                            )
                                        }
                                    ) {
                                        Text("Sign Out")
                                    }
                                } else {
                                    TextButton(onClick = { showAuthDialog = false }) {
                                        Text("Close")
                                    }
                                }
                            },
                            dismissButton = {
                                if (currentUser != null) {
                                    TextButton(onClick = { showAuthDialog = false }) {
                                        Text("Done")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
