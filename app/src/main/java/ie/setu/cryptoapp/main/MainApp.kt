package ie.setu.cryptoapp.main

import android.app.Application
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging

class MainApp : Application() {
    private val logger = KotlinLogging.logger {}

    val tokens = ArrayList<Token>();

    override fun onCreate() {
        super.onCreate()
        logger.info { "Logger started for main " }
        tokens.add(Token("BTC", "fgawhfdwuihfauwhi"))
        tokens.add(Token("ETH", "fgawhfdwuihfauwhi"))
        tokens.add(Token("XCN", "fgawhfdwuihfauwhi"))
    }
}