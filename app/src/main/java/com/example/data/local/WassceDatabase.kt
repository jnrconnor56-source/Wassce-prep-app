package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "topic_progress")
data class TopicProgressEntity(
    @PrimaryKey val topicId: String,
    val subjectId: String,
    val status: String,
    val confidenceRating: Int,
    val lastUpdatedMillis: Long
)

@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val subjectName: String,
    val year: Int,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val grade: String,
    val timeTakenSeconds: Int,
    val completedAtMillis: Long
)

@Entity(tableName = "offline_study_guides")
data class OfflineStudyGuideEntity(
    @PrimaryKey val id: String,
    val textbookId: String,
    val subjectId: String,
    val chapterTitle: String,
    val summaryNotes: String,
    val formulasJson: String,
    val examinerWarningsJson: String,
    val quizQuestionsJson: String,
    val createdAtMillis: Long
)

@Dao
interface WassceDao {
    @Query("SELECT * FROM topic_progress")
    fun getAllProgress(): Flow<List<TopicProgressEntity>>

    @Query("SELECT * FROM topic_progress WHERE subjectId = :subjectId")
    fun getProgressBySubject(subjectId: String): Flow<List<TopicProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopicProgress(entity: TopicProgressEntity)

    @Query("SELECT * FROM exam_attempts ORDER BY completedAtMillis DESC")
    fun getAllAttempts(): Flow<List<ExamAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExamAttemptEntity)

    @Query("SELECT * FROM offline_study_guides ORDER BY createdAtMillis DESC")
    fun getAllStudyGuides(): Flow<List<OfflineStudyGuideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyGuide(guide: OfflineStudyGuideEntity)

    @Query("DELETE FROM offline_study_guides WHERE id = :id")
    suspend fun deleteStudyGuide(id: String)
}

@Database(
    entities = [TopicProgressEntity::class, ExamAttemptEntity::class, OfflineStudyGuideEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WassceDatabase : RoomDatabase() {
    abstract fun wassceDao(): WassceDao

    companion object {
        @Volatile
        private var INSTANCE: WassceDatabase? = null

        fun getDatabase(context: Context): WassceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WassceDatabase::class.java,
                    "wassce_prep_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
