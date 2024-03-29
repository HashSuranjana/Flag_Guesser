package com.example.flagguesse_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

class HintActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Hint()

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hint() {

    val countryCodes = remember { Data().countryCodes } // Get the list of country codes from the Data class

    val countryFlags = remember{ Data().countryFlags } // Get the map of country flags from the Data class

    val countryNameMap = remember { Data().country_names } // Get the map of the country names from the Data class

    val countryNames = countryNameMap.values.toList()  //Get the values of the map country_name and convert into a list

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

        Button(
            modifier = Modifier
                .offset(y = 400.dp)
                .width(200.dp),

            onClick = {

            }
        ) {
            Text(text = if (isAnswered) "Next" else "Submit")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FlagGuessefinalTheme {
        Hint()
    }
}