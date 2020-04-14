package de.narlt.pulsecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var bpm = 0L
    private var hasBpm = false
    private var lastClicks = LinkedList<Long>()
    private val minClicksForCalculation = 4
    private val resetTime = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cContent.setOnClickListener(this)
        resetText()
    }

    override fun onClick(v: View?) {
        if (lastClicks.size > 0 && SystemClock.elapsedRealtime() - lastClicks.last > resetTime) {
            resetText()
            lastClicks.clear()
        }
        lastClicks.add(SystemClock.elapsedRealtime())
        calculateBpm()
    }

    private fun calculateBpm() {
        if (lastClicks.size >= minClicksForCalculation) {
            val elapsed = lastClicks.last - lastClicks.first
            val timePerBeat = elapsed / (lastClicks.size - 1)
            bpm = (60000.0 / timePerBeat).roundToLong()
            hasBpm = true
            tvBPM.text = "$bpm bpm"
        }
    }

    private fun resetText() {
        tvBPM.text = "--- bpm"
    }
}
