package com.example.timeintervals

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.timeintervals.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var numberOfIntervals by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.seekBarNumInterval.progress = 0
        binding.seekBarTime.progress = 0
        numberOfIntervals = binding.seekBarNumInterval.progress + 1


        binding.seekBarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val displayProgress = progress+1
                binding.textViewCurrentInterval.text = "Current time interval: $displayProgress h"
                binding.textViewTime.text = "$displayProgress:00:00"
                binding.textViewTimeBarLabel.text = "$displayProgress h"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarNumInterval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val displayProgress = progress+1
                binding.textViewNumIntervals.text = "Number of intervals left: $displayProgress"
                binding.textViewIntervaBarlLabel.text = "$displayProgress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.buttonStart.setOnClickListener {

            startTimer()
            binding.seekBarTime.isEnabled = false
            binding.seekBarNumInterval.isEnabled = false
            binding.buttonStart.isEnabled = false
            binding.textViewTime.setTextColor(Color.GREEN)
        }
    }

    private fun startTimer() {
        var timeInMilliSeconds = 11000L
        val countDownTimer: CountDownTimer = object : CountDownTimer(timeInMilliSeconds, 1000) {
            override fun onFinish() {
                this.cancel()
                timeInMilliSeconds = 11000L
                this.start()
            }

            override fun onTick(p0: Long) {
                val seconds = (p0 / 1000) % 60
                Log.i("timer", "$seconds")
            }
        }
        countDownTimer.start()
    }




}