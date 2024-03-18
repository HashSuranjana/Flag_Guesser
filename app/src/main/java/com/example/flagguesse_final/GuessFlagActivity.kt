package com.example.flagguesse_final

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.contextaware.ContextAware
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
import androidx.compose.ui.graphics.RectangleShape
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

    val flagIds = remember { Data().countryFlags.values.toList() }

    val remainingFlags = remember { flagIds.filterNot { it == countryflag } }

    val shuffledFlags = remember { (remainingFlags.shuffled().take(2) + countryflag).shuffled() }

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

        shuffledFlags.forEachIndexed { index, flagResourceId ->
            val boxColor = if (isCorrect != null && index == 2 && isCorrect!!) Color.Green else if (isCorrect != null && index == 2 && !isCorrect!!) Color.Red else Color.Gray
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(130.dp)
                    .clickable {
                        if (!isSubmitted) {
                            val selectedCountryCode = Data().countryCodes.find { code ->
                                Data().countryFlags[code] == flagResourceId
                            }
                            if (selectedCountryCode == countryCode) {
                                isCorrect = true
                            } else {
                                isCorrect = false
                            }
                            isSubmitted = true
                        }
                    },
                contentAlignment = Alignment.Center,
                content = {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(4.dp)
                            .background(color = boxColor)
                    ) {
                        Image(
                            painter = painterResource(id = flagResourceId),
                            contentDescription = null
                        )
                    }
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
                }
            ) {
                Text(text = "Next")
            }
        } else {
            Button(
                onClick = {
                    isSubmitted = true
                    val selectedCountryCode = Data().countryCodes.find { code ->
                        Data().countryFlags[code] == shuffledFlags[2]
                    }
                    isCorrect = selectedCountryCode == countryCode
                }
            ) {
                Text(text = "Submit")
            }
        }
    }
}










