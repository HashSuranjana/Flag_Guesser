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
    val countryCodes = remember { randomCountryCodes.shuffled().take(3) }
    val countryFlags = remember { countryCodes.map { code -> Data().countryFlags[code] ?: R.drawable.ad } }
    val countryNames = remember { mutableStateListOf("", "", "") }

    var attempts by remember { mutableStateOf(0) }
    var correctAttempts by remember { mutableStateOf(0) }
    var submitted by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(4.dp)
    ) {
        countryCodes.forEachIndexed { index, countryCode ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Image(
                    painter = painterResource(id = countryFlags[index]),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

                val isEditable = !submitted || (submitted && attempts < 3)
                val isCorrect = countryNames[index] == Data().country_names[countryCode]
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
                if (!submitted) {
                    submitted = true
                    attempts++
                    correctAttempts = countryNames.countIndexed { index, name ->
                        name == Data().country_names[countryCodes[index]]
                    }
                } else {
                    attempts++
                }
            },
            enabled = !submitted || attempts < 3,
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