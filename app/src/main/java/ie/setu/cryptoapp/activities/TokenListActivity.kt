package ie.setu.cryptoapp.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.cryptoapp.R
import ie.setu.cryptoapp.databinding.ActivityMainBinding
import ie.setu.cryptoapp.databinding.ActivityTokenListBinding
import ie.setu.cryptoapp.databinding.CardTokenBinding
import ie.setu.cryptoapp.main.MainApp
import ie.setu.cryptoapp.models.Token

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

        binding.toolbar.title = "My Watchlist"
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

}

class TokenAdapter constructor(private var tokens: List<Token>) :
    RecyclerView.Adapter<TokenAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTokenBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val token = tokens[holder.adapterPosition]
        holder.bind(token)
    }

    override fun getItemCount(): Int = tokens.size

    class MainHolder(private val binding : CardTokenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(token: Token) {
            binding.tokenName.text = token.name
            binding.contractAddress.text = token.contractAddress
        }
    }
}
