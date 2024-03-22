package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopscrapping.data.ScrapState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingScreen(
    onButtonPress: (url:String) -> Unit,
    scrapUiState: ScrapState,
    modifier: Modifier
)
{
    var urlText by remember { mutableStateOf("") }
    var isSearchButtonPressed by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Log.d("ablancom","updated...")
    Column (
        modifier = modifier
//            .fillMaxSize()
            .padding(top = 25.dp, start = 16.dp, end = 16.dp)
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isSearchButtonPressed)
        {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = urlText,
                onValueChange = { newText ->
                    urlText = newText
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onButtonPress(urlText) }),
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onButtonPress(urlText)
                    isSearchButtonPressed = true
                },
                modifier = Modifier.
                    fillMaxWidth()

            ) {

                Text("Buscar")
            }
            if(scrapUiState.isError)
            {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Hubo un error")
            }
            Spacer(modifier = Modifier)
        }
        else
        {

            if(scrapUiState.isScrapping)
            {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),

                )
            }
            else
            {
                if(scrapUiState.isError)
                {
                    isSearchButtonPressed = false
                }
                else
                {
                    ScrapingDetailScreen(
                        scrapUiState,
                        modifier = modifier.weight(1f,false)
                    )
                }

            }


        }
    }

}

@Preview(showSystemUi = true)
@Composable

fun ScrapingScreenPreview(){
    ScrapingScreen(
        onButtonPress = { url: String ->
            Log.d("Prueba", "String obtenida " + url)
        },
        ScrapState(),
        modifier = Modifier
    )
}