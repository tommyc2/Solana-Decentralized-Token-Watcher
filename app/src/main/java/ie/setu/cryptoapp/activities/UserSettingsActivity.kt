package ie.setu.cryptoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ie.setu.cryptoapp.R
import ie.setu.cryptoapp.databinding.ActivityUserSettingsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.settings_title)
        setSupportActionBar(binding.toolbar)

        setupNightModeSwitch()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        updateCurrentDate()
    }

    private fun setupNightModeSwitch() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isNightMode = prefs.getBoolean("night_mode", false)
        binding.nightModeSwitch.isChecked = isNightMode

        binding.nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("night_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun updateCurrentDate() {
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        binding.currentDateText.text = dateFormat.format(Date())
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.selectedItemId = R.id.nav_settings

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tokens -> {
                    val intent = Intent(this, TokenListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_settings -> true
                else -> false
            }
        }
    }
}

