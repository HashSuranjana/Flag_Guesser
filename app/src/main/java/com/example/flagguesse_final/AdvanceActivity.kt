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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme

class AdvanceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val randomCountryCodes = remember { Data().countryCodes.shuffled().take(3) }
                    DisplayFlagsAndInputs(randomCountryCodes)
                }
            }
        }
    }
}

@Composable
fun DisplayFlagsAndInputs(randomCountryCodes: List<String>) {
    var countryCodes by rememberSaveable { mutableStateOf(randomCountryCodes.shuffled().take(3)) }
    var countryFlags by rememberSaveable { mutableStateOf(countryCodes.map { code -> Data().countryFlags[code] ?: R.drawable.ad }) }
    val countryNames = remember { mutableStateListOf("", "", "") }

    var attempts by rememberSaveable { mutableStateOf(0) }
    var correctAttempts by rememberSaveable { mutableStateOf(0) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var totalMarks by rememberSaveable { mutableStateOf(0) }

    val orientation = LocalConfiguration.current.orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(150, 174, 196)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            countryCodes.forEachIndexed { index, countryCode ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .fillMaxWidth()
                ) {
                    Box(modifier = Modifier
                        .width(150.dp)
                        .height(120.dp)
                        .background(color = Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = countryFlags[index]),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    val isCorrect = countryNames[index] == Data().country_names[countryCode]
                    val isEditable = !submitted || (submitted && !isCorrect && attempts < 3)

                    val backgroundColor = when {
                        submitted && isCorrect -> Color.Green
                        submitted && !isCorrect -> Color.Red
                        else -> Color.Transparent
                    }

                    OutlinedTextField(
                        value = countryNames[index],
                        onValueChange = {
                            if (isEditable) {
                                countryNames[index] = it
                            }
                        },
                        label = { Text("Type Country Name") },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(300.dp)
                            .background(color = backgroundColor),
                        enabled = isEditable
                    )
                    if (!isCorrect && submitted && attempts == 3) {
                        Text(
                            text = Data().country_names[countryCode] ?: "",
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Red
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (correctAttempts == 3 || attempts == 3) {
                        // Generate new set of random country codes
                        val newRandomCountryCodes = Data().countryCodes.shuffled().take(3)
                        countryCodes = newRandomCountryCodes
                        countryFlags = newRandomCountryCodes.map { code -> Data().countryFlags[code] ?: R.drawable.ad }
                        countryNames.clear()
                        countryNames.addAll(listOf("", "", ""))
                        attempts = 0
                        submitted = false
                        totalMarks = correctAttempts

                    } else {
                        if (!submitted) {
                            submitted = true
                            attempts++
                            correctAttempts = countryNames.countIndexed { index, name ->
                                name == Data().country_names[countryCodes[index]]
                            }
                            totalMarks = correctAttempts


                        } else {
                            attempts++
                        }
                    }
                }
                , modifier = Modifier.width(200.dp),

                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(if (correctAttempts == 3 || attempts == 3) "Next" else "Submit")
            }

            // Display total marks obtained by the user
            Text(
                text = "Marks: $totalMarks / 3",
                modifier = Modifier.padding(8.dp)
            )
        }
    }else{
        Column(modifier = Modifier
            .fillMaxHeight()
            .background(color = Color(150, 174, 196))
            .padding(top = 60.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ) {
                countryCodes.forEachIndexed { index, countryCode ->
                    Row(horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(vertical = 3.dp)
                        ) {
                            Box(modifier = Modifier
                                .width(150.dp)
                                .height(120.dp)
                                .background(color = Color(150, 200, 220), shape = RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = countryFlags[index]),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            val isCorrect = countryNames[index] == Data().country_names[countryCode]
                            val isEditable = !submitted || (submitted && !isCorrect && attempts < 3)

                            val backgroundColor = when {
                                submitted && isCorrect -> Color.Green
                                submitted && !isCorrect -> Color.Red
                                else -> Color.Transparent
                            }

                            OutlinedTextField(
                                value = countryNames[index],
                                onValueChange = {
                                    if (isEditable) {
                                        countryNames[index] = it
                                    }
                                },
                                label = { Text("Type Country Name") },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .width(200.dp)
                                    .background(color = backgroundColor),
                                enabled = isEditable
                            )
                            if (!isCorrect && submitted && attempts == 3) {
                                Text(
                                    text = Data().country_names[countryCode] ?: "",
                                    modifier = Modifier.padding(start = 10.dp),
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (correctAttempts == 3 || attempts == 3) {
                        // Generate new set of random country codes
                        val newRandomCountryCodes = Data().countryCodes.shuffled().take(3)
                        countryCodes = newRandomCountryCodes
                        countryFlags = newRandomCountryCodes.map { code -> Data().countryFlags[code] ?: R.drawable.ad }
                        countryNames.clear()
                        countryNames.addAll(listOf("", "", ""))
                        attempts = 0
                        submitted = false
                        totalMarks = correctAttempts

                    } else {
                        if (!submitted) {
                            submitted = true
                            attempts++
                            correctAttempts = countryNames.countIndexed { index, name ->
                                name == Data().country_names[countryCodes[index]]
                            }
                            totalMarks = correctAttempts


                        } else {
                            attempts++
                        }
                    }
                }
                , modifier = Modifier
                    .width(200.dp)
                    .offset( x=-10.dp, y=20.dp),

                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
            ) {
                Text(if (correctAttempts == 3 || attempts == 3) "Next" else "Submit")
            }

            // Display total marks obtained by the user
            Text(
                text = "Marks: $totalMarks / 3",
                modifier = Modifier
                    .padding(8.dp)
                    .offset(x=370.dp,y=-310.dp)
            )
        }
    }
}

inline fun <T> Iterable<T>.countIndexed(predicate: (Int, T) -> Boolean): Int {
    var count = 0
    for ((index, element) in this.withIndex()) {
        if (predicate(index, element)) {
            count++
        }
    }
    return count
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    FlagGuessefinalTheme {
        val randomCountryCodes = remember { Data().countryCodes.shuffled().take(3) }
        DisplayFlagsAndInputs(randomCountryCodes)
    }
}
