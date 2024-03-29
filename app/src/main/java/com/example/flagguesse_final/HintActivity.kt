package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
    val randomCountryCode by remember { mutableStateOf(countryCodes.random()) }
    val correctCountryName = countryNameMap[randomCountryCode] ?: ""

    var guessedLetters by remember { mutableStateOf(List(correctCountryName.length) { "" }) }
    var userInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Guess The Country",

            modifier = Modifier.padding(vertical = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = countryFlags[randomCountryCode] ?: R.drawable.ad),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
        Text(
            text = guessedLetters.joinToString(" "),

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
                if (userInput.length == 1) {
                    val inputLower = userInput.lowercase() // Convert input to lowercase
                    val nameLower = correctCountryName.lowercase() // Convert country name to lowercase
                    if (nameLower.contains(inputLower)) {
                        guessedLetters = correctCountryName.map { if (it.lowercase() == inputLower) userInput else "" }
                        if (!guessedLetters.contains("")) {
                            message = "Congratulations! You guessed it right!"
                        }
                    } else {
                        message = "Try again! Incorrect guess."
                    }
                } else {
                    message = "Please enter a single letter."
                }
            }
        ) {
            Text(text = "Submit")
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


