package com.mezzat.instantsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mezzat.instantsearch.search.presentation.ui.SearchScreen
import com.mezzat.instantsearch.ui.theme.InstantSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstantSearchTheme {
                SearchScreen()
            }
        }
    }
}
