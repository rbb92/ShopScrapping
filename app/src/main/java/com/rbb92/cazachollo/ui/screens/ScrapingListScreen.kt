package com.rbb92.cazachollo.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rbb92.cazachollo.R
import com.rbb92.cazachollo.data.ScrapListState
import com.rbb92.cazachollo.data.ScrapedItem
import com.rbb92.cazachollo.data.Store
import com.rbb92.cazachollo.notifications.showToast
import com.rbb92.cazachollo.scrapingTool.imageResourceFromStore
import com.rbb92.cazachollo.ui.utils.CustomImageLoader
import com.rbb92.cazachollo.utils.convertirMinutosADiasYMinutos
import com.rbb92.cazachollo.utils.epochToString
import com.rbb92.cazachollo.viewmodel.AppViewModelProvider
import com.rbb92.cazachollo.viewmodel.ScrapListViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingListScreen(
    modifier: Modifier,
    scrapListViewModel: ScrapListViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{
    Log.d("ablancom","IN LIST SCRAP SCREEN!!!")
    when(scrapListViewModel.scrapListState) {
        is ScrapListState.Loading -> LoadingScrapListScreen(modifier = modifier.fillMaxSize())
        is ScrapListState.Empty -> EmptyScrapListScreen(modifier = modifier.fillMaxSize())
        is ScrapListState.Success -> LoadedScrapListScreen(modifier = modifier.fillMaxSize(),scrapListViewModel,
            (scrapListViewModel.scrapListState as ScrapListState.Success).items)

        else -> LoadingScrapListScreen(modifier = modifier.fillMaxSize())
    }

}

@Composable
fun LoadedScrapListScreen(
    modifier: Modifier,
    scrapListViewModel: ScrapListViewModel,
    listItems: List<ScrapedItem>
) {
    val listState = rememberLazyListState()
//    var previous by remember {mutableStateOf(-1)}
//    var accumulate by remember {mutableStateOf(0)}


    scrapListViewModel.getScrapList()

//    Spacer(modifier = Modifier.size(24.dp))
    if(listItems.size==0)
        EmptyScrapListScreen(modifier = modifier)
    LazyColumn(state = listState, modifier = modifier)
    {
        items(listItems.size) {
            ScrapItemCard(
                scrapListViewModel = scrapListViewModel,
                scrapItem = listItems.get(it),
                modifier = Modifier.padding(8.dp))
        }

    }
//    LaunchedEffect(listState.isScrollInProgress) {
//        Log.d("ablancom","paso 0 ${listState.firstVisibleItemScrollOffset}")
//
//        if (listState.firstVisibleItemScrollOffset == previous)
//        {
//            accumulate += 1
//        }
//        else
//        {
//            accumulate = 0
//        }
//        if(accumulate > 2)
//        {
//            accumulate = 0
//            scrapListViewModel.getScrapList()
//        }
//        previous = listState.firstVisibleItemScrollOffset
//        Log.d("ablancom","paso 1 ${accumulate}, ${previous}")
//    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapItemCard(scrapListViewModel: ScrapListViewModel,
                  scrapItem: ScrapedItem,
                  modifier: Modifier)
{
    var expanded by remember { mutableStateOf(false) }
    var isAnimationComplete by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = expanded, label = "VisibilityTransition")
    val alpha by transition.animateFloat(
        label = "VisibilityScaleTransition",
        transitionSpec = {
            if (false isTransitioningTo true) {
                keyframes {
                    durationMillis = 250 // Duración total de la animación
                    1f at 0 // Escala al 80% después de 100 ms
                    0f at 250 // Volver a la escala original después de 300 ms
                }
            } else {
                spring(stiffness = Spring.StiffnessLow)
            }
        }
    ) { visibility ->
        if (visibility) 0f else 1f
    }

    LaunchedEffect(expanded) {
        isAnimationComplete = false
        if (expanded) {
            delay(250) // Esperar a que termine la animación
            isAnimationComplete = true
        }
    }


    Card(
        modifier = Modifier
            .padding(top = 15.dp)
            .border(
                width = if (scrapListViewModel.mainGoal(scrapItem) or scrapListViewModel.secundaryGoal(
                        scrapItem
                    )
                ) 4.dp else 2.dp,
                color = if (scrapListViewModel.mainGoal(scrapItem)) Color.Red else MaterialTheme.colorScheme.secondary, // Color del borde
                shape = MaterialTheme.shapes.small
            ),
        onClick = {expanded = !expanded},
        elevation = CardDefaults.cardElevation(14.dp), // Ajusta la elevación aquí para cambiar la profundidad
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)

                ) {
                    if (!isAnimationComplete) {
                        ImageIcon(scrapItem.src_image, modifier.alpha(alpha))
                        ScrapInformation(
                            scrapItem.title,
                            scrapItem.priceDifference,
                            scrapItem.currentPrice,
                            scrapItem.currency,
                            Store.valueOf(scrapItem.store),
                            modifier.alpha(alpha)
                        )
                    }


                }
                if (expanded and isAnimationComplete) {
                    ScrapInformationExpanded(
                        scrapListViewModel,
                        scrapItem,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 8.dp,
                            bottom = 16.dp,
                            end = 16.dp
                        )
                    )
                }
            }

            if(scrapListViewModel.mainGoal(scrapItem) or scrapListViewModel.secundaryGoal(scrapItem))
            {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = (8).dp) // Ajusta la posición del badge
                ) {
                    // Contenido del badge
                    Badge(modifier = Modifier) {
                        Text(text = "\uD83C\uDF1F")
                    }
                }
            }
        }

    }
}

