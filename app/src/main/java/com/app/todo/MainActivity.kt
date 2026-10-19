package com.app.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.app.todo.data.local.preferences.UserPreferencesRepository
import com.app.todo.presentation.navigation.AppNavGraph
import com.app.todo.ui.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by preferencesRepository.themeMode.collectAsState(initial = "System default")
            TodoTheme(themeMode = themeMode) {
                AppNavGraph()
            }
        }
    }
}