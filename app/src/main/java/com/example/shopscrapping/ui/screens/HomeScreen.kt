package com.example.shopscrapping.ui.screens


import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search

import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.shopscrapping.R
import com.example.shopscrapping.bbdd.PreferencesManager
import com.example.shopscrapping.data.HomeUIState
import com.example.shopscrapping.data.TabsTypes
import com.example.shopscrapping.viewmodel.AppViewModelProvider
import com.example.shopscrapping.viewmodel.HomeViewModel
import com.example.shopscrapping.viewmodel.ScrapViewModel


@Composable
fun ScrappingHomeContent(
    modifier: Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState = homeViewModel.homeUIState.collectAsState().value

    var tabSelection by remember { mutableStateOf( TabsTypes.ScrapScreen) }

    Log.d("ablancom","por HomeScreen")


    val navigationItemContentList = listOf(
        NavigationItemContent(
            type = TabsTypes.ScrapScreen,
            icon = Icons.Default.Search,
            text = stringResource(id = R.string.tab_scrapping)
        ),
        NavigationItemContent(
            type = TabsTypes.ScrapListScreen,
            icon = Icons.Default.List ,
            text = stringResource(id = R.string.tab_list_scrapping),

        ),
        NavigationItemContent(
            type = TabsTypes.Screen3,
            icon = Icons.Default.Settings,
           text = stringResource(id = R.string.tab_list_settings)
        )
    )
//    TODO Migrar a Scaffold? https://developer.android.com/develop/ui/compose/components/scaffold?hl=es-419
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {}
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                when (tabSelection)
                {
                    TabsTypes.ScrapScreen -> ScrapingScreen(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                horizontal = 16.dp
                            ),
                        homeViewModel = homeViewModel
                    )
                    TabsTypes.ScrapListScreen -> ScrapingListScreen( modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            horizontal = 16.dp
                        ))
                    TabsTypes.Screen3 -> SettingsScreen( modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = 16.dp
                        ))
                }

                AnimatedVisibility(
                    visible = true
                ) {
                    BottomNavigationBar(
                        currentTab = tabSelection,
                        onTabPressed = { tabType: TabsTypes ->
                            tabSelection = tabType
                        },
                        navigationItemContentList = navigationItemContentList,
                        homeViewModel = homeViewModel,
                        homeUiState =homeUiState,
                        modifier = Modifier.fillMaxWidth()

                    )
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomNavigationBar(
    currentTab: TabsTypes,
    onTabPressed: ((TabsTypes) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    homeViewModel: HomeViewModel,
    homeUiState: HomeUIState,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {


                NavigationBarItem(
                    selected = false,
//                    selected = currentTab == navItem.type,
                    onClick = { onTabPressed(navItem.type)
                        if(navItem.type == TabsTypes.ScrapListScreen)
                        {
                            homeViewModel.clearBudgeList()
                        }
                    },
                    icon = {

                            if(currentTab == navItem.type)
                            {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = navItem.text,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                            }
                            else
                            {
                                Box(modifier = Modifier) {
                                    Icon(
                                        imageVector = navItem.icon,
                                        contentDescription = navItem.text,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    if(homeUiState.budgeListPoint and (navItem.type == TabsTypes.ScrapListScreen))
                                    {
                                        Badge(modifier = Modifier.padding(start = 28.dp)) {
                                            Text(text = "")
                                        }

                                    }
                                    if(homeUiState.budgeListStar and (navItem.type == TabsTypes.ScrapListScreen))
                                    {
                                        Badge(modifier = Modifier.padding(start = 28.dp)) {
                                            Text(text = "\uD83C\uDF1F")
                                        }

                                    }

                                }
                            }
                    },
                    label = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally){

                            if(currentTab == navItem.type) {
                                Text(navItem.text, color=MaterialTheme.colorScheme.primary)
                                Divider(color=MaterialTheme.colorScheme.primary, thickness = 3.dp)
                            }
                            else
                            {
                                Text(navItem.text)
                            }
                        }

                    },
                    alwaysShowLabel = true,


                )

            }
    }
}


private data class NavigationItemContent(
    val type: TabsTypes,
    val icon: ImageVector,
    val text: String
)


@Composable
@Preview
fun TestHomeGui()
{
    ScrappingHomeContent(
        modifier = Modifier
    )
}