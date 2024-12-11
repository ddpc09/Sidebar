package com.example.sidebar

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SidebarWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private var windowManager: WindowManager? = null
    private var sidebarView: View? = null
    private var isSidebarVisible = true

    override suspend fun doWork(): Result {
        inflateSidebar(applicationContext)
        return Result.success()
    }

    private suspend fun inflateSidebar(context: Context) {
        withContext(Dispatchers.Main) {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            sidebarView = inflater.inflate(R.layout.sidebar_layout, null)
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )
            layoutParams.gravity = android.view.Gravity.START
            layoutParams.x = 0 // Sidebar starts visible on-screen

            sidebarView?.let {
                setupSwipeAndClickListener(it, layoutParams)
                windowManager?.addView(it, layoutParams)

                // Ensure width is calculated
                it.post {
                    Log.d("SidebarWorker", "Sidebar width: ${it.width}")
                }
            }
        }
    }

    private fun setupSwipeAndClickListener(view: View, layoutParams: WindowManager.LayoutParams) {
        val gestureDetector =
            GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
                private val SWIPE_THRESHOLD = 100
                private val SWIPE_VELOCITY_THRESHOLD = 100

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Log.d("SidebarWorker", "Fling gesture detected")

                    if (e1 != null) {
                        val diffX = e2.x - e1.x
                        Log.d("SidebarWorker", "Fling diffX=$diffX, velocityX=$velocityX")

                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                // Swipe right
                                Log.d("SidebarWorker", "Swipe right detected")
                                showSidebar(layoutParams)
                            } else {
                                // Swipe left
                                Log.d("SidebarWorker", "Swipe left detected")
                                hideSidebar(layoutParams)
                            }
                            return true
                        }
                    }
                    return false
                }
            })

        // Set onTouchListener to detect swipes and clicks
        view.setOnTouchListener { v, event ->
//            Log.d("SidebarWorker", "Touch event detected: ${event.action}")
            gestureDetector.onTouchEvent(event) // Ensure this is being called
            true
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Save initial touch position for click detection
                    v.tag = Pair(event.x, event.y)
                }

                MotionEvent.ACTION_UP -> {
                    // Retrieve the saved touch position
                    val initialPosition = v.tag as? Pair<Float, Float>
                    if (initialPosition != null) {
                        val deltaX = Math.abs(event.x - initialPosition.first)
                        val deltaY = Math.abs(event.y - initialPosition.second)

                        // If movement is minimal, it's a click
                        if (deltaX < 10 && deltaY < 10) {
                            v.performClick()
                        }
                    }
                }
            }

            // Always pass the event to the GestureDetector
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun showSidebar(layoutParams: WindowManager.LayoutParams) {
        if (!isSidebarVisible) {
            val currentX = layoutParams.x
            val targetX = 0
            Log.d("SidebarWorker", "Animating from $currentX to $targetX")
            animateSidebar(layoutParams, currentX, targetX)
            isSidebarVisible = true
            Log.d("SidebarWorker", "Sidebar shown")
        }
    }

    private fun hideSidebar(layoutParams: WindowManager.LayoutParams) {
        if (isSidebarVisible) {
            val currentX = layoutParams.x
            val sidebarWidth = sidebarView?.width ?: 300 // Fallback width
            val targetX = -sidebarWidth
            Log.d(
                "SidebarWorker",
                "Animating from $currentX to $targetX with sidebar width $sidebarWidth"
            )
            animateSidebar(layoutParams, currentX, targetX)
            isSidebarVisible = false
            Log.d("SidebarWorker", "Sidebar hidden")
        }
    }

    private fun animateSidebar(layoutParams: WindowManager.LayoutParams, startX: Int, endX: Int) {
        Log.d("SidebarWorker", "Starting animation from $startX to $endX")
        val animator = ValueAnimator.ofInt(startX, endX)
        animator.duration = 300 // Animation duration in milliseconds
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            layoutParams.x = animatedValue
            windowManager?.updateViewLayout(sidebarView, layoutParams)
            sidebarView?.invalidate() // Force redraw
            Log.d("SidebarWorker", "Animated x position: $animatedValue")
        }
        animator.start()
    }
}





