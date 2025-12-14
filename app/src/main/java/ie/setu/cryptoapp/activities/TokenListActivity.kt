package ie.setu.cryptoapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.cryptoapp.R
import ie.setu.cryptoapp.api.API
import ie.setu.cryptoapp.databinding.ActivityTokenListBinding
import ie.setu.cryptoapp.databinding.CardTokenBinding
import ie.setu.cryptoapp.main.MainApp
import ie.setu.cryptoapp.models.Token
import ie.setu.cryptoapp.utils.Utility
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
class TokenListActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivityTokenListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokenListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TokenAdapter(app.tokens) { position ->

            val launcherIntent = Intent(this, UpdateTokenActivity::class.java)
            launcherIntent.putExtra("token_position", position)
            getUpdateResult.launch(launcherIntent)
        }
        binding.toolbar.title = "Your Token Watchlist"
        setSupportActionBar(binding.toolbar)

        setupBottomNavigation() // Setup bottom navigation
        refreshTokenMarketCaps() // Refresh market caps on startup
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.selectedItemId = R.id.nav_tokens

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tokens -> true
                R.id.nav_settings -> {
                    val intent = Intent(this, UserSettingsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, TokenActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_refresh -> {
                refreshTokenMarketCaps()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshTokenMarketCaps() {
        if (app.tokens.isEmpty()) {
            Toast.makeText(this, "No tokens to refresh. Please add some", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Refreshing token prices...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            for (i in app.tokens.indices) {
                try {
                    val tokenData = API.getTokenData(app.tokens[i].contractAddress)
                    if (tokenData.length() > 0) {
                        val newMarketCap = tokenData.getJSONObject(0).getDouble("fdv")
                        app.tokens[i].marketCap = newMarketCap
                        binding.recyclerView.adapter?.notifyItemChanged(i)
                        logger.info { "Updated market cap for ${app.tokens[i].name}: $newMarketCap" }
                    }
                } catch (e: Exception) {
                    logger.error { "Failed to refresh token ${app.tokens[i].name}: $e" }
                }
            }

            // Save updated market caps
            try {
                Utility.writeTokens(applicationContext, app.tokens)
                logger.info("Tokens saved to internal storage")
            } catch (error: Exception) {
                logger.error("Failed to save tokens: $error")
            }

            Toast.makeText(this@TokenListActivity, "Refreshed items!", Toast.LENGTH_SHORT).show()
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemInserted(app.tokens.size - 1)
            }
        }

    private val getUpdateResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
        }
}

class TokenAdapter constructor(private var tokens: ArrayList<Token>, private val onUpdateClick: (Int) -> Unit ) :
    RecyclerView.Adapter<TokenAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTokenBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val token = tokens[holder.adapterPosition]
        holder.bind(token)
        // Set delete event listener (passing in lambda to function)
        holder.setDeleteListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                tokens.removeAt(pos)
                notifyItemRemoved(pos)
                logger.info { "Token removed at position $pos" }
                logger.info { tokens.toString() }

                try {
                    Utility.writeTokens(holder.itemView.context.applicationContext, tokens)
                }
                catch (_: Exception) {
                    logger.error("Failed to write tokens after deletion")
                }
            }
        }

        holder.setUpdateListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onUpdateClick(pos)
            }
        }
    }

    override fun getItemCount(): Int = tokens.size

    class MainHolder(private val binding : CardTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(token: Token) {
            binding.tokenName.text = token.name
            binding.contractAddress.text = token.contractAddress
            binding.marketCap.text = token.marketCap.toString()
        }

        fun setDeleteListener(onDelete: () -> Unit) {
            binding.deleteButton.setOnClickListener {
                onDelete()
            }
        }

        fun setUpdateListener(onUpdate: () -> Unit) {
            binding.updateButton.setOnClickListener {
                onUpdate()
            }
        }
    }
}
