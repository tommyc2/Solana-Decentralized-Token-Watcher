package ie.setu.cryptoapp.models

data class Token(var name: String = "",
                 var contractAddress: String = "",
                 var price: Double = 0.0,
                 var priceChange24h: Double = 0.0,
                 var liquidity: Number = 0.0,
                 var marketCap: Double = 0.0,
                 var totalSupply: Double = marketCap / price)