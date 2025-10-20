package ie.setu.cryptoapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.cryptoapp.R
import ie.setu.cryptoapp.databinding.ActivityTokenListBinding
import ie.setu.cryptoapp.databinding.CardTokenBinding
import ie.setu.cryptoapp.main.MainApp
import ie.setu.cryptoapp.models.Token
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
        binding.recyclerView.adapter = TokenAdapter(app.tokens)

        binding.toolbar.title = "My Token Watchlist"
        setSupportActionBar(binding.toolbar)
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
                // todo: refresh token data on UI

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemInserted(app.tokens.size - 1)
            }
        }
}

class TokenAdapter constructor(private var tokens: MutableList<Token>) :
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
    }
}
