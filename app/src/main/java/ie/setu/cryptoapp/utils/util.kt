package ie.setu.cryptoapp.utils

import android.content.Context
import ie.setu.cryptoapp.models.Token
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import mu.KotlinLogging

object Utility {

    private val logger = KotlinLogging.logger {}
    private val TOKENS_FILE = "tokens.json"
    fun isValidAlias(alias: String): Boolean {
        return alias.isNotEmpty() && alias.length <= 10
    }

    fun writeTokens(context: Context, tokens: ArrayList<Token>) {
        val jsonArray = JSONArray()
        tokens.forEach { token ->
            val obj = JSONObject()
            obj.put("name", token.name)
            obj.put("contractAddress", token.contractAddress)
            obj.put("marketCap", token.marketCap)
            jsonArray.put(obj)
        }

        context.openFileOutput(TOKENS_FILE, Context.MODE_PRIVATE).use {
            it.write(jsonArray.toString().toByteArray())
        }
    }

    fun readTokens(context: Context): ArrayList<Token> {
        val tokens = ArrayList<Token>()
        val content: String = try {
            context.openFileInput(TOKENS_FILE).bufferedReader().use {
                it.readText()
            }
        } catch (_: FileNotFoundException) {
            return tokens
        } catch (_: Exception) {
            return tokens
        }

        try {
            logger.info("Reading tokens from storage: $content")
            val array = JSONArray(content)
            for (i in 0..array.length() - 1) {
                val obj = array.getJSONObject(i)
                val token = Token(
                    name = obj.optString("name", ""),
                    contractAddress = obj.optString("contractAddress", ""),
                    marketCap = obj.optDouble("marketCap", 0.0)
                )
                tokens.add(token)
            }
        } catch (_: Exception) {

        }
        return tokens
    }
}