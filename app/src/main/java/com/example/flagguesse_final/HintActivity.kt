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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import kotlinx.coroutines.delay

class HintActivity : ComponentActivity() { // Activity of Hint Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val time = intent.getBooleanExtra("Timer",false)
                    println(time)
                    HintActivityfunc(time) // call the HintActivityfunc() composable function
                }
            }
        }
    }
}

@Composable
fun HintActivityfunc(Time:Boolean) {

    val countryCodes = remember { CountryInfo().countryCodes }

    val countryFlags = remember { CountryInfo().countryFlags }

    val countryNameMap = remember { CountryInfo().country_names }

    var randomCode by rememberSaveable { mutableStateOf(countryCodes.random()) } //mutable variable of generating random code

    var correct_name by rememberSaveable { mutableStateOf(countryNameMap[randomCode] ?: "") } // mutable variable for correct name

    var letter_guess by rememberSaveable { mutableStateOf(List(correct_name.length) { "" }) } //mutable variable of added letter

    var textInput by rememberSaveable { mutableStateOf("") }

    var message by rememberSaveable { mutableStateOf("") } //mutable variable for message

    var wrongGuess by rememberSaveable { mutableStateOf(0) } //mutable variable for wrong answering

    var nextButton by rememberSaveable { mutableStateOf(false) }


    val timeValue by remember { mutableStateOf(10) }

    var timeLeft by rememberSaveable { mutableStateOf(timeValue) }

    val orientation = LocalConfiguration.current.orientation //https://medium.com/@rzmeneghelo/adapt-with-ease-mastering-orientation-changes-in-jetpack-compose-5a298da703d0#:~:text=The%20first%20step%20in%20managing,is%20easily%20accessible%20via%20LocalConfiguration%20.&text=In%20this%20snippet%3A,LocalConfiguration.

    if (orientation == Configuration.ORIENTATION_PORTRAIT){ //Potrait Layout

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

            if (Time){ // if switch is enable


                LaunchedEffect(key1 = timeLeft) {
                    while (timeLeft > 0) {
                        delay(1000L)
                        timeLeft--
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
                        painter = painterResource(id = countryFlags[randomCode] ?: R.drawable.ad),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            Text(
                text = buildString {
                    for (i in correct_name.indices) {
                        append(if (letter_guess[i].isBlank()) "-" else letter_guess[i])
                        append(" ")
                    }
                }, style = TextStyle(fontSize = 25.sp, color = Color.White),

                modifier = Modifier.padding(vertical = 16.dp)
            )
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text(text = "Enter a letter", color = Color.White) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            //Button for submitting
            Button(
                onClick = {
                    if (nextButton) {
                        randomCode = countryCodes.random()
                        correct_name = countryNameMap[randomCode] ?: ""
                        letter_guess = List(correct_name.length) { "" }
                        nextButton = false
                        message = ""
                        wrongGuess = 0
                        timeLeft = 10
                    } else {
                        if (textInput.length == 1) {
                            val inputLower = textInput.lowercase() // Convert input to lowercase
                            val nameLower = correct_name.lowercase() // Convert country name to lowercase
                            if (nameLower.contains(inputLower)) {
                                for (i in correct_name.indices) {
                                    if (correct_name[i].lowercase() == inputLower && letter_guess[i].isBlank()) {
                                        letter_guess = letter_guess.toMutableList().also { it[i] = textInput }
                                    }
                                }
                                if (!letter_guess.contains("")) {
                                    message = "CORRECT! You guessed it right!"
                                    nextButton = true
                                }
                            } else {
                                message = "Try again! Incorrect guess."
                                wrongGuess++
                                if (wrongGuess == 3) {
                                    message = "Correct answer: $correct_name"
                                    nextButton = true
                                }
                            }
                        } else {
                            message = "Please enter a single letter."
                        }
                    }
                }, modifier = Modifier.width(200.dp),

                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(text = if (nextButton) "Next" else "Submit", color = Color.White)
            }
            Text(text = message, color = Color.Red)
        }
    }else{ //Landscape mode
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
                            painter = painterResource(id = countryFlags[randomCode] ?: R.drawable.ad),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = buildString {
                            for (i in correct_name.indices) {
                                append(if (letter_guess[i].isBlank()) "-" else letter_guess[i])
                                append(" ")
                            }
                        }, color = Color.White,

                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        label = { Text(text = "Enter a letter",color =Color.White) },
                        modifier = Modifier.padding(bottom = 16.dp)

                    )
                    //Button for submitting
                    Button(
                        onClick = {
                            //reset the values and generate new values
                            if (nextButton) {
                                randomCode = countryCodes.random()
                                correct_name = countryNameMap[randomCode] ?: ""
                                letter_guess = List(correct_name.length) { "" }
                                nextButton = false
                                message = ""
                                wrongGuess = 0
                                timeLeft = 10 // reset the time
                            } else {
                                if (textInput.length == 1) {
                                    val inputLower = textInput.lowercase() // Converting input to lowercase
                                    val nameLower = correct_name.lowercase() // Converting country name to lowercase
                                    if (nameLower.contains(inputLower)) {
                                        for (i in correct_name.indices) { //run a for loop through the correct name and check if the entered correct
                                            if (correct_name[i].lowercase() == inputLower && letter_guess[i].isBlank()) {
                                                letter_guess = letter_guess.toMutableList().also { it[i] = textInput }
                                            }
                                        }
                                        if (!letter_guess.contains("")) {
                                            message = "CORRECT! You guessed it right!"
                                            nextButton = true
                                        }
                                    } else {
                                        message = "Try again! Incorrect guess."
                                        wrongGuess++
                                        if (wrongGuess == 3) {
                                            message = "Correct answer: $correct_name"
                                            nextButton = true
                                        }
                                    }
                                } else {
                                    message = "Please enter a single letter."
                                }
                            }
                        }, modifier = Modifier.width(200.dp),

                        colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                    ) {
                        Text(text = if (nextButton) "Next" else "Submit", color = Color.White)
                    }
                    Text(text = message, color = Color.Red,modifier = Modifier.offset(y=25.dp))
                }
            }
        }
    }
}





