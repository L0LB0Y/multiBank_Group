package com.elsadig.multibankgroup.ui.screens.feed

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elsadig.multibankgroup.R
import com.elsadig.multibankgroup.domain.entity.PriceUpdate
import com.elsadig.multibankgroup.domain.entity.StockSymbol
import com.elsadig.multibankgroup.ui.model.PriceDirection
import com.elsadig.multibankgroup.ui.model.StockUiModel
import com.elsadig.multibankgroup.ui.theme.MultiBankGroupTheme

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    uiState: FeedUiState,
    isFeedRunning: Boolean,
    onClickStock: (StockUiModel) -> Unit
) {
    Crossfade(targetState = isFeedRunning) { state ->
        when (state) {
            true -> LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(items = uiState.stocks, key = { it.stockSymbol.stockId }) { item ->
                    key(item.stockSymbol.price.newPrice) {
                        StockItem(
                            modifier = Modifier.animateItem(),
                            stockUiModel = item
                        ) {
                            onClickStock(item)
                        }
                    }
                }
            }

            false -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Start the Tracker")
            }
        }
    }
}

@Composable
fun StockItem(modifier: Modifier = Modifier, stockUiModel: StockUiModel, onClick: () -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.currency),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = stockUiModel.stockSymbol.symbol,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stockUiModel.stockSymbol.companyName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "$${stockUiModel.stockSymbol.price.newPrice}".take(7),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            val targetColor = when (stockUiModel.priceDirection) {
                PriceDirection.UP -> Color.Green
                PriceDirection.DOWN -> Color.Red
                PriceDirection.NONE -> Color.Transparent
            }

            val animatedColor by animateColorAsState(
                targetValue = targetColor,
                animationSpec = tween(),
                label = "priceColorAnimation"
            )

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .drawBehind { drawCircle(animatedColor) })

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun StockItemPreview() {
    MultiBankGroupTheme {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            StockItem(
                stockUiModel = StockUiModel(
                    stockSymbol = StockSymbol(
                        1,
                        "AAPL",
                        "Apple Inc.",
                        PriceUpdate(1, 0.0),
                        "Consumer electronics and software giant."
                    ),
                    priceDirection = PriceDirection.UP
                )
            ) {}
        }
    }
}