@Composable
fun ScrapInformationExpanded(scrapListViewModel: ScrapListViewModel,
                             scrapItem: ScrapedItem,
                             modifier: Modifier)
{
    val storeLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    var showRemoveDialog by remember { mutableStateOf(false) }
    val currentContext = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 25.dp),
            text = scrapItem.title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        if(scrapItem.description != "")
        {
            Divider(thickness = 3.dp)
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier.padding(bottom = 25.dp),
                text = scrapItem.description,
                style = MaterialTheme.typography.titleSmall,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(8.dp))
        }

        FullImage(url_img = scrapItem.src_image, modifier = modifier)


        ElevatedCard (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
//            border = CardDefaults.outlinedCardBorder(true),
            elevation = CardDefaults.outlinedCardElevation(defaultElevation = 5.dp)
        ){
            Column(modifier = Modifier.padding(14.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = stringResource(id = R.string.tab_list_scrapping_initial_price),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = "${ scrapItem.initialPrice } ${scrapItem.currency}",
                        style = MaterialTheme.typography.labelLarge
                    )

                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = stringResource(id = R.string.tab_list_scrapping_current_price),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier)
                    Row()
                    {
                        if (scrapItem.priceDifference > 0)
                        {
                            Icon(imageVector = Icons.Filled.ArrowDropDown,contentDescription = null, modifier= Modifier.size(16.dp))
                            Text(
                                text = "${ scrapItem.priceDifference }%",
                                color = colorResource(id = R.color.green_increase),
                                style = MaterialTheme.typography.labelSmall,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            )
                        }

                        if (scrapItem.priceDifference < 0)
                        {
                            Icon(imageVector = Icons.Filled.ArrowDropUp,contentDescription = null, modifier= Modifier.size(16.dp))
                            Text(
                                text = "${ (-1)*scrapItem.priceDifference }%",
                                color = Color.Red,
                                style = MaterialTheme.typography.labelSmall,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            )
                        }


                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = "${ scrapItem.currentPrice } ${scrapItem.currency}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                }
                Divider(modifier = Modifier, thickness = 4.dp)
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp))
                {
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp, top = 6.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = stringResource(id = R.string.tab_list_scrapping_added_at),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp, top = 6.dp),
                        text = epochToString(scrapItem.initialDate),
                        style = MaterialTheme.typography.labelMedium
                    )

                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp))
                {
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = stringResource(id = R.string.tab_list_scrapping_search_period),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = convertirMinutosADiasYMinutos(scrapItem.periodAlert),
                        style = MaterialTheme.typography.labelMedium
                    )

                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp))
                {
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = stringResource(id = R.string.tab_list_scrapping_search_number),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = "${scrapItem.numberSearch}",
                        style = MaterialTheme.typography.labelMedium
                    )

                }
                if(scrapItem.latestSearch != 0L){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 6.dp)
                    )
                    {
                        Text(
                            modifier = Modifier.padding(bottom = 6.dp),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            text = stringResource(id = R.string.tab_list_scrapping_latest_search),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier)
                        Text(
                            modifier = Modifier.padding(bottom = 6.dp),
                            text = epochToString(scrapItem.latestSearch),
                            style = MaterialTheme.typography.labelMedium
                        )

                    }
                }
                Divider(modifier = Modifier, thickness = 4.dp)
                Box(modifier = Modifier.padding(top = 16.dp),)
                {
                    Icon(
                        imageVector = Icons.Filled.WarningAmber,
                        contentDescription = "Icono de advertencia",
//                        tint = Color.Red, // Color del icono de advertencia
                        modifier = Modifier.size(24.dp)
                    )
                    if(scrapItem.isStock)
                    {
                        Text(
                            modifier = Modifier.padding(top = 6.dp, start = 30.dp),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            text = stringResource(id = R.string.tab_list_scrapping_alert_stock),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    else{
                        Text(
                            modifier = Modifier.padding(top = 6.dp, start = 30.dp),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            text = stringResource(id = R.string.tab_list_scrapping_alert_price, scrapItem.limitPrice ,scrapItem.currency),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

            }

        }


        Row(horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)

        ) {
                Button(
                    onClick = {
                        //TODO insertar url con REFERIDO!
                        val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse(scrapItem.url))
                        storeLauncher.launch(intent)
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        modifier = modifier.size(24.dp)
                    )
                }

                Button(onClick = {
                    showRemoveDialog = true
//                    scrapListViewModel.removeWork(scrapItem.uuid)

                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Borrar",
                        modifier = modifier.size(24.dp)
                    )
                }
            if (showRemoveDialog) {
                AlertDialog(
                    onDismissRequest = { showRemoveDialog = false},
                    title = {Text(stringResource(id = R.string.tab_list_scrapping_remove_dialog_title))},
                    text = {Text(stringResource(id = R.string.tab_list_scrapping_remove_dialog_text))},
                    confirmButton = {
                        Button(
                            onClick = {
                                scrapListViewModel.removeWork(scrapItem.uuid)
                                scrapListViewModel.getScrapList()
                                showToast(currentContext, currentContext.getString(R.string.tab_list_scrapping_remove_dialog_removed))
                                showRemoveDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(stringResource(id = R.string.tab_list_scrapping_remove_dialog_yes))
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showRemoveDialog = false
                            }
                        ) {
                            Text(stringResource(id = R.string.tab_list_scrapping_remove_dialog_no))
                        }
                    }
                )
            }

            }


    }
}


