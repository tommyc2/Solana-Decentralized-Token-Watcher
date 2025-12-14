package ie.setu.cryptoapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ie.setu.cryptoapp.databinding.ActivityUpdateTokenBinding
import ie.setu.cryptoapp.main.MainApp
import ie.setu.cryptoapp.utils.Utility
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class UpdateTokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTokenBinding
    lateinit var app: MainApp
    private var tokenPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        tokenPosition = intent.getIntExtra("token_position", -1) // setting -1 here in case there is an error

        if (tokenPosition != -1 && tokenPosition < app.tokens.size) {

            val token = app.tokens[tokenPosition]

            binding.tokenName.setText(token.name)
            binding.contractAddress.setText(token.contractAddress)
            binding.marketCap.setText(Utility.formatMarketCap(token.marketCap))
        }

        binding.toolbarUpdate.title = "Update Token"
        setSupportActionBar(binding.toolbarUpdate)

        // Save button
        binding.btnSave.setOnClickListener {
            val newName = binding.tokenName.text.toString()

            if (newName.isNotEmpty() && tokenPosition != -1) {

                app.tokens[tokenPosition].name = newName

                // Update token in internal storage
                try {
                    Utility.writeTokens(applicationContext, app.tokens)
                    logger.info("Token updated: ${app.tokens[tokenPosition].name}")
                } catch (e: Exception) {
                    logger.error("Error saving updated token: $e")
                }

                setResult(RESULT_OK)
                finish()
            }
        }

        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
