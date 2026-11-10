package com.example.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val schoolName: String = "Senior High School",
    val shsTrack: String = "All Programmes",
    val totalXp: Long = 0L,
    val streakDays: Long = 1L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreExamAttempt(
    @DocumentId val id: String = "",
    val userId: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val year: Long = 2024L,
    val score: Long = 0L,
    val totalQuestions: Long = 0L,
    val percentage: Long = 0L,
    val grade: String = "",
    val timeTakenSeconds: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreSearchResearch(
    @DocumentId val id: String = "",
    val userId: String = "",
    val query: String = "",
    val answer: String = "",
    val subjectName: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreAudioNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val subjectId: String = "",
    val transcriptText: String = "",
    val aiStudyGuideSummary: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
