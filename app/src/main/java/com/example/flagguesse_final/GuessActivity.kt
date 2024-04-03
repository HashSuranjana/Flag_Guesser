package com.example.flagguesse_final

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import kotlinx.coroutines.delay


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
                    val time = intent.getBooleanExtra("Timer",false)
                    println(time)
                    RandomFlag(time)

                }
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FlagGuessefinalTheme {



        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomFlag(Time:Boolean) {

    val countryCodes = remember { Data().countryCodes } // Get the list of country codes from the Data class

    val countryFlags = remember{ Data().countryFlags } // Get the map of country flags from the Data class

    val countryNameMap = remember { Data().country_names } // Get the map of the country names from the Data class

    val countryNames = countryNameMap.values.toList()  //Get the values of the map country_name and convert into a list

    var expanded by rememberSaveable { mutableStateOf(false) } //initialize the dropdown menu

    var selectedItem by rememberSaveable { mutableStateOf("Select Country") } // Initialize the item in the dropdown menu

    var randomCountryCode by rememberSaveable { mutableStateOf(countryCodes.random()) } //generate random flag from the

    var correctCountryName by rememberSaveable { mutableStateOf(countryNameMap[randomCountryCode] ?: "") }

    var isAnswered by rememberSaveable { mutableStateOf(false) }

    var message by rememberSaveable { mutableStateOf("") }

    val timeValue by remember { mutableStateOf(10) }

    var timeLeft by rememberSaveable { mutableStateOf(timeValue) }





    val orientation = LocalConfiguration.current.orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT){

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color(44, 64, 83)),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Guess The Country",
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
                color = Color.White,
                modifier = Modifier.padding(vertical = 25.dp)
            )

            if (Time){


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if(timeLeft ==0){
                        if ((!isAnswered && selectedItem != "Select Country") || timeLeft==0) {
                            val isCorrect = selectedItem == correctCountryName
                            message = if (isCorrect) "CORRECT! You Guessed It." else "WRONG! $correctCountryName"
                            isAnswered = true
                        } else if ((isAnswered && timeLeft == 0) || (!isAnswered && timeLeft == 0)) {
                            // Reset variables for the next round
                            isAnswered = false
                            message = ""
                            selectedItem = "Select Country"
                            randomCountryCode = countryCodes.random()
                            correctCountryName = countryNameMap[randomCountryCode] ?: ""
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
                    contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(id = countryFlags[randomCountryCode] ?: R.drawable.ad),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
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
                    .offset(y = 300.dp)
                    .width(200.dp),

                onClick = {
                    if (!isAnswered && selectedItem != "Select Country") {
                        val isCorrect = selectedItem == correctCountryName
                        message = if (isCorrect) "CORRECT! You Guessed It." else "WRONG! $correctCountryName"
                        isAnswered = true
                    } else if (isAnswered) {
                        // Reset variables for the next round
                        isAnswered = false
                        message = ""
                        selectedItem = "Select Country"
                        randomCountryCode = countryCodes.random()
                        correctCountryName = countryNameMap[randomCountryCode] ?: ""
                        timeLeft = 10

                    }
                },

                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(text = if (isAnswered) "Next" else "Submit", color = Color.White)
            }


            // Display the correct or incorrect message

            val correctPartColor = if (message.startsWith("CORRECT")) Color.Green else Color.Red
            val incorrectPartColor = Color.Red

            val correctPart = message.substringBefore(":").trim()
            val incorrectPart = message.substringAfter(":").trim()

            Row(modifier = Modifier
                .padding(16.dp)
                .height(70.dp)
                .offset(y = 170.dp)) {
                if (message.startsWith("CORRECT")) {
                    Text(
                        text = correctPart,
                        color = correctPartColor,
                        modifier = Modifier.padding(end = 4.dp),
                        style = TextStyle(fontSize = 25.sp)
                    )
                }
                if (message.startsWith("WRONG")) {
                    Text(
                        text = incorrectPart,
                        color = incorrectPartColor,
                        style = TextStyle(fontSize = 25.sp)
                    )
                }
            }

        }
    }else{
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color(44, 64, 83))
            .padding(horizontal = 40.dp)) {

            Text(text = "Guess The Country",
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
                color = Color.White,
                modifier = Modifier.padding(vertical = 25.dp)
            )

            if (Time){


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if(timeLeft ==0){
                        if ((!isAnswered && selectedItem != "Select Country") || timeLeft==0) {
                            val isCorrect = selectedItem == correctCountryName
                            message = if (isCorrect) "CORRECT! You Guessed It." else "WRONG! $correctCountryName"
                            isAnswered = true
                        } else if ((isAnswered && timeLeft == 0) || (!isAnswered && timeLeft == 0)) {
                            // Reset variables for the next round
                            isAnswered = false
                            message = ""
                            selectedItem = "Select Country"
                            randomCountryCode = countryCodes.random()
                            correctCountryName = countryNameMap[randomCountryCode] ?: ""
                        }
                    }

                }
                Text(text = "Time left: $timeLeft", color = Color.White)

            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier
                            .width(250.dp)
                            .height(220.dp)
                            .background(Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center){
                            Image(
                                painter = painterResource(id = countryFlags[randomCountryCode] ?: R.drawable.ad),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                            )
                        }
                    }

                    Column (horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween){
                        ExposedDropdownMenuBox(modifier = Modifier.offset(x = 150.dp,y=30.dp),
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

                            Button(
                                modifier = Modifier
                                    .offset(x = 30.dp, y = 100.dp)
                                    .width(200.dp),

                                onClick = {
                                    if (!isAnswered && selectedItem != "Select Country") {
                                        val isCorrect = selectedItem == correctCountryName
                                        message = if (isCorrect) "CORRECT! You Guessed It." else "WRONG! $correctCountryName"
                                        isAnswered = true
                                    } else if (isAnswered) {
                                        // Reset variables for the next round
                                        isAnswered = false
                                        message = ""
                                        selectedItem = "Select Country"
                                        randomCountryCode = countryCodes.random()
                                        correctCountryName = countryNameMap[randomCountryCode] ?: ""
                                        timeLeft = 10
                                    }
                                },

                                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                            ) {
                                Text(text = if (isAnswered) "Next" else "Submit",color = Color.White)
                            }


                            // Display the correct or incorrect message

                            val correctPartColor = if (message.startsWith("CORRECT")) Color.Green else Color.Red
                            val incorrectPartColor = Color.Red

                            val correctPart = message.substringBefore(":").trim()
                            val incorrectPart = message.substringAfter(":").trim()

                            Row(modifier = Modifier
                                .padding(16.dp)
                                .height(70.dp)
                                .offset(y = 170.dp)) {
                                if (message.startsWith("CORRECT")) {
                                    Text(
                                        text = correctPart,
                                        color = correctPartColor,
                                        modifier = Modifier.padding(end = 4.dp),
                                        style = TextStyle(fontSize = 25.sp)
                                    )
                                }
                                if (message.startsWith("WRONG")) {
                                    Text(
                                        text = incorrectPart,
                                        color = incorrectPartColor,
                                        style = TextStyle(fontSize = 25.sp)
                                    )
                                }
                            }

                        }
                    }
                }
            }

        }
    }












