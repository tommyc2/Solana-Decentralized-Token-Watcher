package ie.setu.cryptoapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ie.setu.cryptoapp.api.API
import ie.setu.cryptoapp.api.API.logger
import ie.setu.cryptoapp.databinding.ActivityMainBinding
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging
import ie.setu.cryptoapp.main.MainApp
import ie.setu.cryptoapp.utils.Utility
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class TokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        // Add token button
        binding.btnAdd.setOnClickListener {
            val alias = binding.tokenName.text.toString()
            val contractAddress = binding.contractAddress.text.toString()

            if (alias.isNotEmpty() && contractAddress.isNotEmpty() && Utility.isValidAlias(alias)) {
                lifecycleScope.launch {
                    try {
                        val token = API.getTokenData(contractAddress)
                        if (token.toString() == JSONArray().toString()) {
                            logger.info("Token not found: 404 error")
                        }
                        val foundToken: Token = convertJSONToTokenObject(token, alias)
                        app.tokens.add(foundToken)

                        logger.info("Token added: ${app.tokens.get(app.tokens.size-1).name}, ${app.tokens.get(app.tokens.size-1).contractAddress}, ${app.tokens.get(app.tokens.size-1).marketCap}")
                        setResult(RESULT_OK)
                        finish() // finish activity after adding token
                    } catch (e: Exception) {
                        logger.info("Error: $e")
                    }
                }
            }

        }
    }

    fun convertJSONToTokenObject(token: JSONArray, alias: String): Token {
        val name = token.getJSONObject(0).getJSONObject("baseToken").getString("symbol")
        val contractAddress = token.getJSONObject(0).getJSONObject("baseToken").getString("address")
        val marketCap = token.getJSONObject(0).getDouble("fdv")
        return Token("$name / $alias", contractAddress, marketCap)
    }
}
