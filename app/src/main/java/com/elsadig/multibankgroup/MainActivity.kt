package com.elsadig.multibankgroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.elsadig.multibankgroup.ui.navigation.NavigationSetup
import com.elsadig.multibankgroup.ui.theme.MultiBankGroupTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiBankGroupTheme {
                NavigationSetup()
            }
        }
    }
}