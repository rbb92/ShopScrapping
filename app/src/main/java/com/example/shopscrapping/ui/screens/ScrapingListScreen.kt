package com.example.shopscrapping.ui.screens

import android.content.Intent
import android.graphics.fonts.FontStyle
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopscrapping.data.ScrapListState
import com.example.shopscrapping.data.ScrapedItem
import com.example.shopscrapping.notifications.showToast
import com.example.shopscrapping.viewmodel.AppViewModelProvider
import com.example.shopscrapping.viewmodel.ScrapListViewModel
import com.google.accompanist.coil.rememberCoilPainter
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
    if(listItems.size==0)
        EmptyScrapListScreen(modifier = modifier)
    LazyColumn(state = listState, modifier = modifier)
    {
        items(listItems.size) {
            ScrapItemCard(
                scrapListViewModel = scrapListViewModel,
                scrapItem = listItems.get(it),
                modifier = Modifier.padding(8.dp))
//            Text(text = "TODO, item ${it.url}, precio ${it.currentPrice}, UUID ${it.uuid}")
        }

    }
    //TODO quitar efecto scroll, no me convence, y poner un boton de actualizar
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
        modifier = modifier,
        onClick = {expanded = !expanded},
        elevation = CardDefaults.cardElevation(8.dp), // Ajusta la elevación aquí para cambiar la profundidad
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
                if (!isAnimationComplete)
                {
                ImageIcon(scrapItem.src_image, modifier.alpha(alpha))
                ScrapInformation(scrapItem.title,scrapItem.initialPrice,scrapItem.currentPrice,scrapItem.isStock,modifier.alpha(alpha))
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
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(bottom = 25.dp),
            text = scrapItem.title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        FullImage(url_img = scrapItem.src_image, modifier = modifier)


        ElevatedCard (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ){
            Column(modifier = Modifier.padding(14.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = "Precio inicial",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = "20.33€",
                        style = MaterialTheme.typography.labelMedium
                    )

                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = "Precio actual",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier)
                    Row()
                    {
                        Icon(imageVector = Icons.Filled.ArrowDropUp,contentDescription = null, modifier= Modifier.size(16.dp))
                        Text(
                            text = "13%",
                            style = MaterialTheme.typography.labelSmall,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = "20.33€",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                }
                Divider(modifier = Modifier, thickness = 2.dp)
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp))
                {
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        text = "Numero de busquedas",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        modifier = Modifier.padding(bottom = 6.dp),
                        text = "123",
                        style = MaterialTheme.typography.labelMedium
                    )

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
                            text = "Avisar cuando producto esté disponible",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    else{
                        Text(
                            modifier = Modifier.padding(top = 6.dp, start = 30.dp),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            text = "Avisar cuando precio sea inferior a ${scrapItem.limitPrice}",
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
                    title = {Text("Borrar producto")},
                    text = {Text("¿Estás seguro de que deseas borrar el producto de la lista?")},
                    confirmButton = {
                        Button(
                            onClick = {
                                scrapListViewModel.removeWork(scrapItem.uuid)
                                scrapListViewModel.getScrapList()
                                showToast(currentContext, "Producto eliminado de la lista \uD83D\uDDD1️")
                                showRemoveDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showRemoveDialog = false
                            }
                        ) {
                            Text("No")
                        }
                    }
                )
            }

            }


    }
}


@Composable
fun ScrapInformation(title: String, initialPrice: Float, currentPrice: Float, stock: Boolean, modifier: Modifier) {
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
                    text = "No disponible",
                    style = MaterialTheme.typography.bodyLarge
                )
        }
        else if(initialPrice == 0.0f)
        {
            Text(
                text = "${currentPrice} €",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        else
        {
            Text(
                text = "${currentPrice} € (${initialPrice} € )",
                style = MaterialTheme.typography.bodyLarge
            )
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
        painter = rememberCoilPainter(
            request = url_img
        ),

        contentDescription = null
    )
}


@Composable
fun FullImage(url_img: String, modifier: Modifier) {
    Row (modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Image(
            modifier = modifier
                .size(220.dp)
                .wrapContentWidth()
                .padding(1.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
            painter = rememberCoilPainter(
                request = url_img
            ),

            contentDescription = null
        )
    }
}
@Composable
fun EmptyScrapListScreen(modifier: Modifier) {
    Column (modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Ups, no has añadido ningún producto todavía \uD83D\uDE1F")
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
            Text(text = "Buscando...")
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