package com.example.sidebar

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val sidebarViewModel: SidebarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request battery optimization exemption
        requestIgnoreBatteryOptimizations()

        // Initialize the sidebar service through ViewModel
        lifecycleScope.launch {
            sidebarViewModel.initializeSidebarService()
        }

        // Start the sidebar worker
        startSidebarWorker()
    }

    private fun requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = packageName
            val powerManager = getSystemService(POWER_SERVICE) as PowerManager

            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }

    private fun startSidebarWorker() {
        val workRequest = OneTimeWorkRequestBuilder<SidebarWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}