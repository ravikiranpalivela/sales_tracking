package com.tekskills.st_tekskills.utils.recording

import android.content.Context
import android.media.AudioManager
import android.media.MediaRecorder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.tekskills.st_tekskills.utils.recording.FileUploadWorker.Companion.sendNotification
import timber.log.Timber
import java.util.Date

class CallStateListener(private val context: Context) : PhoneStateListener() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var filePath = ""

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        super.onCallStateChanged(state, phoneNumber)
        when (state) {
            TelephonyManager.CALL_STATE_OFFHOOK -> startRecording()
            TelephonyManager.CALL_STATE_IDLE -> stopRecording()
            TelephonyManager.CALL_STATE_RINGING -> {
                Timber.e("CALL_STATE_RINGING => ")
            }
        }
    }

    private fun startRecording() {
        Timber.e("startRecording => ")

        if (!isRecording) {
            try {
                Timber.e("startRecording try => ")

                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                )

                mediaRecorder = MediaRecorder()
                mediaRecorder?.apply {
                    Timber.e("startRecording Media Recorder=> ")

                    setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                    setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    setAudioSamplingRate(44100);
                    filePath =
                        context.getExternalFilesDir(null)?.absolutePath + "/recorded_call_${Date().time}.mp3"
                    setOutputFile(filePath)
                    prepare()
                    start()
                    isRecording = true
                }
            } catch (e: Exception) {
//                e.printStackTrace()
                Timber.e("err => " + e.toString())
            }
        }
    }

    private fun stopRecording() {
        Timber.e("stopRecording => ")

        if (isRecording) {
            try {
                Timber.e("stopRecording try =>")
                mediaRecorder?.apply {
                    stop()
                    reset()
                    release()
                    isRecording = false

                    val uploadWorkRequest =
                        OneTimeWorkRequest.Builder(FileUploadWorker::class.java)
                            .setInputData(workDataOf(FileUploadWorker.KEY_FILE_PATH to filePath))
                            .build()
                    context.sendNotification("File Enqueue", "Enqueue.")

                    WorkManager.getInstance(context).enqueue(uploadWorkRequest)
                    Timber.e("stopRecording worm manager assigned =>")

                }
            } catch (e: Exception) {
                Timber.e("err => " + e.toString())
            }
        }
    }
}
