package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

private const val TAG = "FirestoreRepository"

class FirestoreRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google to access cloud data.")
    }

    suspend fun saveUserProfile(displayName: String, email: String, school: String, track: String) {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        try {
            val payload = mapOf(
                "userId" to uid,
                "displayName" to displayName,
                "email" to email,
                "schoolName" to school,
                "shsTrack" to track,
                "totalXp" to 1500L,
                "streakDays" to 14L,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            userRef.set(payload).await()
            Log.d(TAG, "User profile saved in Firestore for $uid")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user profile", e)
        }
    }

    fun observeUserProfile(): Flow<FirestoreUserProfile?> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(null)
            return@flow
        }
        val userRef = db.collection("users").document(uid)
        userRef.snapshots().map { snapshot ->
            snapshot.toObject(FirestoreUserProfile::class.java)
        }.collect { emit(it) }
    }

    suspend fun saveExamAttempt(
        subjectId: String,
        subjectName: String,
        year: Int,
        score: Int,
        total: Int,
        percentage: Int,
        grade: String,
        timeTakenSeconds: Int
    ) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("attempts").document()
        try {
            val payload = mapOf(
                "userId" to uid,
                "subjectId" to subjectId,
                "subjectName" to subjectName,
                "year" to year.toLong(),
                "score" to score.toLong(),
                "totalQuestions" to total.toLong(),
                "percentage" to percentage.toLong(),
                "grade" to grade,
                "timeTakenSeconds" to timeTakenSeconds.toLong(),
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Log.d(TAG, "Exam attempt synced to Firestore for $uid")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync exam attempt", e)
        }
    }

    suspend fun saveSearchResearch(query: String, answer: String, subjectName: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("research").document()
        try {
            val payload = mapOf(
                "userId" to uid,
                "query" to query,
                "answer" to answer,
                "subjectName" to subjectName,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Log.d(TAG, "Search research synced to Firestore")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync search research", e)
        }
    }

    suspend fun saveAudioNote(title: String, subjectId: String, transcript: String, summary: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("audio_notes").document()
        try {
            val payload = mapOf(
                "userId" to uid,
                "title" to title,
                "subjectId" to subjectId,
                "transcriptText" to transcript,
                "aiStudyGuideSummary" to summary,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Log.d(TAG, "Audio note synced to Firestore")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync audio note", e)
        }
    }

    fun observeAudioNotes(): Flow<List<FirestoreAudioNote>> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(emptyList())
            return@flow
        }
        db.collection("users").document(uid).collection("audio_notes")
            .snapshots()
            .map { snap -> snap.toObjects(FirestoreAudioNote::class.java) }
            .collect { emit(it) }
    }
}
