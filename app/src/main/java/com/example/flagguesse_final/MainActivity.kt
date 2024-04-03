package com.example.flagguesse_final

//Demo Video Link : https://drive.google.com/file/d/19qi49hSeNf9E5erxWihZjoPCragSwZN4/view?usp=sharing

import android.content.Intent
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme

class MainActivity : ComponentActivity() { // Activity of MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainFunction() // Calling the MainFunction
                }
            }
        }
    }

    @Composable
    fun MainFunction() {
        //https://medium.com/@rzmeneghelo/adapt-with-ease-mastering-orientation-changes-in-jetpack-compose-5a298da703d0#:~:text=The%20first%20step%20in%20managing,is%20easily%20accessible%20via%20LocalConfiguration%20.&text=In%20this%20snippet%3A,LocalConfiguration.
        val orientation = LocalConfiguration.current.orientation // Check the phones orientation whether it is Landscape or Portrait

        if (orientation == Configuration.ORIENTATION_PORTRAIT) { // if orientation is portrait

            var timerEnabled by rememberSaveable { mutableStateOf(false) } // Declaring timerEnabled variable to save the switch state

            Column(

                modifier = Modifier  // Defining the Columns styling
                    .fillMaxSize()
                    .background(color = Color(150, 174, 196)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween

            ) {

                Box( // Defining the Box Styling

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)

                ) {
                    Image( //Defining Image Styling

                        painter = painterResource(id = R.drawable.bgimage), //Get the id of the image from drawable
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }

                Row( // Declaring Switch Row's Styling

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Switch(

                        checked = timerEnabled, //check the switch's state and assign it into timerEnabled variable

                        onCheckedChange = { isChecked ->
                            timerEnabled = isChecked
                        },

                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Blue) //Color of the switch's button
                    )
                    Spacer(modifier = Modifier.width(8.dp)) //Add a space after the button

                    Text(text = "Enable Timer")  //Timer Switch's Text
                }

                Column( //Adding a column to add buttons and declaring its styling

                    modifier = Modifier
                        .height(150.dp)
                        .width(500.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround

                ) {

                    Row( //First Two buttons Row and its styling

                        modifier = Modifier.width(300.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        // Guess Country Button
                        Button(
                            onClick = {
                                //Check whether Switch is on or off
                                if(timerEnabled){

                                    val intent = Intent(this@MainActivity, GuessActivity::class.java)
                                    intent.putExtra("Timer",timerEnabled) // pass the switch's state to other Activities if it is on
                                    startActivity(intent)
                                }else{

                                    val intent =Intent(this@MainActivity,GuessActivity::class.java)
                                    startActivity(intent)
                                }
                            },

                            colors = ButtonDefaults.buttonColors(Color(110, 39, 89)) // Button Color
                        ) {

                            Text(text = "Guess The Country", color = Color.White) // Button Text

                        }

                        //Guess Hints Button
                        Button(
                            onClick = {

                                if(timerEnabled){

                                    val intent = Intent(this@MainActivity, HintActivity::class.java)
                                    intent.putExtra("Timer",timerEnabled) // pass the switch's state to other Activities if it is on
                                    startActivity(intent)

                                }else{

                                    val intent =Intent(this@MainActivity,HintActivity::class.java)
                                    startActivity(intent)
                                }
                            },

                            colors = ButtonDefaults.buttonColors(Color(110, 39, 89)) // Button Color
                        ) {

                            Text(text = "Guess-Hints", color = Color.White) // Button Text

                        }
                    }

                    // Row for second two buttons and styling
                    Row(
                        modifier = Modifier.width(300.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        //Guess Flag button
                        Button(
                            onClick = {

                                if(timerEnabled){

                                    val intent = Intent(this@MainActivity, GuessFlagActivity::class.java)
                                    intent.putExtra("Timer",timerEnabled) // pass the switch's state to other Activities if it is on
                                    startActivity(intent)
                                }else{

                                    val intent =Intent(this@MainActivity,GuessFlagActivity::class.java)
                                    startActivity(intent)
                                }
                            },

                            colors = ButtonDefaults.buttonColors(Color(110, 39, 89)) // Button Color
                        ) {

                            Text(text = "Guess The Flag", color = Color.White) // Button Text

                        }

                        //Advanced Level Button
                        Button(
                            onClick = {

                                if(timerEnabled){

                                    val intent = Intent(this@MainActivity, AdvanceActivity::class.java)
                                    intent.putExtra("Timer",timerEnabled) // pass the switch's state to other Activities if it is on
                                    startActivity(intent)
                                }else{

                                    val intent =Intent(this@MainActivity,AdvanceActivity::class.java) // Button Color
                                    startActivity(intent)
                                }
                            },

                            colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                        ) {

                            Text(text = "Advanced Level", color = Color.White) // Button Text

                        }
                    }
                }
            }
        } else { //If phones orientation is Landscape

            var timerEnabled by rememberSaveable { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(150, 174, 196)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .height(500.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bgimage),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier

                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Switch(

                            checked = timerEnabled,
                            onCheckedChange = { isChecked ->
                                timerEnabled = isChecked

                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.Blue)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Enable Timer",

                            )
                    }

                    Column(

                        modifier = Modifier
                            .height(150.dp)
                            .width(500.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround

                    ) {

                        Row(

                            modifier = Modifier.width(300.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Button(

                                onClick = {
                                    if(timerEnabled){

                                        val intent = Intent(this@MainActivity, GuessActivity::class.java)
                                        intent.putExtra("Timer",timerEnabled)
                                        startActivity(intent)

                                    }else{

                                        val intent =Intent(this@MainActivity,GuessActivity::class.java)
                                        startActivity(intent)
                                    }
                                },

                                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                            ) {

                                Text(
                                    text = "Guess Country",
                                    color = Color.White,
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )

                            }
                            Button(
                                onClick = {
                                    if(timerEnabled){

                                        val intent = Intent(this@MainActivity, HintActivity::class.java)
                                        intent.putExtra("Timer",timerEnabled)
                                        startActivity(intent)

                                    }else{

                                        val intent =Intent(this@MainActivity,HintActivity::class.java)
                                        startActivity(intent)
                                    }
                                },

                                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))
                            ) {

                                Text(text = "Guess-Hints", color = Color.White)

                            }
                        }

                        Row(

                            modifier = Modifier.width(300.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {

                            Button(
                                onClick = {
                                    if(timerEnabled){
                                        val intent = Intent(this@MainActivity, GuessFlagActivity::class.java)
                                        intent.putExtra("Timer",timerEnabled)
                                        startActivity(intent)

                                    }else{

                                        val intent =Intent(this@MainActivity,GuessFlagActivity::class.java)
                                        startActivity(intent)
                                    }
                                },

                                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))

                            ) {

                                Text(text = "Guess Flag", color = Color.White)

                            }

                            Button(

                                onClick = {
                                    if(timerEnabled){

                                        val intent = Intent(this@MainActivity, AdvanceActivity::class.java)
                                        intent.putExtra("Timer",timerEnabled)
                                        startActivity(intent)

                                    }else{

                                        val intent =Intent(this@MainActivity,AdvanceActivity::class.java)
                                        startActivity(intent)
                                    }
                                },

                                colors = ButtonDefaults.buttonColors(Color(110, 39, 89))

                            ) {

                                Text(text = "Advance-Level", color = Color.White)

                            }
                        }
                    }
                }
            }
        }
    }
}
