package com.rbb92.cazachollo.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rbb92.cazachollo.R
import com.rbb92.cazachollo.data.CountriesCode
import com.rbb92.cazachollo.data.Store
import com.rbb92.cazachollo.data.getDrawableFromCountryCode
import com.rbb92.cazachollo.scrapingTool.canSetRegion
import com.rbb92.cazachollo.scrapingTool.imageResourceFromStore
import com.rbb92.cazachollo.ui.theme.md_theme_light_primary
import com.rbb92.cazachollo.viewmodel.AppViewModelProvider
import com.rbb92.cazachollo.viewmodel.HomeViewModel
import com.rbb92.cazachollo.viewmodel.ScrapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingScreen(
    modifier: Modifier,
    scrapViewModel: ScrapViewModel = viewModel(factory = AppViewModelProvider.Factory),
    homeViewModel: HomeViewModel,
    initialUrl: String,
    activity: Activity
)
{
    val scrapUiState = scrapViewModel.scrapeState.collectAsState().value

    val onButtonPress = { url: String, store:Store, country:CountriesCode ->
        Log.d("ablanco","boton pulsado")
        scrapViewModel.scrapeUrl(url, store, country)
    }

    var urlText by remember { mutableStateOf(initialUrl) }

    var currentStore by remember {
        if(urlText == "")
            mutableStateOf(Store.NULL)
        else
            mutableStateOf(scrapViewModel.detectStoreFromURL(urlText))
    }

    var currentCountry by remember { mutableStateOf(CountriesCode.ES) }
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
//                    textColor = Color.Black,
//                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large,
                placeholder = {
                    Text(
                        text = stringResource(R.string.tab_scrapping_field_placeholder),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                },
                onValueChange = { newText ->
                    urlText = newText
                    currentStore = if(scrapViewModel.detectStoreFromURL(urlText) != Store.NULL)
                                    scrapViewModel.detectStoreFromURL(urlText)
                                   else
                                       currentStore
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (currentStore != Store.NULL) onButtonPress(urlText,currentStore,currentCountry) }),
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                )

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    onButtonPress(urlText,currentStore,currentCountry)

                    scrapViewModel.inScrapingState()
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(48.dp)

                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                enabled = currentStore != Store.NULL
            )

            Spacer(modifier = Modifier.height(44.dp))

            StoreMenu({ currentStore = it},currentStore, modifier)
//            Spacer(modifier = Modifier.height(14.dp))
//
//            StoreDropdownMenu({ currentStore = it},currentStore)
            Spacer(modifier = Modifier.height(24.dp))

            if(canSetRegion(currentStore))
                CountryMenu({ currentCountry = it},currentCountry, modifier)

            if(scrapUiState.isError)
            {
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.tab_scrapping_search_error))
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
                    Text(text = stringResource(R.string.tab_scrapping_searching))
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
                        activity = activity,
                        modifier = modifier.weight(1f,false)
                    )
                }

            }


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMenu(updateStore: (Store)->Unit, currentStore: Store, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
//    var currentStore by remember { mutableStateOf(Store.NULL) }

    val storeItems = Store.values().filter {
        it != Store.NULL
    }

    Column {

        Row(modifier = Modifier
            .clickable { expanded = true }
            .fillMaxWidth(0.7f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (currentStore != Store.NULL) "${currentStore.name}  " else stringResource(R.string.tab_scrapping_choose_store),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
//                    .align(Alignment.CenterStart),
//                style = MaterialTheme.typography.bodyMedium,
            )
            if (currentStore != Store.NULL)
                Image(painterResource(imageResourceFromStore(currentStore)),"", modifier=Modifier.size(18.dp))
            IconButton(
                onClick = { expanded = true },
//                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            storeItems.forEach { store ->
                DropdownMenuItem(onClick = {
                    updateStore(store)
                    expanded = false
                            },
                    text = {
                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(1f))
                        {
                            Image(painterResource(imageResourceFromStore(store)),"", modifier = Modifier.size(18.dp) )
//                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "    ${store.name}")
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun CountryMenu(updateStore: (CountriesCode)->Unit, currentCountry: CountriesCode, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
//    var currentStore by remember { mutableStateOf(Store.NULL) }

    val storeItems = CountriesCode.values()

    Column {

        Row(modifier = Modifier
            .clickable { expanded = true }
            .fillMaxWidth(0.4f)
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currentCountry.name ,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
//                    .align(Alignment.CenterStart),
//                style = MaterialTheme.typography.bodyMedium,
            )
            Image(painterResource(getDrawableFromCountryCode(currentCountry)),"")
            IconButton(
                onClick = { expanded = true },
//                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier,
        ) {
            storeItems.forEach { country ->
                DropdownMenuItem(onClick = {
                    updateStore(country)
                    expanded = false
                },
                    text = {
                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = country.name)
                                Spacer(modifier = Modifier.weight(1f))
                                Image(painterResource(getDrawableFromCountryCode(country)),"")}
                            }
                            )

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