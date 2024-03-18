package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme


class GuessActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {



            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    RandomFlag()

                }
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FlagGuessefinalTheme {

            RandomFlag()

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomFlag() {

    val countryCodes = remember { Data().countryCodes } // Get the list of country codes from the Data class

    val countryFlags = remember{ Data().countryFlags } // Get the map of country flags from the Data class

    val countryNameMap = remember { Data().country_names } // Get the map of the country names from the Data class

    val countryNames = countryNameMap.values.toList()  //Get the values of the map country_name and convert into a list

    var expanded by rememberSaveable { mutableStateOf(false) }

    var selectedItem by rememberSaveable { mutableStateOf("Select Country") }

    var randomCountryCode by rememberSaveable { mutableStateOf(countryCodes.random()) }

    var correctCountryName by rememberSaveable { mutableStateOf(countryNameMap[randomCountryCode] ?: "") }

    var isAnswered by rememberSaveable { mutableStateOf(false) }

    var message by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Guess The Country",
            style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
            modifier = Modifier.offset(y= 50.dp)
        )
        Box(
            modifier = Modifier
                .offset(y = 100.dp)
                .fillMaxWidth()
                .background(color = Color.Cyan),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = countryFlags[randomCountryCode] ?: R.drawable.ad),
                contentDescription = null
            )
        }

        ExposedDropdownMenuBox(modifier = Modifier.offset(y=150.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                countryNames.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectedItem = countryNames[index]
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .offset(y = 400.dp)
                .width(200.dp),

            onClick = {
                if (!isAnswered && selectedItem != "Select Country") {
                    val isCorrect = selectedItem == correctCountryName
                    message = if (isCorrect) "Correct! You Guessed It." else "Wrong! Country is: $correctCountryName"
                    isAnswered = true
                } else if (isAnswered) {
                    // Reset variables for the next round
                    isAnswered = false
                    message = ""
                    selectedItem = "Select Country"
                    randomCountryCode = countryCodes.random()
                    correctCountryName = countryNameMap[randomCountryCode] ?: ""
                }
            }
        ) {
            Text(text = if (isAnswered) "Next" else "Submit")
        }


        // Display the correct or incorrect message

        val correctPartColor = if (message.startsWith("Correct")) Color.Green else Color.Red
        val incorrectPartColor = Color.Blue

        val correctPart = message.substringBefore(":").trim()
        val incorrectPart = message.substringAfter(":").trim()

        Row(modifier = Modifier
            .padding(16.dp)
            .height(70.dp)
            .offset(y = 200.dp)) {
            Text(
                text = correctPart,
                color = correctPartColor,
                modifier = Modifier.padding(end = 4.dp),
                style = TextStyle(fontSize = 20.sp)
            )
            if (message.startsWith("Wrong")) {
                Text(
                    text = incorrectPart,
                    color = incorrectPartColor,
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    }
}











