package de.narlt.pulsecalculator

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import de.narlt.pulsecalculator.ui.theme.PulsecalculatorTheme
import java.util.LinkedList
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    private var bpm = 0L
    private var hasBpm = false
    private var lastClicks = LinkedList<Long>()
    private val minClicksForCalculation = 4
    private val resetTime = 1500
    private var buttonText by mutableStateOf("--- bpm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PulsecalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PulseButton(
                        onClick = this::buttonClicked,
                        textRef = buttonText,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun buttonClicked() {
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
            buttonText = "$bpm bpm"
        }
    }

    private fun resetText() {
        buttonText = "--- bpm"
    }
}

@Composable
fun PulseButton(onClick: () -> Unit, textRef: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .weight(.9f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = textRef,
                    fontSize = TextUnit(30f, TextUnitType.Sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Row(modifier = Modifier.weight(.1f)) {
                Text(
                    text = "(Click the screen in the rhythm of your heart beat)",
                    modifier = Modifier.padding(top = 40.dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PulseButtonPreview() {
    PulsecalculatorTheme {
        PulseButton({}, "text", Modifier.padding(Dp(20f)))
    }
}