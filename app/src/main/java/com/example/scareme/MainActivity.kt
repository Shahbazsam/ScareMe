package com.example.scareme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.scareme.ui.theme.ScareMeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the app instance
       val  app = application as ScareMeApplication
        enableEdgeToEdge()
        setContent {
            ScareMeTheme {
                NavigationGraph(
                        app = app
                )
            }
        }
    }
}

