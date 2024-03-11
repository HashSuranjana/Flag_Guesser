package com.example.flagguesse_final

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier= Modifier
                        .fillMaxSize()
                        .background(color = Color.LightGray)
                        .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween){

                        Mytext(text = "Flag Guesser")
                        MyButtons()

                    }
                }
            }
        }
    }

    @Composable
    fun MyButtons(){

        Column (

            modifier = Modifier
            .height(150.dp)
            .width(500.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Row(

                modifier = Modifier.width(300.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Button(onClick = {

                    var intent = Intent(this@MainActivity, GuessActivity::class.java)
                    startActivity(intent)}) {
                    Text(text = "Guess Country")

                }
                Button(onClick = {

                    var i = Intent(this@MainActivity,HintActivity::class.java)
                    startActivity(i)

                }) {
                    Text(text = "Guess-Hints")

                }
            }

            Row (
                modifier = Modifier.width(300.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {

                Button(onClick = {

                    var i = Intent(this@MainActivity,GuessFlagActivity::class.java)
                    startActivity(i) }) {
                    Text(text = "Guess Flag")

                }

                Button(onClick = {

                    var i = Intent(this@MainActivity,AdvanceActivity::class.java)
                    startActivity(i) }) {
                    Text(text = "Advance-Level")

                }
            }
        }
    }

    @Composable
    fun Mytext(text:String){
        Text(text = text,
            style= androidx.compose.ui.text.TextStyle(fontSize = 33.sp,
                color = Color.Red
            ))
    }





    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FlagGuessefinalTheme {
            Column (modifier= Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
                .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween){

                Mytext(text = "Flag Guesser")
                MyButtons()

            }

        }
    }
}

