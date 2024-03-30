package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme

class AdvanceActivity : ComponentActivity() {
    private var totalMarks by mutableStateOf(0)

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
    var countryFlags by remember { mutableStateOf(countryCodes.map { code -> Data().countryFlags[code] ?: R.drawable.ad }) }
    val countryNames = remember { mutableStateListOf("", "", "") }

    var attempts by remember { mutableStateOf(0) }
    var correctAttempts by remember { mutableStateOf(0) }
    var submitted by remember { mutableStateOf(false) }
    var totalMarks by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
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
                    .background(Color.Gray, shape = RoundedCornerShape(16.dp)),
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

            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(if (correctAttempts == 3 || attempts == 3) "Next" else "Submit")
        }

        // Display total marks obtained by the user
        Text(
            text = "Marks: $totalMarks / 3",
            modifier = Modifier.padding(8.dp)
        )
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
