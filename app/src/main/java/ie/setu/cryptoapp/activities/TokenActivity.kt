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
            val name = binding.tokenName.text.toString()
            val contractAddress = binding.contractAddress.text.toString()

            if (name.isNotEmpty() && contractAddress.isNotEmpty()) {
                app.tokens.add(Token(name, contractAddress).copy())

                lifecycleScope.launch {
                    try {
                        val token = API.getTokenData("CzFvsLdUazabdiu9TYXujj4EY495fG7VgJJ3vQs6bonk")
                        logger.info("Fetched Token: $token")
                    } catch (e: Exception) {
                        logger.info("Error: $e")
                    }


                    // return to list activity
                    setResult(RESULT_OK)
                    finish() // finish activity after adding token
                }
            }
        }
    }
}
