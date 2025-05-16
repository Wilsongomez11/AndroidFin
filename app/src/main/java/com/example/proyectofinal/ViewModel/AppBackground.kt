package com.example.proyectofinal.ViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.proyectofinal.R

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    val background = painterResource(id = R.drawable.pizza_background)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = background,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xAA000000))
        )

        content()
    }
}
