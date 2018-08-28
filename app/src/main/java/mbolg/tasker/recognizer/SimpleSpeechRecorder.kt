package mbolg.tasker.recognizer

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.AsyncTask
import android.os.SystemClock
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class SimpleSpeechRecorder @Inject constructor(val context: Context) : SpeechRecorder {
    private val audioFile = File(context.filesDir, "voice_record.pcm")
    var listener: SpeechRecorder.RecordDoneListener? = null
    private var recordTask: RecordTask = RecordTask(audioFile, listener)
    override fun setOnRecordDoneListener(listener: SpeechRecorder.RecordDoneListener) {
        this.listener = listener
    }

    override fun startRecording() {
        recordTask = RecordTask(audioFile, listener)
        recordTask.execute()
    }

    override fun stopRecording() {
        recordTask.cancel(false)
    }


    private class RecordTask(val file: File, val listener: SpeechRecorder.RecordDoneListener?) : AsyncTask<Void, Void, Int>() {

        private val AUDIO_SOURCE = MediaRecorder.AudioSource.MIC
        private val SAMPLE_RATE = 8000 // Hz
        private val ENCODING = AudioFormat.ENCODING_PCM_16BIT
        private val CHANNEL_MASK = AudioFormat.CHANNEL_IN_MONO

        private val BUFFER_SIZE = 2 * AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_MASK, ENCODING)

        override fun doInBackground(vararg p0: Void?): Int {

            var audioRecord: AudioRecord? = null
            var wavOut: FileOutputStream? = null
            var startTime: Long = 0
            var endTime: Long = 0

            try {
                audioRecord = AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_MASK, ENCODING, BUFFER_SIZE)
                wavOut = FileOutputStream(file)

                writeWavHeader(wavOut, CHANNEL_MASK, SAMPLE_RATE, ENCODING)

                val buffer = ByteArray(BUFFER_SIZE)
                var run = true
                var read: Int
                var total: Long = 0

                startTime = SystemClock.elapsedRealtime()
                audioRecord.startRecording()
                while (run && !isCancelled) {
                    read = audioRecord.read(buffer, 0, buffer.size)

                        var i = 0
                        while (i < read && total <= 4294967295L) {
                            wavOut.write(buffer[i].toInt())
                            i++
                            total++
                        }
                        run = false

                }
            } catch (ex: IOException) {

            } finally {
                if (audioRecord != null) {
                    try {
                        if (audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                            audioRecord.stop()
                            endTime = SystemClock.elapsedRealtime()
                        }
                    } catch (ex: IllegalStateException) {

                    }

                    if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
                        audioRecord.release()
                    }
                }
                if (wavOut != null) {
                    try {
                        wavOut.close()
                    } catch (ex: IOException) {

                    }

                }
            }

            try {
                updateWavHeader(file)
            } catch (ex: IOException) {

            }

            return 0
        }

        override fun onCancelled() {
            listener?.onRecordDone()
        }


        private fun writeWavHeader(out: OutputStream, channelMask: Int, sampleRate: Int, encoding: Int) {
            val channels: Short
            when (channelMask) {
                AudioFormat.CHANNEL_IN_MONO -> channels = 1
                AudioFormat.CHANNEL_IN_STEREO -> channels = 2
                else -> throw IllegalArgumentException("Unacceptable channel mask")
            }

            val bitDepth: Short
            when (encoding) {
                AudioFormat.ENCODING_PCM_8BIT -> bitDepth = 8
                AudioFormat.ENCODING_PCM_16BIT -> bitDepth = 16
                AudioFormat.ENCODING_PCM_FLOAT -> bitDepth = 32
                else -> throw IllegalArgumentException("Unacceptable encoding")
            }

            writeWavHeader(out, channels, sampleRate, bitDepth)
        }

        private fun writeWavHeader(out: OutputStream, channels: Short, sampleRate: Int, bitDepth: Short) {
            val littleBytes = ByteBuffer
                    .allocate(14)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putShort(channels)
                    .putInt(sampleRate)
                    .putInt(sampleRate * channels.toInt() * (bitDepth / 8))
                    .putShort((channels * (bitDepth / 8)).toShort())
                    .putShort(bitDepth)
                    .array()

            out.write(byteArrayOf(
                    // RIFF header
                    'R'.toByte(), 'I'.toByte(), 'F'.toByte(), 'F'.toByte(), // ChunkID
                    0, 0, 0, 0, // ChunkSize (must be updated later)
                    'W'.toByte(), 'A'.toByte(), 'V'.toByte(), 'E'.toByte(), // Format
                    // fmt subchunk
                    'f'.toByte(), 'm'.toByte(), 't'.toByte(), ' '.toByte(), // Subchunk1ID
                    16, 0, 0, 0, // Subchunk1Size
                    1, 0, // AudioFormat
                    littleBytes[0], littleBytes[1], // NumChannels
                    littleBytes[2], littleBytes[3], littleBytes[4], littleBytes[5], // SampleRate
                    littleBytes[6], littleBytes[7], littleBytes[8], littleBytes[9], // ByteRate
                    littleBytes[10], littleBytes[11], // BlockAlign
                    littleBytes[12], littleBytes[13], // BitsPerSample
                    // data subchunk
                    'd'.toByte(), 'a'.toByte(), 't'.toByte(), 'a'.toByte(), // Subchunk2ID
                    0, 0, 0, 0)// Subchunk2Size (must be updated later)
            )
        }

        private fun updateWavHeader(wav: File) {
            val sizes = ByteBuffer
                    .allocate(8)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((wav.length() - 8).toInt()) // ChunkSize
                    .putInt((wav.length() - 44).toInt()) // Subchunk2Size
                    .array()

            var accessWave: RandomAccessFile? = null

            try {
                accessWave = RandomAccessFile(wav, "rw")
                accessWave.seek(4)
                accessWave.write(sizes, 0, 4)

                accessWave.seek(40)
                accessWave.write(sizes, 4, 4)
            } catch (ex: IOException) {
                throw ex
            } finally {
                if (accessWave != null) {
                    try {
                        accessWave.close()
                    } catch (ex: IOException) {

                    }

                }
            }
        }
    }


    override fun getFile(): File = audioFile
}