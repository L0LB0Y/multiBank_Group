package com.elsadig.multibankgroup.domain.repository

import com.elsadig.multibankgroup.domain.entity.PriceUpdate
import com.elsadig.multibankgroup.domain.entity.StockSymbol
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IStockRepository {
    // mock the stockList
    val stockList: List<StockSymbol>
        get() = listOf(
            StockSymbol(
                1,
                "AAPL",
                "Apple Inc.",
                PriceUpdate(1, 0.0),
                "Consumer electronics and software giant."
            ),
            StockSymbol(
                2,
                "GOOG",
                "Alphabet Inc.",
                PriceUpdate(2, 0.0),
                "Parent company of Google and YouTube."
            ),
            StockSymbol(
                3,
                "TSLA",
                "Tesla Inc.",
                PriceUpdate(3, 0.0),
                "Electric vehicles and clean energy."
            ),
            StockSymbol(
                4,
                "AMZN",
                "Amazon.com",
                PriceUpdate(4, 0.0),
                "E-commerce and cloud computing leader."
            ),
            StockSymbol(
                5,
                "MSFT",
                "Microsoft Corp.",
                PriceUpdate(5, 0.0),
                "Enterprise software and Azure cloud."
            ),
            StockSymbol(
                6,
                "NVDA",
                "NVIDIA Corp.",
                PriceUpdate(6, 0.0),
                "GPUs and AI accelerator chips."
            ),
            StockSymbol(
                7,
                "META",
                "Meta Platforms",
                PriceUpdate(7, 0.0),
                "Social media and VR hardware."
            ),
            StockSymbol(
                8,
                "NFLX",
                "Netflix Inc.",
                PriceUpdate(8, 0.0),
                "Streaming entertainment platform."
            ),
            StockSymbol(
                9,
                "AMD",
                "Advanced Micro Devices",
                PriceUpdate(9, 0.0),
                "CPUs and GPUs for PCs and servers."
            ),
            StockSymbol(
                10,
                "INTC",
                "Intel Corp.",
                PriceUpdate(10, 0.0),
                "Semiconductor and processor manufacturer."
            ),
            StockSymbol(
                11,
                "DIS",
                "The Walt Disney Co.",
                PriceUpdate(11, 0.0),
                "Media, entertainment and theme parks."
            ),
            StockSymbol(
                12,
                "BABA",
                "Alibaba Group",
                PriceUpdate(12, 0.0),
                "Chinese e-commerce and cloud services."
            ),
            StockSymbol(
                13,
                "UBER",
                "Uber Technologies",
                PriceUpdate(13, 0.0),
                "Ride-hailing and food delivery."
            ),
            StockSymbol(
                14,
                "SPOT",
                "Spotify Technology",
                PriceUpdate(14, 0.0),
                "Music and podcast streaming."
            ),
            StockSymbol(
                15,
                "PYPL",
                "PayPal Holdings",
                PriceUpdate(15, 0.0),
                "Digital payments and fintech."
            ),
            StockSymbol(
                16,
                "SNAP",
                "Snap Inc.",
                PriceUpdate(16, 0.0),
                "Multimedia messaging and AR."
            ),
            StockSymbol(
                17,
                "SHOP",
                "Shopify Inc.",
                PriceUpdate(17, 0.0),
                "E-commerce platform for merchants."
            ),
            StockSymbol(
                18,
                "SQ",
                "Block Inc.",
                PriceUpdate(18, 0.0),
                "Payments, Bitcoin and fintech tools."
            ),
            StockSymbol(
                19,
                "COIN",
                "Coinbase Global",
                PriceUpdate(19, 0.0),
                "Cryptocurrency exchange platform."
            ),
            StockSymbol(
                20,
                "PLTR",
                "Palantir Technologies",
                PriceUpdate(20, 0.0),
                "Big data analytics and AI."
            ),
            StockSymbol(
                21,
                "RBLX",
                "Roblox Corp.",
                PriceUpdate(21, 0.0),
                "Online gaming and metaverse platform."
            ),
            StockSymbol(
                22,
                "HOOD",
                "Robinhood Markets",
                PriceUpdate(22, 0.0),
                "Commission-free trading app."
            ),
            StockSymbol(
                23,
                "LYFT",
                "Lyft Inc.",
                PriceUpdate(23, 0.0),
                "Ride-sharing service in North America."
            ),
            StockSymbol(
                24,
                "ZM",
                "Zoom Video Comm.",
                PriceUpdate(24, 0.0),
                "Video conferencing and collaboration."
            ),
            StockSymbol(
                25,
                "TWLO",
                "Twilio Inc.",
                PriceUpdate(25, 0.0),
                "Cloud communications and messaging APIs."
            )
        )
    val priceUpdates: SharedFlow<PriceUpdate>
    val connectionState: StateFlow<Boolean>
    fun connect()
    fun disconnect()
    fun sendPriceUpdate(priceUpdate: PriceUpdate)
}