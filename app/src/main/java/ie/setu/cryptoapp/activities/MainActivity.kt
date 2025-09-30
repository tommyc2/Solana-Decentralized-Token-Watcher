package ie.setu.cryptoapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ie.setu.cryptoapp.databinding.ActivityMainBinding
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging

class MainActivity : AppCompatActivity() {
    private val logger = KotlinLogging.logger {}
    private lateinit var binding: ActivityMainBinding

    var token = Token()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add token button
        binding.btnAdd.setOnClickListener {
            token.name = binding.tokenName.text.toString()
            token.contractAddress = binding.tokenName.text.toString()

            if (token.name.isNotEmpty() && token.contractAddress.isNotEmpty()) {
                logger.info { "Token added: $token.name" }
                logger.info { "Token added: $token.contractAddress" }
            }
        }
    }
}
