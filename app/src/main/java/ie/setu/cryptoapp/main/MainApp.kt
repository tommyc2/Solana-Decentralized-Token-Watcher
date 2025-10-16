package ie.setu.cryptoapp.main

import android.app.Application
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging

class MainApp : Application() {

    val tokens = ArrayList<Token>();

    override fun onCreate() {
        super.onCreate()
        tokens.add(Token("BTC", "fgawhfdwuihfauwhi"))
        tokens.add(Token("ETH", "fgawhfdwuihfauwhi"))
        tokens.add(Token("XCN", "fgawhfdwuihfauwhi"))
    }
}