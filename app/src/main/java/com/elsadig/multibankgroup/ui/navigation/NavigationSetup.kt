package com.elsadig.multibankgroup.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elsadig.multibankgroup.ui.screens.details.DetailsScreen
import com.elsadig.multibankgroup.ui.screens.details.DetailsViewModel
import com.elsadig.multibankgroup.ui.screens.feed.FeedScreen
import com.elsadig.multibankgroup.ui.screens.feed.FeedViewModel
import com.elsadig.multibankgroup.ui.theme.MultiBankGroupTheme

@Composable
fun NavigationSetup(modifier: Modifier = Modifier) {
    val parentViewModel: ParentViewModel = hiltViewModel()
    val navController = rememberNavController()
    val parentUiState by parentViewModel.uiState.collectAsStateWithLifecycle()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isDetailsScreen = currentBackStackEntry?.destination?.hasRoute<Routes.DetailsRoute>() == true
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        MultibankGroupTopBar(
            isConnected = parentUiState.connectionState,
            isFeedRunning = parentUiState.isFeedRunning,
            toggleFeed = parentViewModel::toggleFeed,
            showBackButton = isDetailsScreen,
            onClickBack = {
                if (isDetailsScreen)
                    navController.popBackStack()
            }
        )
        NavHost(navController = navController, startDestination = Routes.FeedRoute) {
            composable<Routes.FeedRoute> {
                val feedViewModel: FeedViewModel = hiltViewModel()
                val feedUiState by feedViewModel.uiState.collectAsStateWithLifecycle()
                FeedScreen(
                    uiState = feedUiState,
                    isFeedRunning = parentUiState.isFeedRunning,
                    onClickStock = { stockSymbol ->
                        navController.navigate(
                            Routes.DetailsRoute(
                                stockID = stockSymbol.stockSymbol.stockId,
                                stockLastPrice = stockSymbol.stockSymbol.price.newPrice,
                                priceDirection = stockSymbol.priceDirection
                            )
                        )
                    }
                )
            }

            composable<Routes.DetailsRoute> {
                val detailsViewModel: DetailsViewModel = hiltViewModel()
                val detailUiState by detailsViewModel.uiState.collectAsStateWithLifecycle()
                detailUiState?.let {
                    DetailsScreen(stockUiModel = it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultibankGroupTopBar(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    isFeedRunning: Boolean,
    showBackButton: Boolean,
    toggleFeed: () -> Unit,
    onClickBack: () -> Unit
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Text("MultiBankGroup Stocks Tracker", style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val offsetXOfIconButton by animateDpAsState(
                    targetValue = if (showBackButton) 0.dp else (-45).dp,
                    animationSpec = tween(durationMillis = 600),
                    label = "indicatorMove"
                )
                val offsetXOfConnectionIndicator by animateDpAsState(
                    targetValue = if (showBackButton) 0.dp else (-40).dp,
                    animationSpec = tween(durationMillis = 600),
                    label = "indicatorMove"
                )
                IconButton(
                    onClick = onClickBack,
                    modifier = Modifier.offset {
                        IntOffset(offsetXOfIconButton.roundToPx(), 0)
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                ConnectionIndicator(
                    modifier = Modifier
                        .offset {
                            IntOffset(offsetXOfConnectionIndicator.roundToPx(), 0)
                        }, isConnected = isConnected
                )
            }
        }, actions = {
            Switch(checked = isFeedRunning, onCheckedChange = { toggleFeed() })
        })
}

@Composable
fun ConnectionIndicator(modifier: Modifier = Modifier, isConnected: Boolean = false) {
    val color by animateColorAsState(
        targetValue = if (isConnected) Color.Green else Color.Red,
        animationSpec = tween(),
        label = "connection-indicator"
    )
    Box(
        modifier = modifier
            .size(25.dp)
            .clip(CircleShape)
            .drawBehind { drawRect(color = color) }
    )
}

@Preview(showBackground = true)
@Composable
private fun MultibankGroupTopBarPreview() {
    var isSwitch by remember { mutableStateOf(false) }
    MultiBankGroupTheme {
        Box(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            MultibankGroupTopBar(
                isConnected = false,
                isFeedRunning = isSwitch,
                toggleFeed = { isSwitch = !isSwitch },
                showBackButton = isSwitch
            ) {}
        }
    }
}