package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shopscrapping.data.ScrapState
import com.google.accompanist.coil.rememberCoilPainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapingDetailScreen(
    scrapUiState: ScrapState,
    modifier: Modifier
)
{
    val timeOptions = listOf("15 min","1 hora", "4 horas", "6 horas", "12 horas", "1 día", "2 días", "3 días", "4 días", "5 días", "6 días", "7 días")
    var priceLimit by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(timeOptions[0]) }
    var inStockOption by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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
                        text = scrapUiState.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    val imagePainter: Painter = rememberCoilPainter(
                        request = scrapUiState.src_image
                    )
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
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
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxSize(1f)) {
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Row (horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp))
                    {
                        Text(text = "Alerta de stock")
                        Spacer(modifier = Modifier)
                        RadioButton(selected = inStockOption, onClick = { inStockOption = !inStockOption })
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
                        Button(onClick = { /*TODO*/ },) {
                            Text(text = "TODO")
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 36.dp))
                        Button(onClick = { /*TODO*/ },) {
                            Text(text = "TODO2")
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}