@Composable
fun ScrapInformation(title: String, diffPrice: Int, currentPrice: Float, currency: String, store: Store,  modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (currentPrice == 0.0f)
        {
            Text(
                    text = stringResource(id = R.string.tab_list_scrapping_not_available),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
        }
        else
        {
            Row (modifier = modifier, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${currentPrice} ${currency}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(15.dp))
                if (diffPrice > 0)
                {
                    Text(
                        text = "${ diffPrice }%",
                        color = colorResource(id = R.color.green_increase),
                        style = MaterialTheme.typography.labelLarge,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    )
                    Icon(imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier= Modifier
                            .size(24.dp)
                            .padding(top = 8.dp, end = 4.dp),
                        tint = colorResource(id = R.color.green_increase))

                }

                if (diffPrice < 0)
                {
                    Text(
                        text = "${ (-1)*diffPrice }%",
                        color = Color.Red,
                        style = MaterialTheme.typography.labelMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    )
                    Icon(imageVector = Icons.Filled.ArrowDropUp,
                        contentDescription = null,
                        modifier= Modifier
                            .size(24.dp)
                            .padding(top = 8.dp, end = 4.dp),
                        tint = Color.Red)
                }
                Spacer(modifier = Modifier.weight(1f))
                Image(painterResource(imageResourceFromStore(store)),"", modifier = Modifier.size(16.dp) )
            }

        }
    }
}

@Composable
fun ImageIcon(url_img: String, modifier: Modifier) {
    Image(
        modifier = modifier
            .size(68.dp)
            .padding(1.dp)
            .clip(MaterialTheme.shapes.extraSmall),
        contentScale = ContentScale.Crop,
        painter = CustomImageLoader(
             url_img
        ),

        contentDescription = null
    )
}


@Composable
fun FullImage(url_img: String, modifier: Modifier) {
    Row (modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Image(
            modifier = modifier
//                .size(220.dp)
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
//                .wrapContentWidth()
//                .padding(1.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
            painter = CustomImageLoader(
                 url_img
            ),

            contentDescription = null
        )
    }
}
@Composable
fun EmptyScrapListScreen(modifier: Modifier) {
    Column (modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = stringResource(id = R.string.tab_list_scrapping_empty_screen))
    }
}

@Composable
fun LoadingScrapListScreen(modifier: Modifier) {
    Column (modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier= Modifier.size(124.dp))
            Text(text = stringResource(id = R.string.tab_scrapping_searching))
        }
    }
}

@Preview(showSystemUi = true)
@Composable

fun ScrapingListScreenPreview(){

//    ScrapingListScreen(modifier = Modifier,
//        scrapListViewModel = viewModel(factory = AppViewModelProvider.Factory)
//    )
}