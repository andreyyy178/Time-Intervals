package com.example.timeintervals

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.timeintervals.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvTime: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var ringtone: Ringtone
    private var numberOfRepeats: Int = 0
    private var numberOfHours: Int = 0
    private var numberOfMinutes: Int = 5
    private var countDownTimer: CountDownTimer? = null
    private val CHANNEL_ID = "notification ID"
    private val notificatoinID = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val snd = Uri.parse("android.resource://"+this.packageName +"/"+R.raw.music)
        //mediaPlayer = MediaPlayer.create(this, snd)

        ringtone = RingtoneManager.getRingtone(this, snd)

       // createNotificationChannel(CHANNEL_ID, "noti")


        binding.seekBarNumInterval.progress = 0
        binding.seekBarTime.progress = 0
        binding.seekBarMinutes.progress = 1
        tvTime = binding.textViewTime
        tvTime.setTextColor(Color.GRAY)

        binding.seekBarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numberOfHours = progress
                binding.textViewCurrentInterval.text = "Interval: ${
                    numberOfHours.toString().padStart(2, '0')
                }h ${numberOfMinutes.toString().padStart(2, '0')}m"
                Log.i("hours", "$numberOfHours")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekBarMinutes.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numberOfMinutes = progress * 5
                binding.textViewCurrentInterval.text = "Interval: ${
                    numberOfHours.toString().padStart(2, '0')
                }h ${numberOfMinutes.toString().padStart(2, '0')}m"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekBarNumInterval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numberOfRepeats = progress
                binding.textViewNumIntervals.text = "Repeats: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.buttonStart.setOnClickListener {
            if (numberOfHours == 0 && numberOfMinutes == 0) {
                Toast.makeText(
                    this,
                    "Interval must be set!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.textViewCurrentInterval.text = "Interval: ${
                    numberOfHours.toString().padStart(2, '0')
                }h ${numberOfMinutes.toString().padStart(2, '0')}m"
                binding.textViewNumIntervals.text = "Repeats left: $numberOfRepeats"
                binding.seekBarMinutes.isEnabled = false
                binding.seekBarTime.isEnabled = false
                binding.seekBarNumInterval.isEnabled = false
                binding.buttonStart.isEnabled = false
                tvTime.setTextColor(Color.BLACK)
                countDownTimer = startTimer(numberOfHours, numberOfMinutes)
            }


        }

        binding.buttonReset.setOnClickListener {
            countDownTimer?.let { it1 -> cancelTimer(it1) }
            binding.seekBarNumInterval.progress = 0
            binding.seekBarTime.progress = 0
            binding.seekBarMinutes.progress = 0
            tvTime.setTextColor(Color.GRAY)
            tvTime.text = "00:00:00"
            numberOfRepeats = 0
            numberOfHours = 0
            numberOfMinutes = 0
            binding.textViewCurrentInterval.text = "Interval: 00h 00m"
            binding.textViewNumIntervals.text = "Repeats: 0"
            binding.seekBarTime.isEnabled = true
            binding.seekBarNumInterval.isEnabled = true
            binding.buttonStart.isEnabled = true
            binding.seekBarMinutes.isEnabled = true
        }

    }

    private fun startTimer(numberOfHours: Int, numberOfMinutes: Int): CountDownTimer {


        var millisUntilFinished =
            TimeUnit.HOURS.toMillis(numberOfHours.toLong()) + TimeUnit.MINUTES.toMillis(
                numberOfMinutes.toLong()
            )

        val countDownTimer: CountDownTimer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onFinish() {
                ringtone.play()
                //sendNotification()
                this.cancel()
                if (numberOfRepeats == 0) {
                    tvTime.text = "FINISHED!"
                    tvTime.setTextColor(Color.GREEN)
                    return
                }
                numberOfRepeats--
                binding.textViewNumIntervals.text = "Repeats left: $numberOfRepeats"
                millisUntilFinished = TimeUnit.HOURS.toMillis(numberOfHours.toLong())
                this.start()

            }

            override fun onTick(p0: Long) {
                var seconds = p0 / 1000
                var minutes = seconds / 60
                val hours = minutes / 60
                Log.i("timer", "$seconds")

                seconds %= 60
                minutes %= 60

                tvTime.text = "${hours.toString().padStart(2, '0')}:${
                    minutes.toString().padStart(2, '0')
                }:${seconds.toString().padStart(2, '0')}"
            }
        }

        countDownTimer.start()

        return countDownTimer
    }

    private fun cancelTimer(countDownTimer: CountDownTimer) {
        countDownTimer.cancel()
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Time Interval")
            //.setContentText("Interval 1/5")
            //.setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            //.setSound(null)

        //mediaPlayer.start()
        with(NotificationManagerCompat.from(this)) {
            notify(notificatoinID, builder.build())
        }

    }

    private fun createNotificationChannel(
        channelID: String,
        channelName: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val snd = Uri.parse("android.resource://"+this.packageName +"/"+R.raw.music)

            val audioAttribute = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val soundUri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                //.authority("blablabla")
                .path(R.raw.music.toString()).build()
            val notyChannel =
                NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH)

            notyChannel.enableLights(true)
            notyChannel.lightColor = Color.RED
            //notyChannel.enableVibration(true)
           // notyChannel.vibrationPattern = longArrayOf(100, 1000, 100, 1000, 100, 1000, 100)
           notyChannel.setSound(snd, audioAttribute)

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notyChannel)
        }
    }

    private fun createNotificationChannel434() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}