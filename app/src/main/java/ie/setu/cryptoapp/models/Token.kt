package ie.setu.cryptoapp.models

data class Token(val name: String,
                 val contractAddress: String,
                 val iconUrl: String,
                 val price: Double,
                 val priceChange24h: Double,
                 val liquidity: Double,
                 val marketCap: Double,
                 val totalSupply: Double)