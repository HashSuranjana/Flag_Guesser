package com.example.flagguesse_final
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                // A surface container using the 'background' color from the theme
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

    var submitted by remember { mutableStateOf(false) }
    var correct by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Display the flag images and text boxes for user input
        countryCodes.forEachIndexed { index, countryCode ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = countryFlags[index]),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                if (!submitted || (submitted && !correct)) {
                    OutlinedTextField(
                        value = countryNames[index],
                        onValueChange = {
                            if (!submitted || (submitted && !correct)) {
                                countryNames[index] = it
                                correct = false
                            }
                        },
                        label = { Text("Type the name of the flag") },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                } else {
                    Text(
                        text = countryNames[index],
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        // Submit button
        Button(
            onClick = {
                val correctNames = countryCodes.map { code -> Data().country_names[code] }
                submitted = true
                correct = countryNames == correctNames
            },
            enabled = !submitted || (submitted && !correct),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (submitted) "Submitted" else "Submit")
        }
    }
}
