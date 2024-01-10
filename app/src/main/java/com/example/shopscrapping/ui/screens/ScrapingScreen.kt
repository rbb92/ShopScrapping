package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopscrapping.viewmodel.ScrapUiState
import com.google.accompanist.coil.rememberCoilPainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingScreen(
    onButtonPress: (url:String) -> Unit,
    scrapUiState: ScrapUiState,
    modifier: Modifier
)
{
    var urlText by remember { mutableStateOf("") }
    var isButtonPressed by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(top = 25.dp, start = 16.dp, end =16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = urlText,
            onValueChange = { newText ->
                urlText = newText
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onButtonPress(urlText) }),

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onButtonPress(urlText)
                isButtonPressed=true},
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Enviar")
        }
        if (isButtonPressed){
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = scrapUiState.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            val imagePainter: Painter = rememberCoilPainter(
                request = scrapUiState.src_image
            )
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    ,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = scrapUiState.price,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(26.dp))

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
        ScrapUiState(),
        modifier = Modifier
    )
}