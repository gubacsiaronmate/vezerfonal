package com.smokinggunstudio.vezerfonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.smokinggunstudio.vezerfonal.helpers.CurrentActivityProvider
import com.smokinggunstudio.vezerfonal.helpers.security.CurrentContextProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        CurrentActivityProvider.current = this
        CurrentContextProvider.current = this
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        if (CurrentActivityProvider.current === this) {
            CurrentActivityProvider.current = null
        }
        super.onDestroy()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}