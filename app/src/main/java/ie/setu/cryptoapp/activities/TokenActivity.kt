package ie.setu.cryptoapp.activities

import android.os.Bundle
import android.widget.Toast
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

            if (alias.isEmpty()) {
                Toast.makeText(this, "Please enter a token name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contractAddress.isEmpty()) {
                Toast.makeText(this, "Please enter a contract address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Utility.isValidAlias(alias)) {
                Toast.makeText(this, "Token name must be 10 characters or less", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (alias.isNotEmpty() && contractAddress.isNotEmpty() && Utility.isValidAlias(alias)) {

                // coroutine & API call to get token data
                lifecycleScope.launch {
                    try {
                        val token = API.getTokenData(contractAddress)

                        // Handle invalid token address
                        if (token.length() == 0) {
                            Toast.makeText(this@TokenActivity, "Invalid token address", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val foundToken: Token = convertJSONToTokenObject(token, alias)
                        app.tokens.add(foundToken)
                        // Save tokens to JSON file after adding (internal device storage)
                        try {
                            Utility.writeTokens(applicationContext, app.tokens)
                        } catch (_: Exception) {
                            logger.error("Error occured")
                        }

                        logger.info("Token added: ${app.tokens.get(app.tokens.size-1).name}, ${app.tokens.get(app.tokens.size-1).contractAddress}, ${app.tokens.get(app.tokens.size-1).marketCap}")
                        setResult(RESULT_OK)
                        finish() // finish activity after adding token
                    } catch (e: Exception) {
                        Toast.makeText(this@TokenActivity, "Invalid token address", Toast.LENGTH_SHORT).show()
                        logger.info("Error: invalid token address: $e")
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
