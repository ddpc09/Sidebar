package com.example.sidebar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SidebarViewModel(application: Application) : AndroidViewModel(application) {

    // Function to initialize sidebar logic
    fun initializeSidebarService() {
        viewModelScope.launch(Dispatchers.IO) {
            // Heavy initialization logic can go here
        }
    }
}