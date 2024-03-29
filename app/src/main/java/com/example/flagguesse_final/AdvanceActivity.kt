package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
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
import com.example.flagguesse_final.Data
import com.example.flagguesse_final.R
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
    var countryCodes by remember { mutableStateOf(randomCountryCodes.shuffled().take(3)) }
    var countryFlags by remember { mutableStateOf(countryCodes.map { code -> Data().countryFlags[code] ?: R.drawable.ad }) }
    val countryNames = remember { mutableStateListOf("", "", "") }

    var attempts by remember { mutableStateOf(0) }
    var correctAttempts by remember { mutableStateOf(0) }
    var submitted by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        countryCodes.forEachIndexed { index, countryCode ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 2.dp).fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = countryFlags[index]),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
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
                    correctAttempts = 0
                    submitted = false
                } else {
                    if (!submitted) {
                        submitted = true
                        attempts++
                        correctAttempts = countryNames.countIndexed { index, name ->
                            name == Data().country_names[countryCodes[index]]
                        }
                    } else {
                        attempts++
                    }
                }
            },
            modifier = Modifier.width(100.dp)
        ) {
            Text(if (correctAttempts == 3 || attempts == 3) "Next" else "Submit")
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
