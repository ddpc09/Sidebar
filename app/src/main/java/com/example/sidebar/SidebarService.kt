//package com.example.sidebar
//
////import android.app.NotificationChannel
////import android.app.NotificationManager
////import android.app.Service
////import android.content.Intent
////import android.graphics.PixelFormat
////import android.os.Build
////import android.os.IBinder
////import android.view.*
////import android.widget.Button
////import android.widget.ImageButton
////import android.widget.Toast
////import androidx.core.app.NotificationCompat
////
////class SidebarService : Service() {
////
////    private lateinit var windowManager: WindowManager
////    private lateinit var sidebarView: View
////
////    override fun onCreate() {
////        super.onCreate()
////
////        // Create the notification channel (for Android 8.0+)
////        createNotificationChannel()
////
////        // Start the service in the foreground
////        startForegroundService()
////
////        // Inflate the sidebar layout
////        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
////        sidebarView = inflater.inflate(R.layout.sidebar_layout, null)
////
////        // Define the layout parameters for the sidebar
////        val layoutParams = WindowManager.LayoutParams(
////            WindowManager.LayoutParams.WRAP_CONTENT,
////            WindowManager.LayoutParams.MATCH_PARENT,
////            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Overlay type
////            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
////                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
////                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
////            PixelFormat.TRANSLUCENT
////        )
////        layoutParams.gravity = Gravity.START // Sidebar appears on the left
////
////        // Add the sidebar to the window
////        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
////        windowManager.addView(sidebarView, layoutParams)
////
////        // Set up button interaction
////        val homeButton: Button = sidebarView.findViewById(R.id.home_button)
////        homeButton.setOnClickListener {
////            Toast.makeText(this, "Home button clicked", Toast.LENGTH_SHORT).show()
////        }
////    }
////
////    private fun createNotificationChannel() {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            val channel = NotificationChannel(
////                "sidebar_channel",
////                "Sidebar Service",
////                NotificationManager.IMPORTANCE_LOW
////            )
////            val manager = getSystemService(NotificationManager::class.java)
////            manager?.createNotificationChannel(channel)
////        }
////    }
////
////    private fun startForegroundService() {
////        val notification = NotificationCompat.Builder(this, "sidebar_channel")
////            .setContentTitle("Sidebar Service")
////            .setContentText("Sidebar is running")
//////            .setSmallIcon(R.drawable.ic_sidebar) // Replace with your icon
////            .setPriority(NotificationCompat.PRIORITY_LOW) // Low priority for minimal intrusion
////            .build()
////
////        startForeground(1, notification)
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////        // Remove the sidebar view when service is destroyed
////        windowManager.removeView(sidebarView)
////        stopForeground(true)
////    }
////
////    override fun onBind(intent: Intent?): IBinder? {
////        return null // Not a bound service
////    }
////}
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.Service
//import android.content.Intent
//import android.graphics.PixelFormat
//import android.os.Build
//import android.os.Handler
//import android.os.IBinder
//import android.os.Looper
//import android.view.*
//import android.widget.Button
//import android.widget.ImageButton
//import android.widget.Toast
//import androidx.core.app.NotificationCompat
//
//class SidebarService : Service() {
//
//    private lateinit var windowManager: WindowManager
//    private lateinit var sidebarView: View
//    private var isSidebarVisible = true
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Create notification channel for the foreground service
//        createNotificationChannel()
//
//        // Start the service in the foreground
//        startForegroundService()
//
//        // Delay sidebar initialization to ensure smoother performance
//        Handler(Looper.getMainLooper()).postDelayed({
//            inflateSidebar()
//        }, 100)
//    }
//
//    private fun inflateSidebar() {
//        // Inflate and add the sidebar layout
//        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        sidebarView = inflater.inflate(R.layout.sidebar_layout, null)
//
//        val layoutParams = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//            PixelFormat.TRANSLUCENT
//        )
//        layoutParams.gravity = Gravity.START
//
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//        windowManager.addView(sidebarView, layoutParams)
//
//        // Example button in the sidebar
//        val homeButton: Button = sidebarView.findViewById(R.id.home_button)
//        homeButton.setOnClickListener {
//            Toast.makeText(this, "Home button clicked", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "sidebar_channel",
//                "Sidebar Service",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            val manager = getSystemService(NotificationManager::class.java)
//            manager?.createNotificationChannel(channel)
//        }
//    }
//
//    private fun startForegroundService() {
//        val notification = NotificationCompat.Builder(this, "sidebar_channel")
//            .setContentTitle("Sidebar Service")
//            .setContentText("Sidebar is running")
////            .setSmallIcon(R.drawable.ic_sidebar) // Replace with your icon
//            .build()
//
//        startForeground(1, notification)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (::sidebarView.isInitialized) {
//            windowManager.removeView(sidebarView)
//        }
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//}
//
