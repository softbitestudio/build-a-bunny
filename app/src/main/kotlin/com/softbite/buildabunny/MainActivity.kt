package com.softbite.buildabunny

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.softbite.buildabunny.data.repository.CharacterRepository
import com.softbite.buildabunny.navigation.AppNavGraph
import com.softbite.buildabunny.ui.theme.BuildABunnyTheme

class MainActivity : ComponentActivity() {

    // In a production app this would be injected (e.g. Hilt/Koin).
    // Kept as a field here so it survives configuration changes via the activity scope.
    private val repository = CharacterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildABunnyTheme {
                AppNavGraph(repository = repository)
            }
        }
    }
}
