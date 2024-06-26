package com.example.flagguesse_final

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import kotlinx.coroutines.delay

class GuessFlagActivity : ComponentActivity() { // Activity of GuessFlagActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), // Fill the entire size of the screen
                    color = MaterialTheme.colorScheme.background // Set the background color from the theme
                ) {
                    val time = intent.getBooleanExtra("Timer",false)
                    println(time)
                    ContryFlagfunc(time) // Call the GuessFlagGame composable
                }
            }
        }
    }
}



@Composable
fun ContryFlagfunc(Time:Boolean) {

    var Submitted by rememberSaveable { mutableStateOf(false) } // State for tracking if the guess is submitted

    var Correct by rememberSaveable { mutableStateOf<Boolean?>(null) } // State for tracking if the guess is correct

    var countryCode by rememberSaveable { mutableStateOf(CountryInfo().countryCodes.random()) } // State for storing the randomly selected country code

    var countryName by rememberSaveable { mutableStateOf(CountryInfo().country_names[countryCode] ?: "Unknown") } // State for storing the country name corresponding to the country code

    var countryflag by rememberSaveable { mutableStateOf(CountryInfo().countryFlags[countryCode] ?: R.drawable.ad) } // State for storing the flag image resource id

    var selectedFlagIndex by rememberSaveable { mutableStateOf<Int?>(null) } // State for tracking the index of the selected flag

    var shuffledFlags by rememberSaveable { mutableStateOf(listOf<Int>()) } // State for storing shuffled flag images

    val flagIds = rememberSaveable { CountryInfo().countryFlags.values.toList() } // List of all flag image resource ids

    val remainingFlags = rememberSaveable { flagIds.filterNot { it == countryflag } } // List of remaining flag image resource ids

    val timeValue by remember { mutableStateOf(10) }

    var timeLeft by rememberSaveable { mutableStateOf(timeValue) } // mutable variable for timeleft


    val orientation = LocalConfiguration.current.orientation //https://medium.com/@rzmeneghelo/adapt-with-ease-mastering-orientation-changes-in-jetpack-compose-5a298da703d0#:~:text=The%20first%20step%20in%20managing,is%20easily%20accessible%20via%20LocalConfiguration%20.&text=In%20this%20snippet%3A,LocalConfiguration.

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(

            modifier = Modifier
                .fillMaxSize() // Fill the entire size of the parent
                .background(color = Color(44, 64, 83)), // Add padding around the column
            horizontalAlignment = Alignment.CenterHorizontally, // Align content horizontally to the center
            verticalArrangement = Arrangement.SpaceAround // Arrange content vertically with equal space around

        ) {

            Text(

                text = countryName, // Display the country name
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),color = Color.White
            )

            if (Time){


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }


                    if (selectedFlagIndex != null) { // Check if a flag is selected
                        Submitted = true // Set submitted state
                        val selectedCountryCode =
                            CountryInfo().countryCodes.find { code -> // Find country code corresponding to selected flag
                                CountryInfo().countryFlags[code] == shuffledFlags[selectedFlagIndex!!]
                            }
                        Correct =
                            selectedCountryCode == countryCode // Check if selected country code matches current country code
                    }



                }
                Text(text = "Time left: $timeLeft", color = Color.White)

            }

            if (shuffledFlags.isEmpty()) { // Check if shuffled flags list is empty
                shuffledFlags = (remainingFlags.shuffled()
                    .take(2) + countryflag).shuffled() // Shuffle remaining flags and add current flag
            }

            shuffledFlags.forEachIndexed { index, flagResourceId -> // Iterate through shuffled flags
                val isSelected = selectedFlagIndex == index // Check if current flag is selected
                val boxColor =
                    if (Correct != null && Correct!!) Color.Green else if (Correct != null && !Correct!!) Color.Red else if (isSelected) Color(225, 200, 225) else Color(150, 200, 220) // Define box color based on correctness and selection
                Box(
                    modifier = Modifier
                        .size(200.dp) // Set the size of the box
                        .padding(4.dp) // Add padding around the box
                        .background(
                            color = boxColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            if (!Submitted) { // Check if guess is not submitted
                                selectedFlagIndex = index // Set the selected flag index
                            }
                        },
                    contentAlignment = Alignment.Center, // Align content inside the box to the center
                    content = {
                        Box(modifier =Modifier.size(160.dp),
                            contentAlignment = Alignment.Center,
                            content ={
                                Image(
                                    painter = painterResource(id = flagResourceId), // Load flag image
                                    contentDescription = null, // No content description
                                    modifier = Modifier
                                        .size(150.dp)  // Fill the entire size of the parent
                                        .padding(8.dp) // Add padding inside the box
                                )
                            }
                        )
                    }
                )
            }

            if (Submitted || timeLeft == 0) { // Check if guess is submitted
                val resultText = if (Correct == true) "CORRECT!" else "WRONG!" // Define result text
                Text(
                    text = resultText, // Display result text
                    color = if (Correct == true) Color.Green else Color.Red, // Set text color based on correctness
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (Submitted || timeLeft == 0) { // Check if guess is submitted
                Button(

                    onClick = {
                        Submitted = false // Reset submitted state
                        Correct = null // Reset correctness state
                        countryCode = CountryInfo().countryCodes.random() // Select a new random country code
                        countryName = CountryInfo().country_names[countryCode] ?: "Unknown" // Get the country name corresponding to the country code
                        countryflag = CountryInfo().countryFlags[countryCode] ?: R.drawable.ad // Get the flag image resource id
                        selectedFlagIndex = null // Reset selected flag index
                        shuffledFlags = emptyList() // Clear shuffled flags list
                        timeLeft = 10
                    }
                ) {
                    Text(text = "Next", color = Color.White) // Display Next button
                }
            } else {

                Button(

                    onClick = {
                        if (selectedFlagIndex != null || timeLeft == 0) { // Check if a flag is selected
                            Submitted = true // Set submitted state
                            val selectedCountryCode =
                                CountryInfo().countryCodes.find { code -> // Find country code corresponding to selected flag
                                    CountryInfo().countryFlags[code] == shuffledFlags[selectedFlagIndex!!]
                                }
                            Correct = selectedCountryCode == countryCode // Check if selected country code matches current country code
                        }
                    },
                    enabled = selectedFlagIndex != null // Enable button if a flag is selected
                    , modifier = Modifier.width(200.dp),

                    colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                ) {
                    Text(text = "Submit", color = Color.White) // Display "Submit" button
                }
            }
        }

    }else{

        Column(

            modifier = Modifier
                .fillMaxSize() // Fill the entire size of the parent
                .background(color = Color(44, 64, 83)), // Add padding around the column
            horizontalAlignment = Alignment.CenterHorizontally, // Align content horizontally to the center
            verticalArrangement = Arrangement.SpaceBetween // Arrange content vertically with equal space around
        ) {

            Text(

                text = countryName, // Display the country name
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(vertical = 15.dp),color = Color.White
            )

            if (Time){


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
                    }

                    if(timeLeft ==0){
                        if (selectedFlagIndex != null) { // Check if a flag is selected
                            Submitted = true // Set submitted state
                            val selectedCountryCode =
                                CountryInfo().countryCodes.find { code -> // Find country code corresponding to selected flag
                                    CountryInfo().countryFlags[code] == shuffledFlags[selectedFlagIndex!!]
                                }
                            Correct = selectedCountryCode == countryCode // Check if selected country code matches current country code
                        }
                    }

                }
                Text(text = "Time left: $timeLeft", color = Color.White)

            }


            Row(

                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()){

                if (shuffledFlags.isEmpty()) { // Check if shuffled flags list is empty
                    shuffledFlags = (remainingFlags.shuffled()
                        .take(2) + countryflag).shuffled() // Shuffle remaining flags and add current flag
                }

                shuffledFlags.forEachIndexed { index, flagResourceId -> // Iterate through shuffled flags

                    val isSelected = selectedFlagIndex == index // Check if current flag is selected

                    val boxColor = if (Correct != null && Correct!!) Color.Green else if (Correct != null && !Correct!!) Color.Red else if (isSelected) Color(225, 200, 225) else Color(150, 200, 220) // Define box color based on correctness and selection


                    Box(
                        modifier = Modifier
                            .size(180.dp) // Set the size of the box
                            .padding(4.dp) // Add padding around the box
                            .background(
                                color = boxColor,
                                shape = RoundedCornerShape(16.dp)
                            ) // Set background color and rounded corners
                            .clickable {
                                if (!Submitted) { // Check if guess is not submitted
                                    selectedFlagIndex = index // Set the selected flag index
                                }
                            },
                        contentAlignment = Alignment.Center, // Align content inside the box to the center
                        content = {

                            Box(modifier =Modifier.size(160.dp),
                                contentAlignment = Alignment.Center,
                                content ={

                                    Image(
                                        painter = painterResource(id = flagResourceId), // Load flag image
                                        contentDescription = null, // No content description
                                        modifier = Modifier
                                            .size(150.dp)  // Fill the entire size of the parent
                                            .padding(8.dp) // Add padding inside the box
                                    )
                                }
                            )
                        }
                    )
                }
            }

            if (Submitted || timeLeft == 0) { // Check if guess is submitted

                val resultText = if (Correct == true) "CORRECT!" else "WRONG!" // Define result text

                Text(

                    text = resultText, // Display result text
                    color = if (Correct == true) Color.Green else Color.Red, // Set text color based on correctness
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold

                    )
                )
            }

            if (Submitted || timeLeft == 0) { // Check if guess is submitted

                Button(
                    modifier = Modifier.width(200.dp),
                    onClick = {
                        Submitted = false // Reset submitted state
                        Correct = null // Reset correctness state
                        countryCode = CountryInfo().countryCodes.random() // Select a new random country code
                        countryName = CountryInfo().country_names[countryCode] ?: "Unknown" // Get the country name corresponding to the country code
                        countryflag = CountryInfo().countryFlags[countryCode] ?: R.drawable.ad // Get the flag image resource id
                        selectedFlagIndex = null // Reset selected flag index
                        shuffledFlags = emptyList() // Clear shuffled flags list
                        timeLeft = 10
                    },
                    colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                ) {
                    Text(text = "Next", color = Color.White) // Display "Next" button
                }
            } else {

                Button(

                    onClick = {
                        if (selectedFlagIndex != null || timeLeft == 0) { // Check if a flag is selected
                            Submitted = true // Set submitted state
                            val selectedCountryCode =
                                CountryInfo().countryCodes.find { code -> // Find country code corresponding to selected flag
                                    CountryInfo().countryFlags[code] == shuffledFlags[selectedFlagIndex!!]
                                }
                            Correct =
                                selectedCountryCode == countryCode // Check if selected country code matches current country code
                        }
                    },
                    enabled = selectedFlagIndex != null // Enable button if a flag is selected
                    , modifier = Modifier.width(200.dp),

                    colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                ) {
                    Text(text = "Submit", color = Color.White) // Display "Submit" button
                }
            }
        }
    }
}

