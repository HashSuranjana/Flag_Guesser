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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomFlag() {

    val countryCodes = remember { Data().countryCodes } // Get the list of country codes from the Data class

    val countryFlags = remember{ Data().countryFlags } // Get the map of country flags from the Data class

    val countryNameMap = remember { Data().country_names } // Get the map of the country names from the Data class

    val countryNames = countryNameMap.values.toList()  //Get the values of the map country_name and convert into a list

    var expanded by rememberSaveable { mutableStateOf(false) }

    var selectedItem by rememberSaveable { mutableStateOf("Select Country") }

    val randomCountryCode = rememberSaveable { countryCodes.random() } // Randomly select a country code

    val correctCountryName = rememberSaveable { countryNameMap[randomCountryCode] ?: "" } // Get the correct country name for the selected flag

    var isAnswered by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally) {
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

        ExposedDropdownMenuBox(modifier = Modifier.offset(y=150.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                countryNames.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectedItem = countryNames[index]
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }

            Button(
                modifier = Modifier
                    .offset(x = 40.dp, y = 200.dp)
                    .width(200.dp),
                onClick = {
                    if (!isAnswered) {
                        val isCorrect = selectedItem == correctCountryName
                        val resultText = if (isCorrect) "Correct!" else "Wrong! The correct country name is: $correctCountryName"
                        selectedItem = resultText
                        isAnswered = true
                    }
                }
            ) {
                Text(text = if (isAnswered) "Answered" else "Submit")
            }
        }
    }
}







