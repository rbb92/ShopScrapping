package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopscrapping.viewmodel.AppViewModelProvider
import com.example.shopscrapping.viewmodel.ScrapListViewModel
import com.example.shopscrapping.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{
    val settingsUIState by settingsViewModel.settingsUIState.collectAsState()

    val prueba:(Boolean) -> Unit ={ isChecked -> Log.d("ablancom","nuevo valor de boolean1: ${isChecked}")}
    Column (modifier = modifier) {
        Spacer(modifier = Modifier.size(18.dp))
        SettingRow(name="Solo Wifi",
                   checked = settingsUIState.isWifiOnly,
                   action = {disabled -> settingsViewModel.updateIsWifi(disabled)})
        Spacer(modifier = Modifier.size(9.dp))
        Divider(thickness = 4.dp)
        Spacer(modifier = Modifier.size(9.dp))
        SettingRow(name="Deshabilitar notificaciones secundarias",
                   checked = settingsUIState.isSecundaryNotificationsDisabled,
                   action = {disabled -> settingsViewModel.updateisSecundaryNotificationsDisabled(disabled)})
        Spacer(modifier = Modifier.size(9.dp))
        Divider(thickness = 4.dp)
        Spacer(modifier = Modifier.size(9.dp))
    }
}

@Composable
fun SettingRow(modifier: Modifier = Modifier,
               name: String,
               checked: Boolean,
               action: (Boolean)->Unit)
{
    Row (modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(name)
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = action)

    }
}
