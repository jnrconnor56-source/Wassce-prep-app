package com.example.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileInputStream

private const val TAG = "AudioRecorderHelper"

class AudioRecorderHelper(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    var isRecording: Boolean = false
        private set

    fun startRecording(): Boolean {
        return try {
            val file = File(context.cacheDir, "temp_lecture_recording_${System.currentTimeMillis()}.m4a")
            outputFile = file

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(44100)
                setAudioEncodingBitRate(128000)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            isRecording = true
            Log.d(TAG, "Audio recording started: ${file.absolutePath}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start MediaRecorder", e)
            isRecording = false
            mediaRecorder?.release()
            mediaRecorder = null
            false
        }
    }

    fun stopRecording(): ByteArray? {
        if (!isRecording) return null
        return try {
            mediaRecorder?.apply {
                try {
                    stop()
                } catch (e: Exception) {
                    Log.w(TAG, "Stop failed: ${e.message}")
                }
                release()
            }
            mediaRecorder = null
            isRecording = false

            val file = outputFile
            if (file != null && file.exists() && file.length() > 0) {
                val bytes = FileInputStream(file).use { it.readBytes() }
                Log.d(TAG, "Recording finished, size: ${bytes.size} bytes")
                bytes
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop recording cleanly", e)
            null
        }
    }
}
