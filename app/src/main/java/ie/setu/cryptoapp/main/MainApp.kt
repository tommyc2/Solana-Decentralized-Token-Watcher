package ie.setu.cryptoapp.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging
import ie.setu.cryptoapp.utils.Utility

class MainApp : Application() {

    private val logger = KotlinLogging.logger {}

    val tokens = ArrayList<Token>();

    override fun onCreate() {
        super.onCreate()
        logger.info("App started")

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isNightMode = prefs.getBoolean("night_mode", false)
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        try {
            val tokenData = Utility.readTokens(this)
            tokens.addAll(tokenData)
            logger.info("Loaded ${tokens.size} tokens from storage")
        } catch (_: Exception) {
            logger.info("Error loading tokens from storage")
        }
    }
}