package ie.setu.cryptoapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ie.setu.cryptoapp.databinding.ActivityMainBinding
import ie.setu.cryptoapp.models.Token
import mu.KotlinLogging
import ie.setu.cryptoapp.main.MainApp

class TokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val logger = KotlinLogging.logger {}

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
                logger.info { "Token added: $name" }
                logger.info { "Token added: $contractAddress" }
                logger.info { "Token obj: ${app.tokens}"}

                setResult(RESULT_OK)
                finish() // finish activity after adding token
            }
        }
    }
}
