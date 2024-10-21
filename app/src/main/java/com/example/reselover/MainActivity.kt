package com.example.reselover

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.reselover.note.presentance.screen.homeScreen.viewModel.HomeViewModel
import com.example.reselover.note.presentance.screen.homeScreen.screen.HomeScreen
import com.example.reselover.ui.theme.ReseloverTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReseloverTheme {
                val viewModel = hiltViewModel<HomeViewModel>()
                val state by viewModel.homeState.collectAsState()

                HomeScreen(homeState = state, homeEvent = viewModel::onEvent)
            }
        }
    }
}

