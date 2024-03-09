package com.example.flagguesse_final

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flagguesse_final.ui.theme.FlagGuessefinalTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlin.random.Random



class GuessActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {



            FlagGuessefinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    RandomFlag()

                }
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FlagGuessefinalTheme {

            RandomFlag()

        }
    }
}

@Composable
fun RandomFlag() {

    val countryCodes =
        rememberSaveable { Data().countryCodes } // Get the list of country codes from the Data class

    val countryFlags =
        rememberSaveable { Data().countryFlags } // Get the map of country flags from the Data class

    val randomCountryCode =
        rememberSaveable { countryCodes.random() } // Randomly select a country code

    val flagResourceId = countryFlags[randomCountryCode]
        ?: R.drawable.ad  // Get the corresponding flag for the selected country code


    val countryNames = rememberSaveable { Data().country_names } // Get the list of country names from the Data class

    var expanded by remember { mutableStateOf(false) } // State to hold the dropdown menu visibility

    var namelist = listOf("Hello","World")

    var selectedItem by rememberSaveable { mutableStateOf("") }

    var textFiledSize by rememberSaveable { mutableStateOf(Size.Zero)}

    val icon  = if (expanded){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }



    // Display the selected flag
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = flagResourceId),
                contentDescription = null
            )
        }

        Box(modifier = Modifier.fillMaxWidth() ){

            Column {
                OutlinedTextField(value = selectedItem, onValueChange = {selectedItem = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFiledSize = coordinates.size.toSize()
                        },
                    label = { Text(text = "Select")}, trailingIcon = {
                        Icon(icon,"",Modifier.clickable { expanded !=expanded })
                    })

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) { textFiledSize.width.toDp() })
                ) {
                    namelist.forEach { label ->
                        DropdownMenuItem(text ={Text(label)}, onClick = {
                            selectedItem = label
                            expanded = false
                        })
                    }
                }

            }


        }

    }
}






