package com.github.zottaa.mastersleep.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (intent != null) {
            handleIntent(intent)
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
            SCHEDULE_FRAGMENT -> {
                val scheduleFragmentArgs = Bundle().apply {
                    putLong(NEW_ALARM_TIME, intent.getLongExtra(NEW_ALARM_TIME, 0))
                }
                navController.navigate(R.id.alarmClockScheduleFragment, scheduleFragmentArgs)
            }
            DIARY_LIST_FRAGMENT -> navController.navigate(R.id.diaryListFragment)
        }
    }

    companion object {
        private const val NEW_ALARM_TIME = "newAlarmTime"
        private const val INTENT_NAVIGATION_KEY = "intentAction"
        private const val RING_FRAGMENT = "ringFragment"
        private const val SCHEDULE_FRAGMENT = "scheduleFragment"
        private const val DIARY_LIST_FRAGMENT = "diaryListFragment"
    }
}