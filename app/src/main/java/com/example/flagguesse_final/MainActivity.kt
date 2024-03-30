package com.example.flagguesse_final

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import kotlinx.coroutines.handleCoroutineException
import java.util.*
import kotlin.concurrent.timer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color(150, 174, 196)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp) ){
                            Image(painter = painterResource(id = R.drawable.bgimage), contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }

                        var timerEnabled by remember { mutableStateOf(false) }
                        var timeLeft by remember { mutableStateOf(10) }

                        if (timerEnabled) {
                            CountdownTimer(timeLeft = timeLeft)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Switch(
                                checked = timerEnabled,
                                onCheckedChange = { isChecked ->
                                    timerEnabled = isChecked
                                    if (!isChecked) {
                                        // Reset the timer if switch is turned off
                                        timeLeft = 10
                                    }
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Enable Timer",

                            )
                        }

                        MyButtons()
                    }
                }
            }
        }
    }

    @Composable
    fun MyButtons() {
        Column (

            modifier = Modifier
                .height(150.dp)
                .width(500.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Row(

                modifier = Modifier.width(300.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Button(onClick = {

                    var intent = Intent(this@MainActivity, GuessActivity::class.java)
                    startActivity(intent)},

                    colors = ButtonDefaults.buttonColors(Color.Red)) {

                    Text(text = "Guess Country")

                }
                Button(onClick = {

                    var i = Intent(this@MainActivity,HintActivity::class.java)
                    startActivity(i) },

                    colors = ButtonDefaults.buttonColors(Color.Red)
                    )  {

                    Text(text = "Guess-Hints")

                }
            }

            Row (
                modifier = Modifier.width(300.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {

                Button(onClick = {

                    var i = Intent(this@MainActivity,GuessFlagActivity::class.java)
                    startActivity(i) },

                    colors = ButtonDefaults.buttonColors(Color.Red)) {

                    Text(text = "Guess Flag")

                }

                Button(onClick = {

                    var i = Intent(this@MainActivity,AdvanceActivity::class.java)
                    startActivity(i) },

                    colors = ButtonDefaults.buttonColors(Color.Red)) {

                    Text(text = "Advance-Level")

                }
            }
        }
    }



    @Composable
    fun CountdownTimer(timeLeft: Int) {
        var currentSeconds by remember { mutableStateOf(timeLeft) }

        DisposableEffect(Unit) {
            val timer = timer(period = 1000) {
                currentSeconds--
                if (currentSeconds <= 0) {
                    cancel()
                }
            }

            onDispose {
                timer.cancel()
            }
        }

        Text(
            text = "Time Left: $currentSeconds seconds",

        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FlagGuessefinalTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(150, 174, 196)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp) ){
                    Image(painter = painterResource(id = R.drawable.bgimage), contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }

                var timerEnabled by remember { mutableStateOf(false) }
                var timeLeft by remember { mutableStateOf(10) }

                if (timerEnabled) {
                    CountdownTimer(timeLeft = timeLeft)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Switch(
                        checked = timerEnabled,
                        onCheckedChange = { isChecked ->
                            timerEnabled = isChecked
                            if (!isChecked) {
                                // Reset the timer if switch is turned off
                                timeLeft = 10
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enable Timer",

                        )
                }

                MyButtons()
            }

        }
    }
}
