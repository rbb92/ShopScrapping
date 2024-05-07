package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shopscrapping.data.Period
import com.example.shopscrapping.data.ScrapState
import com.example.shopscrapping.data.ScrapWorkDescription
import com.example.shopscrapping.notifications.notificationProductAdded
import com.example.shopscrapping.notifications.showToast
import com.example.shopscrapping.ui.theme.md_theme_light_onPrimary
import com.example.shopscrapping.ui.theme.md_theme_light_onPrimaryContainer
import com.example.shopscrapping.ui.theme.md_theme_light_onSurface
import com.example.shopscrapping.ui.theme.md_theme_light_primary
import com.example.shopscrapping.ui.theme.md_theme_light_tertiaryContainer
import com.example.shopscrapping.utils.currencyToString
import com.example.shopscrapping.utils.priceToFloat
import com.example.shopscrapping.viewmodel.ScrapViewModel
import com.google.accompanist.coil.rememberCoilPainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingDetailScreen(
    scrapUiState: ScrapState,
    scrapViewModel: ScrapViewModel,
    goBack: ()->Unit,
    newProduct: ()->Unit,
    modifier: Modifier
)
{
    val currentProductUiState = scrapViewModel.currentProduct.collectAsState().value

    val timeOptions = listOf("15 min","1 hora", "4 horas", "6 horas", "12 horas", "1 día", "2 días", "3 días", "4 días", "5 días", "6 días", "7 días")
    var priceLimit by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(timeOptions[0]) }
    var inStockOption by remember { mutableStateOf(false) }
    var inAllProducts by remember { mutableStateOf(false) }
    var subProductSelected by remember { mutableStateOf(scrapUiState.product?.subProductSelected)}

    val currentContext = LocalContext.current
    val focusManager = LocalFocusManager.current

    Log.d("ScrappingDetailScreen","Redrawing!")
    Column (verticalArrangement= Arrangement.SpaceBetween,
        modifier = Modifier.pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, false),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            //Product card
            OutlinedCard(modifier = Modifier,
                border = CardDefaults.outlinedCardBorder(true),
                elevation = CardDefaults.outlinedCardElevation(defaultElevation = 25.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        text = currentProductUiState.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
//                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    val imagePainter: Painter = rememberCoilPainter(
                        request = currentProductUiState.src_image_main
//                        request = if(currentProductUiState.src_image_sec != "") currentProductUiState.src_image_sec else currentProductUiState.src_image_main
                    )
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .border(2.dp, md_theme_light_onSurface, RoundedCornerShape(1.dp)),
                        contentScale = ContentScale.Crop,

                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    //TODO si precio 0 o vacio, mostrar no disponible

                    //TODO Descripción en este punto.
                    Text(
                        text = currentProductUiState.description,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
//                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    //START si hay varios subproductos, indicar aquí opciones
                    if(scrapViewModel.haveSubProducts())
                    {

                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(scrapViewModel.numberSubProducts()) { index ->
//                                Spacer(modifier = Modifier.height(20.dp))
                                val imgSubproduct: Painter = rememberCoilPainter(
                                    request = scrapViewModel.subProductImage(index)
                                )

                                SubProductButton(
                                    image = imgSubproduct,
                                    text = scrapViewModel.subProductTitle(index),
                                    onClick = {
                                        scrapViewModel.selectSubProduct(index)
                                        scrapViewModel.updateCurrentProductUI()
                                    },
                                    active = index == scrapViewModel.currentSubProduct()
                                )

                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }


                    OutlinedCard (
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            )
                            {
                                Text(
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    fontStyle = FontStyle.Italic,
                                    text = "Precio",
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Spacer(modifier = Modifier)
                                Text(
                                    text = if(currentProductUiState.price>0.0f) "${currentProductUiState.price} ${currencyToString(currentProductUiState.currency)}"
                                            else "No disponible",
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )

                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            )
                            {
                                if (currentProductUiState.globalMinPrice != null) {
                                    if (currentProductUiState.globalMinPrice > 0.0f) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 16.dp),
                                            fontStyle = FontStyle.Italic,
                                            text = "Precio global",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row()
                                        {

                                            Spacer(modifier = Modifier.size(12.dp))
                                            Text(
                                                text = "${ currentProductUiState.globalMinPrice.toString() } ${currencyToString(currentProductUiState.currency)}",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontStyle = FontStyle.Italic
                                            )
                                        }

                                    }
                                }
                            }
                            Spacer(modifier = Modifier.size(4.dp))
                            Divider(thickness = 2.dp)
                            Spacer(modifier = Modifier.size(4.dp))
                            Box(modifier = Modifier)
                            {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Icono de informacion",
                                    modifier = Modifier.size(24.dp)
                                )
                                    Text(
                                        modifier = Modifier.padding(top = 6.dp, start = 30.dp),
                                        fontStyle = FontStyle.Italic,
                                        text = "Precio global incluye tanto el precio minimo para el producto nuevo como reacondicionado",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            //Scrap options card
            OutlinedCard(modifier = Modifier.fillMaxSize(1f)) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(26.dp))
                    OutlinedTextField(
                        value = priceLimit,
                        onValueChange = { newText ->
                            priceLimit = newText
                        },
                        enabled = !inStockOption,
                        singleLine = true,
                        label = { Text(text = "precio") },
                        supportingText = { Text(text = "Alerta de precio")},
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)


                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(thickness = 2.dp,color = MaterialTheme.colorScheme.background)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row (horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth())
                    {
                        Text(text = "Alerta de stock")
                        Spacer(modifier = Modifier)
                        RadioButton(selected = inStockOption, onClick = { inStockOption = !inStockOption })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(thickness = 2.dp,color = MaterialTheme.colorScheme.background)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row (horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth())
                    {
                        Text(text = "Incluir reacondicionados")
                        Spacer(modifier = Modifier)
                        RadioButton(selected = inAllProducts, onClick = { inAllProducts = !inAllProducts })
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(thickness = 2.dp,color = MaterialTheme.colorScheme.background)
                    Spacer(modifier = Modifier.height(12.dp))


                    ExposedDropdownMenuBox(expanded = expanded,
                        onExpandedChange = {
                            Log.d("ablancom","expanded change: $expanded")
                            expanded = it
                        }
                    ){

                        OutlinedTextField(
                            readOnly = true,
                            value = selectedOption,
                            onValueChange = { },
                            label = { Text("Comprobar producto cada ...") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            modifier = Modifier.menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            timeOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = selectionOption)
                                    },
                                    onClick = {
                                        selectedOption = selectionOption
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(thickness = 2.dp,color = MaterialTheme.colorScheme.background)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row (horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 10.dp))
                    {
                        Button(onClick = {
                            //TODO mover a una funcion

                            val pos_aux = timeOptions.indexOf(selectedOption)

                            val workDescription = ScrapWorkDescription("",
                                scrapUiState.url,
                                scrapUiState.store,
                                scrapUiState.region,
                                inStockOption,
                                inAllProducts,
                                priceLimit.toFloatOrNull()?:0.0f,
                                Period.values()[pos_aux]
                                )

                            scrapViewModel.createNewWork(workDescription)
                            notificationProductAdded(currentContext, currentProductUiState.title)
                            showToast(currentContext, "\uD83E\uDD1E Producto añadido correctamente")
                            newProduct()
                            goBack()
                        },
                            enabled = if (!inStockOption and (priceLimit.toFloatOrNull() == null) ) false else true
                        )
                        {
                            Text(text = "Añadir")
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 36.dp))
                        Button(onClick = goBack)
                        {
                            Text(text = "Cancelar")
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun SubProductButton(image: Painter, text: String, onClick: () -> Unit, active: Boolean) {
    ElevatedButton(
        enabled = !active,
        onClick = onClick,
        modifier = Modifier
            .size(130.dp, 150.dp)
            .padding(10.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
        colors = if(active)
                    ButtonDefaults.buttonColors(containerColor = md_theme_light_tertiaryContainer, contentColor = md_theme_light_onPrimaryContainer)
                 else
                    ButtonDefaults.buttonColors(containerColor = md_theme_light_onPrimary, contentColor = md_theme_light_onPrimaryContainer)
    ) {
        Column {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
//                    .border(2.dp, md_theme_light_onSurface, RoundedCornerShape(1.dp)),
//                contentScale = ContentScale.Crop,

                )
            Spacer(modifier = Modifier.height(1.dp))
            Divider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(1.dp))
            Text(text = trimUntilFirstLetterOrNumber(text),
                style = MaterialTheme.typography.labelSmall )
        }
    }
}

fun trimUntilFirstLetterOrNumber(input: String): String {
    val regex = Regex("[a-zA-Z0-9]")
    val matchResult = regex.find(input)
    return if (matchResult != null) {
        input.substring(matchResult.range.first)
    } else {
        ""
    }
}