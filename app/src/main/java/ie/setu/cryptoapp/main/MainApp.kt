package ie.setu.cryptoapp.main

import android.app.Application
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging
import ie.setu.cryptoapp.utils.Utility

class MainApp : Application() {

    private val logger = KotlinLogging.logger {}

    val tokens = ArrayList<Token>();

    override fun onCreate() {
        super.onCreate()
        logger.info("App started")

        try {
            val tokenData = Utility.readTokens(this)
            tokens.addAll(tokenData)
            logger.info("Loaded ${tokens.size} tokens from storage")
        } catch (_: Exception) {
            logger.info("Error loading tokens from storage")
        }
    }
}