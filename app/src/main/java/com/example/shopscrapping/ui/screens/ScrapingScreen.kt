package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.ui.theme.md_theme_light_primary
import com.example.shopscrapping.ui.theme.md_theme_light_primaryContainer
import com.example.shopscrapping.viewmodel.AppViewModelProvider
import com.example.shopscrapping.viewmodel.HomeViewModel
import com.example.shopscrapping.viewmodel.ScrapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingScreen(
    modifier: Modifier,
    scrapViewModel: ScrapViewModel = viewModel(factory = AppViewModelProvider.Factory),
    homeViewModel: HomeViewModel
)
{
    val scrapUiState = scrapViewModel.scrapeState.collectAsState().value

    val onButtonPress = { url: String ->
        Log.d("ablanco","boton pulsado")
        scrapViewModel.scrapeUrl(url)
    }

    var urlText by remember { mutableStateOf("") }
//    var isScrappingProcess by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Log.d("ablancom","updated...")
    Log.d("ablancom", "ScrapeState: ${scrapUiState}")
    Column (
        modifier = modifier
//            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!scrapUiState.isScrappingProcess)
        {
            TextField(
                value = urlText,
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                placeholder = {
                    Text(
                        text = "Introduce la url...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                },
                onValueChange = { newText ->
                    urlText = newText
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onButtonPress(urlText) }),
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                )

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    onButtonPress(urlText)

                    scrapViewModel.inScrapingState()
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar",
                        modifier = Modifier.size(48.dp)

                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp),
            )
            if(scrapUiState.isError)
            {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Hubo un error")
            }
        }
        else
        {

            if(scrapUiState.isScrapping)
            {
                //TODO, centrar y mejorar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier= Modifier.size(124.dp))
                    Text(text = "Buscando...")
                }


            }
            else
            {
                if(scrapUiState.isError)
                {
                    scrapViewModel.outScrapingState()
                }
                else
                {
                    ScrapingDetailScreen(
                        scrapUiState,
                        scrapViewModel,
                        {
                            scrapViewModel.clearScrapeUIState()
                            scrapViewModel.outScrapingState()
                            urlText=""


                        },
                        { homeViewModel.productAdded() },
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
//    ScrapingScreen(
//        modifier = Modifier
//    )
}