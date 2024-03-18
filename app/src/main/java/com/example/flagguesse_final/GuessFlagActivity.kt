package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme

class GuessFlagActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessFlagGame()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlagGuessefinalTheme {
        GuessFlagGame()
    }
}

@Composable
fun GuessFlagGame() {
    var isSubmitted by rememberSaveable { mutableStateOf(false) }
    var isCorrect by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var countryCode by rememberSaveable { mutableStateOf(Data().countryCodes.random()) }
    var countryName by rememberSaveable { mutableStateOf(Data().country_names[countryCode] ?: "Unknown") }
    var countryflag by rememberSaveable { mutableStateOf(Data().countryFlags[countryCode] ?: R.drawable.ad) }

    var selectedFlagIndex by remember { mutableStateOf<Int?>(null) }

    var shuffledFlags by remember { mutableStateOf(listOf<Int>()) }

    val flagIds = remember { Data().countryFlags.values.toList() }

    val remainingFlags = remember { flagIds.filterNot { it == countryflag } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Text(
            text = countryName,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        if (shuffledFlags.isEmpty()) {
            shuffledFlags = (remainingFlags.shuffled().take(2) + countryflag).shuffled()
        }

        shuffledFlags.forEachIndexed { index, flagResourceId ->
            val isSelected = selectedFlagIndex == index
            val boxColor = if (isCorrect != null && index == 2 && isCorrect!!) Color.Green else if (isCorrect != null && index == 2 && !isCorrect!!) Color.Red else if (isSelected) Color.Blue else Color.Gray
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(4.dp)
                    .background(color = boxColor, shape = RoundedCornerShape(16.dp)) // Rounded corners
                    .clickable {
                        if (!isSubmitted) {
                            selectedFlagIndex = index
                        }
                    },
                contentAlignment = Alignment.Center,
                content = {
                    Image(
                        painter = painterResource(id = flagResourceId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp) // Add padding to provide space from left and right
                    )
                }
            )
        }

        if (isSubmitted) {
            val resultText = if (isCorrect == true) "CORRECT!" else "WRONG!"
            Text(
                text = resultText,
                color = if (isCorrect == true) Color.Green else Color.Red,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }

        if (isSubmitted) {
            Button(
                onClick = {
                    isSubmitted = false
                    isCorrect = null
                    countryCode = Data().countryCodes.random()
                    countryName = Data().country_names[countryCode] ?: "Unknown"
                    countryflag = Data().countryFlags[countryCode] ?: R.drawable.ad
                    selectedFlagIndex = null
                    shuffledFlags = emptyList() // Clear shuffledFlags to regenerate on next render
                }
            ) {
                Text(text = "Next")
            }
        } else {
            Button(
                onClick = {
                    if (selectedFlagIndex != null) {
                        isSubmitted = true
                        val selectedCountryCode = Data().countryCodes.find { code ->
                            Data().countryFlags[code] == shuffledFlags[selectedFlagIndex!!]
                        }
                        isCorrect = selectedCountryCode == countryCode
                    }
                },
                enabled = selectedFlagIndex != null
            ) {
                Text(text = "Submit")
            }
        }
    }
}




