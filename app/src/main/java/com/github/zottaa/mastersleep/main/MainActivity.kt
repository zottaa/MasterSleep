package com.github.zottaa.mastersleep.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.databinding.ActivityMainBinding
import com.github.zottaa.mastersleep.settings.Themes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MasterSleep)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (intent != null) {
            handleIntent(intent)
        }
        observeViewModel()
        viewModel.init()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.language.collect {
                        val appLocale = LocaleListCompat.forLanguageTags(it.languageTag)
                        AppCompatDelegate.setApplicationLocales(appLocale)
                        Locale.setDefault(Locale(it.languageTag))
                    }
                }
                launch {
                    viewModel.theme.collect {
                        when (it) {
                            Themes.LIGHT -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                            Themes.DARK -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                            else -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        when (intent.getStringExtra(INTENT_NAVIGATION_KEY)) {
            RING_FRAGMENT -> navController.navigate(R.id.alarmClockRingFragment)
        }
    }

    companion object {
        private const val INTENT_NAVIGATION_KEY = "intentAction"
        private const val RING_FRAGMENT = "ringFragment"
    }
}