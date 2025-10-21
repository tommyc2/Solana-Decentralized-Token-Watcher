package ie.setu.cryptoapp.api

import android.widget.Toast
import ie.setu.cryptoapp.models.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object API {

    private val chain = "solana"
    private val URL = "https://api.dexscreener.com/tokens/v1/$chain"
    val client = OkHttpClient()
    val logger = KotlinLogging.logger {  }

    suspend fun solanaTokenExists(ca: String): Boolean = withContext(Dispatchers.IO) {

        // pulled from okhttp docs (boilerplate code)

        val request = Request.Builder()
            .url("$URL/$ca")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.code == 404) {
                logger.info { "Token not found: $ca" }
                return@withContext false
            }

            if (!response.isSuccessful) {
                logger.warn { "Unexpected response code: ${response.code}" }
                return@withContext false
            }

            return@withContext true
        }
    }

    suspend fun getTokenData(ca: String): JSONArray = withContext(Dispatchers.IO){
        val request = Request.Builder()
            .url("$URL/$ca")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                logger.warn { "Unexpected response code: ${response.code}" }
            }

            if (response.code == 404) {
                logger.warn { "Token address does not exist: $ca" }
                return@withContext JSONArray()
            }

            val responseBody = response.body?.string()
            logger.info { "Fetched token JSON: $responseBody" }
            return@withContext JSONArray(responseBody)
        }
    }
}