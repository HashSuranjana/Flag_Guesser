package com.example.flagguesse_final

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import kotlinx.coroutines.delay

class HintActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val time = intent.getBooleanExtra("Timer",false)
                    println(time)
                    Hint(time) // call the Hint() composable function
                }
            }
        }
    }
}

@Composable
fun Hint(Time:Boolean) {
    val countryCodes = remember { Data().countryCodes }
    val countryFlags = remember { Data().countryFlags }
    val countryNameMap = remember { Data().country_names }

    var randomCountryCode by rememberSaveable { mutableStateOf(countryCodes.random()) }
    var correctCountryName by rememberSaveable { mutableStateOf(countryNameMap[randomCountryCode] ?: "") }

    var guessedLetters by rememberSaveable { mutableStateOf(List(correctCountryName.length) { "" }) }
    var userInput by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var incorrectGuesses by rememberSaveable { mutableStateOf(0) }
    var showNextButton by rememberSaveable { mutableStateOf(false) }

    val timeValue by remember { mutableStateOf(10) }

    var timeLeft by rememberSaveable { mutableStateOf(timeValue) }

    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(44, 64, 83)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Think and Guess the Country",

                modifier = Modifier.padding(vertical = 3.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 27.sp),
                color = Color.White
            )

            if (Time){


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if(timeLeft ==0){
                        if (showNextButton) {
                            randomCountryCode = countryCodes.random()
                            correctCountryName = countryNameMap[randomCountryCode] ?: ""
                            guessedLetters = List(correctCountryName.length) { "" }
                            showNextButton = false
                            message = ""
                            incorrectGuesses = 0
                        } else {
                            if (userInput.length == 1 || timeLeft == 0 || (userInput.length ==0 && timeLeft == 0)) {
                                val inputLower = userInput.lowercase() // Convert input to lowercase
                                val nameLower = correctCountryName.lowercase() // Convert country name to lowercase
                                if (nameLower.contains(inputLower)) {
                                    for (i in correctCountryName.indices) {
                                        if (correctCountryName[i].lowercase() == inputLower && guessedLetters[i].isBlank()) {
                                            guessedLetters = guessedLetters.toMutableList().also { it[i] = userInput }
                                        }
                                    }
                                    if (!guessedLetters.contains("")) {
                                        message = "Congratulations! You guessed it right!"
                                        showNextButton = true
                                        timeLeft =10
                                    }
                                } else {
                                    message = "Try again! Incorrect guess."
                                    incorrectGuesses++
                                    if (incorrectGuesses == 3 || timeLeft == 0) {
                                        message = "Correct answer: $correctCountryName"
                                        showNextButton = true
                                        timeLeft =10
                                    }
                                }

                            } else {
                                message = "Please enter a single letter."
                            }
                        }
                    }

                }
                Text(text = "Time left: $timeLeft", color = Color.White)

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier
                    .width(250.dp)
                    .height(220.dp)
                    .background(Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                    ){
                    Image(
                        painter = painterResource(id = countryFlags[randomCountryCode] ?: R.drawable.ad),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            Text(
                text = buildString {
                    for (i in correctCountryName.indices) {
                        append(if (guessedLetters[i].isBlank()) "-" else guessedLetters[i])
                        append(" ")
                    }
                }, style = TextStyle(fontSize = 25.sp),

                modifier = Modifier.padding(vertical = 16.dp)
            )
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text(text = "Enter a letter", color = Color.White) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    if (showNextButton) {
                        randomCountryCode = countryCodes.random()
                        correctCountryName = countryNameMap[randomCountryCode] ?: ""
                        guessedLetters = List(correctCountryName.length) { "" }
                        showNextButton = false
                        message = ""
                        incorrectGuesses = 0
                        timeLeft = 10
                    } else {
                        if (userInput.length == 1) {
                            val inputLower = userInput.lowercase() // Convert input to lowercase
                            val nameLower = correctCountryName.lowercase() // Convert country name to lowercase
                            if (nameLower.contains(inputLower)) {
                                for (i in correctCountryName.indices) {
                                    if (correctCountryName[i].lowercase() == inputLower && guessedLetters[i].isBlank()) {
                                        guessedLetters = guessedLetters.toMutableList().also { it[i] = userInput }
                                    }
                                }
                                if (!guessedLetters.contains("")) {
                                    message = "Congratulations! You guessed it right!"
                                    showNextButton = true
                                }
                            } else {
                                message = "Try again! Incorrect guess."
                                incorrectGuesses++
                                if (incorrectGuesses == 3) {
                                    message = "Correct answer: $correctCountryName"
                                    showNextButton = true
                                }
                            }
                        } else {
                            message = "Please enter a single letter."
                        }
                    }
                }, modifier = Modifier.width(200.dp),

                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(text = if (showNextButton || timeLeft == 0) "Next" else "Submit", color = Color.White)
            }
            Text(text = message, color = Color.Red)
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(44, 64, 83)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Think and Guess the Country",

                modifier = Modifier.padding(vertical = 3.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 27.sp),
                color = Color.White
            )
            if (Time){


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if(timeLeft ==0){
                        if (showNextButton) {
                            randomCountryCode = countryCodes.random()
                            correctCountryName = countryNameMap[randomCountryCode] ?: ""
                            guessedLetters = List(correctCountryName.length) { "" }
                            showNextButton = false
                            message = ""
                            incorrectGuesses = 0
                        } else {
                            if (userInput.length == 1) {
                                val inputLower = userInput.lowercase() // Convert input to lowercase
                                val nameLower = correctCountryName.lowercase() // Convert country name to lowercase
                                if (nameLower.contains(inputLower)) {
                                    for (i in correctCountryName.indices) {
                                        if (correctCountryName[i].lowercase() == inputLower && guessedLetters[i].isBlank()) {
                                            guessedLetters = guessedLetters.toMutableList().also { it[i] = userInput }
                                        }
                                    }
                                    if (!guessedLetters.contains("")) {
                                        message = "Congratulations! You guessed it right!"
                                        showNextButton = true
                                    }
                                } else {
                                    message = "Try again! Incorrect guess."
                                    incorrectGuesses++
                                    if (incorrectGuesses == 3) {
                                        message = "Correct answer: $correctCountryName"
                                        showNextButton = true
                                    }
                                }
                            } else {
                                message = "Please enter a single letter."
                            }
                        }

                    }

                }
                Text(text = "Time left: $timeLeft", color = Color.White)

            }
            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()){
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .width(250.dp)
                        .height(220.dp)
                        .background(Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(id = countryFlags[randomCountryCode] ?: R.drawable.ad),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = buildString {
                            for (i in correctCountryName.indices) {
                                append(if (guessedLetters[i].isBlank()) "-" else guessedLetters[i])
                                append(" ")
                            }
                        }, color = Color.White,

                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text(text = "Enter a letter") },
                        modifier = Modifier.padding(bottom = 16.dp)

                    )
                    Button(
                        onClick = {
                            if (showNextButton) {
                                randomCountryCode = countryCodes.random()
                                correctCountryName = countryNameMap[randomCountryCode] ?: ""
                                guessedLetters = List(correctCountryName.length) { "" }
                                showNextButton = false
                                message = ""
                                incorrectGuesses = 0
                                timeLeft = 10
                            } else {
                                if (userInput.length == 1) {
                                    val inputLower = userInput.lowercase() // Convert input to lowercase
                                    val nameLower = correctCountryName.lowercase() // Convert country name to lowercase
                                    if (nameLower.contains(inputLower)) {
                                        for (i in correctCountryName.indices) {
                                            if (correctCountryName[i].lowercase() == inputLower && guessedLetters[i].isBlank()) {
                                                guessedLetters = guessedLetters.toMutableList().also { it[i] = userInput }
                                            }
                                        }
                                        if (!guessedLetters.contains("")) {
                                            message = "Congratulations! You guessed it right!"
                                            showNextButton = true
                                        }
                                    } else {
                                        message = "Try again! Incorrect guess."
                                        incorrectGuesses++
                                        if (incorrectGuesses == 3) {
                                            message = "Correct answer: $correctCountryName"
                                            showNextButton = true
                                        }
                                    }
                                } else {
                                    message = "Please enter a single letter."
                                }
                            }
                        }, modifier = Modifier.width(200.dp),

                        colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                    ) {
                        Text(text = if (showNextButton || timeLeft ==0) "Next" else "Submit", color = Color.White)
                    }
                    Text(text = message, color = Color.Red,modifier = Modifier.offset(y=25.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FlagGuessefinalTheme {
        Hint(true)
    }
}




