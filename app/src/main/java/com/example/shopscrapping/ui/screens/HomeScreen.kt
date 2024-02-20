package com.example.shopscrapping.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopscrapping.R
import com.example.shopscrapping.data.TabsTypes
import com.example.shopscrapping.ui.utils.ScreenSize
import com.example.shopscrapping.viewmodel.ScrapUiState
import com.example.shopscrapping.viewmodel.ScrapViewModel


@Composable
fun ScrappingHomeContent(
    scrapUiState: ScrapUiState,
    scrapViewModel: ScrapViewModel,
    modifier: Modifier
) {
    var tabSelection by remember { mutableStateOf( TabsTypes.ScrapScreen) }

    val navigationItemContentList = listOf(
        NavigationItemContent(
            type = TabsTypes.ScrapScreen,
            icon = Icons.Default.Search,
            text = stringResource(id = R.string.tab_scrapping)
        ),
        NavigationItemContent(
            type = TabsTypes.ScrapListScreen,
            icon = Icons.Default.Send,
            text = stringResource(id = R.string.tab_list_scrapping)
        ),
        NavigationItemContent(
            type = TabsTypes.Screen3,
            icon = Icons.Default.Drafts,
//            text = stringResource(id = R.string.tab_drafts)
            text = "TODO"
        )
    )
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
                        scrapUiState = scrapUiState,
                        onButtonPress = { url: String ->
                            Log.d("ablanco","boton pulsado")
                            scrapViewModel.scrapeUrl(url)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                horizontal = 16.dp
                            )
                    )
                    TabsTypes.ScrapListScreen -> Text("TODO 2...", modifier =Modifier
                        .weight(1f)
                        .padding(
                            horizontal = 16.dp
                        ))
                    TabsTypes.Screen3 -> Text("TODO 3...", modifier =Modifier
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
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
    }
}

@Composable
private fun BottomNavigationBar(
    currentTab: TabsTypes,
    onTabPressed: ((TabsTypes) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.type,
                onClick = { onTabPressed(navItem.type) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
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
    val viewModel: ScrapViewModel = viewModel()
    val scrapUiState = viewModel.scrapeState.collectAsState().value


    ScrappingHomeContent(
        scrapUiState = scrapUiState,
        scrapViewModel = viewModel,
        modifier = Modifier
    )
}