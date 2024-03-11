package com.example.flagguesse_final

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.contextaware.ContextAware
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme

class GuessFlagActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessFlagGame()

                }
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FlagGuessefinalTheme {
            GuessFlagGame()

        }
    }
}



@Composable
fun GuessFlagGame() {

    val flagIds = remember { Data().countryFlags.values.toList() } // Get all flags' images as a list

    var countryCode = rememberSaveable { Data().countryCodes.random() } //Get a random country code from the CountryCodes

    var countryName = rememberSaveable {Data().country_names[countryCode]?: "Unknown"}  // Get the relevant country name for that country Code
    
    var countryflag = rememberSaveable {Data().countryFlags[countryCode]?: R.drawable.ad} // Get the relevant Flag image for that country Code


    val excludedFlag = flagIds.indexOf(countryflag) // Find the index of the current flag

    val remainingFlags = flagIds.filterIndexed { index, _ -> index != excludedFlag } // Exclude the current flag

    val randomFlags = remember { remainingFlags.shuffled().take(2) } // Randomly select two flags from the remaining list




    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally){

        Text(text = countryName)

        Image(painter = painterResource(id = countryflag), contentDescription = null)

        Image(painter = painterResource(id = randomFlags[0]), contentDescription = null)
        Image(painter = painterResource(id = randomFlags[1]), contentDescription = null)
    }

    


}

