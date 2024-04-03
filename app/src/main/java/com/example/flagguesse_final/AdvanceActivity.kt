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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import kotlinx.coroutines.delay


class AdvanceActivity : ComponentActivity() { // Activity of Advanced Level
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val time = intent.getBooleanExtra("Timer", false) // Extracting time value from intent extras

                    val randomCountryCodes = remember { CountryInfo().countryCodes.shuffled().take(3) } // Generating random country codes

                    AdvancedActivity(randomCountryCodes, time)
                }
            }
        }
    }
}

// Composable function for displaying Flags and Inputs
@Composable
fun AdvancedActivity(randomCountryCodes: List<String>, Time: Boolean) {

    var countryCodes by rememberSaveable { mutableStateOf(randomCountryCodes.shuffled().take(3)) } // Mutable state variable for country codes

    var countryFlags by rememberSaveable { mutableStateOf(countryCodes.map { code -> CountryInfo().countryFlags[code] ?: R.drawable.ad }) } // Mutable state variable for flags

    var attempts by rememberSaveable { mutableStateOf(0) } // Mutable state variable for user attempts

    val countryNames = remember { mutableStateListOf("", "", "") } // Mutable state variable for Country names

    var correctAttempts by rememberSaveable { mutableStateOf(0) } // Mutable state variable for user's correct attempts

    var totalMarks by rememberSaveable { mutableStateOf(0) } // Mutable state variables for total marks

    var submitted by rememberSaveable { mutableStateOf(false) } // Mutable state variable to check submitted or not

    val timeValue by remember { mutableStateOf(10) } // Immutable state variables for add time's value

    var timeLeft by rememberSaveable { mutableStateOf(timeValue) } // Mutable state variables for time left

    //https://medium.com/@rzmeneghelo/adapt-with-ease-mastering-orientation-changes-in-jetpack-compose-5a298da703d0#:~:text=The%20first%20step%20in%20managing,is%20easily%20accessible%20via%20LocalConfiguration%20.&text=In%20this%20snippet%3A,LocalConfiguration.
    val orientation = LocalConfiguration.current.orientation // Configuring screen orientation


    if (orientation == Configuration.ORIENTATION_PORTRAIT) { // if orientation is Portrait

        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(44, 64, 83)),
            verticalArrangement = Arrangement.SpaceBetween

        ) {

            if (Time) { // Displaying time left if switch is enabled

                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if (timeLeft == 0) {
                        // Handling end of time
                        if (correctAttempts == 3 || attempts == 3) {

                            // Reset the values and Generating new values
                            val newRandomCountryCodes = CountryInfo().countryCodes.shuffled().take(3)
                            countryCodes = newRandomCountryCodes
                            countryFlags = newRandomCountryCodes.map { code -> CountryInfo().countryFlags[code] ?: R.drawable.ad }
                            countryNames.clear()
                            countryNames.addAll(listOf("", "", ""))
                            attempts = 0
                            submitted = false
                            totalMarks = correctAttempts

                        } else {
                            if (!submitted) {
                                //Submit buttons functioning
                                submitted = true
                                attempts++
                                correctAttempts = countryNames.countIndexed { index, name ->
                                    name == CountryInfo().country_names[countryCodes[index]]
                                }
                                totalMarks = correctAttempts

                            } else {
                                attempts++
                            }
                        }
                    }
                }
                Text(text = "Time left: $timeLeft", color = Color.White)
            }

            // Displaying each flag and input field

            countryCodes.forEachIndexed { index, countryCode ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(120.dp)
                            .background(color = Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = countryFlags[index]),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    val isCorrect = countryNames[index] == CountryInfo().country_names[countryCode]
                    val isEditable = !submitted || (submitted && !isCorrect && attempts < 3)

                    val backgroundColor = when {
                        submitted && isCorrect -> Color.Green
                        submitted && !isCorrect -> Color.Red
                        else -> Color.Transparent
                    }

                    // Input field for country name of the flag
                    OutlinedTextField(
                        value = countryNames[index],
                        onValueChange = {
                            if (isEditable) {
                                countryNames[index] = it
                            }
                        },
                        label = { Text("Type Country Name", color = Color.White) },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(300.dp)
                            .background(color = backgroundColor),
                        enabled = isEditable
                    )
                    // Displaying correct country name if submission is incorrect after attempts
                    if (!isCorrect && submitted && attempts == 3) {
                        Text(
                            text = CountryInfo().country_names[countryCode] ?: "",
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Red
                        )
                    }
                }
            }
            // Button
            Button(
                onClick = {
                    if ((correctAttempts == 3 || attempts == 3) || timeLeft == 0) {
                        // Reset and generate new Values
                        val newRandomCountryCodes = CountryInfo().countryCodes.shuffled().take(3)
                        countryCodes = newRandomCountryCodes
                        countryFlags = newRandomCountryCodes.map { code -> CountryInfo().countryFlags[code] ?: R.drawable.ad }
                        countryNames.clear()
                        countryNames.addAll(listOf("", "", ""))
                        attempts = 0
                        submitted = false
                        totalMarks = correctAttempts
                        timeLeft = 10
                    } else {
                        if (!submitted) {
                            // Handling user's submission
                            submitted = true
                            attempts++
                            correctAttempts = countryNames.countIndexed { index, name ->
                                name == CountryInfo().country_names[countryCodes[index]]
                            }
                            totalMarks = correctAttempts
                        } else {
                            attempts++
                        }
                    }
                },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(
                    if ((correctAttempts == 3 || attempts == 3) || timeLeft == 0) "Next" else "Submit",
                    color = Color.White
                )
            }
            // Displaying total marks user got
            Text(
                text = "Marks: $totalMarks / 3",
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
    } else { // Landscape layout

        Column(

            modifier = Modifier
                .fillMaxHeight()
                .background(color = Color(44, 64, 83))
                .padding(top = 35.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            if (Time) { // Displaying time left if switch is enabled

                LaunchedEffect(key1 = timeLeft) {

                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if (timeLeft == 0) {
                        // Handling end of time
                        if (correctAttempts == 3 || attempts == 3) {
                            // Reset the values and Generating new values
                            val newRandomCountryCodes = CountryInfo().countryCodes.shuffled().take(3)
                            countryCodes = newRandomCountryCodes
                            countryFlags = newRandomCountryCodes.map { code -> CountryInfo().countryFlags[code] ?: R.drawable.ad }
                            countryNames.clear()
                            countryNames.addAll(listOf("", "", ""))
                            attempts = 0
                            submitted = false
                            totalMarks = correctAttempts
                        } else {
                            if (!submitted) {
                                // Handling submission
                                submitted = true
                                attempts++
                                correctAttempts = countryNames.countIndexed { index, name ->
                                    name == CountryInfo().country_names[countryCodes[index]]
                                }
                                totalMarks = correctAttempts
                            } else {
                                attempts++
                            }
                        }
                    }
                }
                Text(text = "Time left: $timeLeft", color = Color.White)
            }
            // Displaying flags and input fields
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                countryCodes.forEachIndexed { index, countryCode ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(120.dp)
                                    .background(color = Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = countryFlags[index]),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            val isCorrect = countryNames[index] == CountryInfo().country_names[countryCode]
                            val isEditable = !submitted || (submitted && !isCorrect && attempts < 3)

                            val backgroundColor = when {
                                submitted && isCorrect -> Color.Green
                                submitted && !isCorrect -> Color.Red
                                else -> Color.Transparent
                            }

                            // Input field for names of the countries
                            OutlinedTextField(
                                value = countryNames[index],
                                onValueChange = {
                                    if (isEditable) {
                                        countryNames[index] = it
                                    }
                                },
                                label = { Text("Type Country Name", color = Color.White) },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .width(200.dp)
                                    .background(color = backgroundColor),
                                enabled = isEditable
                            )
                            // Displaying correct country name if submit is incorrect after attempts
                            if (!isCorrect && submitted && attempts == 3) {
                                Text(
                                    text = CountryInfo().country_names[countryCode] ?: "",
                                    modifier = Modifier.padding(start = 10.dp),
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }
            // Button
            Button(
                onClick = {
                    if ((correctAttempts == 3 || attempts == 3) || timeLeft == 0) {

                        // Reset the values and Generating new values
                        val newRandomCountryCodes = CountryInfo().countryCodes.shuffled().take(3)
                        countryCodes = newRandomCountryCodes
                        countryFlags = newRandomCountryCodes.map { code -> CountryInfo().countryFlags[code] ?: R.drawable.ad }
                        countryNames.clear()
                        countryNames.addAll(listOf("", "", ""))
                        attempts = 0
                        submitted = false
                        totalMarks = correctAttempts
                        timeLeft = 10
                    } else {
                        if (!submitted) {
                            // Handling User's Submit
                            submitted = true
                            attempts++
                            correctAttempts = countryNames.countIndexed { index, name ->
                                name == CountryInfo().country_names[countryCodes[index]]
                            }
                            totalMarks = correctAttempts
                        } else {
                            attempts++
                        }
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .offset(x = -10.dp, y = 20.dp),
                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(
                    if ((correctAttempts == 3 || attempts == 3) || timeLeft == 0) "Next" else "Submit",
                    color = Color.White
                )
            }
            // Displaying total marks user's got
            Text(
                text = "Marks: $totalMarks / 3",
                modifier = Modifier
                    .padding(8.dp)
                    .offset(x = 340.dp, y = -320.dp),

                color = Color.White
            )
        }
    }
}

//  Function to count elements with index
inline fun <T> Iterable<T>.countIndexed(predicate: (Int, T) -> Boolean): Int {
    var count = 0
    for ((index, element) in this.withIndex()) {
        if (predicate(index, element)) {
            count++
        }
    }
    return count
}

