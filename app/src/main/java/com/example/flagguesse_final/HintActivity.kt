package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme

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
                    Hint()
                }
            }
        }
    }
}

@Composable
fun Hint() {
    val countryCodes = remember { Data().countryCodes }
    val countryFlags = remember { Data().countryFlags }
    val countryNameMap = remember { Data().country_names }

    var randomCountryCode by remember { mutableStateOf(countryCodes.random()) }
    var correctCountryName by remember { mutableStateOf(countryNameMap[randomCountryCode] ?: "") }

    var guessedLetters by remember { mutableStateOf(List(correctCountryName.length) { "" }) }
    var userInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var incorrectGuesses by remember { mutableStateOf(0) }
    var showNextButton by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Think and Guess the Country",

            modifier = Modifier.padding(vertical = 25.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.width(250.dp).height(220.dp)
                .background(Color.Blue,shape = RoundedCornerShape(16.dp)),
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
            },

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
            }, modifier = Modifier.width(200.dp)
        ) {
            Text(text = if (showNextButton) "Next" else "Submit")
        }
        Text(text = message, color = Color.Red)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FlagGuessefinalTheme {
        Hint()
    }
}




