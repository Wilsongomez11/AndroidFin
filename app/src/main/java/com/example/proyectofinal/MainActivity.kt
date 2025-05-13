package com.example.proyectofinal


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import com.example.proyectofinal.Model.MainScreen
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme



class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProyectoFinalTheme {
                Surface {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

