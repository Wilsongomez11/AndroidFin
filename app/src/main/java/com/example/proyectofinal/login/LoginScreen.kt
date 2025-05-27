package com.example.proyectofinal.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.admin.AuthViewModel
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.ViewModelProvider.AuthViewModelFactory
import com.example.proyectofinal.admin.LoginState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: (Administrador) -> Unit,
    adminService: AdministradorService
) {
    val factory = remember { AuthViewModelFactory(adminService) }
    val viewModel: AuthViewModel = viewModel(factory = factory)

    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState = viewModel.loginState

    val background = painterResource(id = R.drawable.pizza_background)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = background,
            contentDescription = "Fondo divertido de Pizza Steve",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xAA000000))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.LightGray) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(username, password)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5722)
                )
            ) {
                Text("Ingresar", color = Color.White)
            }

            when (loginState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                }

                is LoginState.Error -> {
                    Text(
                        loginState.message,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is LoginState.Success -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            "Bienvenido ${loginState.admin.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                        onLoginSuccess(loginState.admin)
                    }
                }

                else -> {}
            }
        }
    }
}
