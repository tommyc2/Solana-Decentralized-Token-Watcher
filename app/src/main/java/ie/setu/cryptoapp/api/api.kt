package ie.setu.cryptoapp.api

import ie.setu.cryptoapp.models.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request

object API {

    private val chain = "solana"
    private val URL = "https://api.dexscreener.com/tokens/v1/$chain"
    val client = OkHttpClient()
    val logger = KotlinLogging.logger {  }

    fun solanaTokenExists(ca: String): Boolean {
        return true
    }

    suspend fun getTokenData(ca: String): String = withContext(Dispatchers.IO){
        val request = Request.Builder()
            .url("$URL/$ca")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val response = response.body?.string()
            logger.info("Response: $response")
            return@withContext response as String
        }
    }
}