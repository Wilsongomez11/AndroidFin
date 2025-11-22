package com.example.proyectofinal.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinal.Api.AdministradorService
import com.example.proyectofinal.LoginResponse
import com.example.proyectofinal.Model.Administrador
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.AdministradorViewModel
import com.example.proyectofinal.ViewModel.LoginViewModel
import com.example.proyectofinal.ViewModel.MeseroViewModel
import com.example.proyectofinal.admin.AuthViewModel
import com.example.proyectofinal.admin.LoginState
import kotlin.Long
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: (LoginResponse) -> Unit,
    adminService: AdministradorService,
    adminViewModel: AdministradorViewModel
) {
    val context = LocalContext.current

    // ViewModel correcto (ya no usa factory inexistente)
    val loginViewModel: LoginViewModel = viewModel()
    val meseroViewModel: MeseroViewModel = viewModel()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginState by loginViewModel.loginState.collectAsState()

    val isLoading = loginState is LoginState.Loading
    val canSubmit = username.isNotBlank() && password.isNotBlank() && !isLoading

    var visible by remember { mutableStateOf(false) }
    val logoScale by animateFloatAsState(if (visible) 1f else 0.8f)
    val logoAlpha by animateFloatAsState(if (visible) 1f else 0f)

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B093B), Color(0xFF3A0CA3), Color(0xFF7209B7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.pizza_logo),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .blur(80.dp)
                .alpha(0.15f)
                .padding(top = 40.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pizza_logo),
                contentDescription = "Logo de la pizzería",
                modifier = Modifier
                    .size(240.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
            )

            Spacer(Modifier.height(28.dp))
            Text(
                "Bienvenido a Pizzería",
                color = Color.White,
                fontSize = 28.sp
            )
            Text(
                "Inicia sesión para continuar",
                color = Color.White.copy(0.8f),
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 28.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xDD1E1E1E))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        singleLine = true,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff
                                    else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation =
                            if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(26.dp))

                    Button(
                        onClick = {
                            loginViewModel.login(adminService, username, password)
                        },
                        enabled = canSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Text("Iniciar sesión", fontSize = 19.sp)
                    }

                    if (isLoading) {
                        Spacer(Modifier.height(16.dp))
                        CircularProgressIndicator(color = Color(0xFFFF9800))
                    }

                    if (loginState is LoginState.Error) {
                        Text(
                            (loginState as LoginState.Error).message,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }

    // Una vez inicia sesión correctamente
    if (loginState is LoginState.Success) {
        val user = (loginState as LoginState.Success).user

        LaunchedEffect(user) {
            Toast.makeText(
                context,
                "Bienvenido ${user.nombre} (${user.cargo})",
                Toast.LENGTH_SHORT
            ).show()

            when (user.cargo.uppercase()) {

                "ADMINISTRADOR", "ADMIN" -> {
                    adminViewModel.establecerAdministradorActual(
                        Administrador(
                            id = user.id,
                            username = user.username,
                            password = "",
                            nombre = user.nombre,
                            cargo = user.cargo
                        )
                    )
                    onLoginSuccess(user)
                }

                "MESERO" -> {
                    meseroViewModel.establecerMeseroActual(user.id, user.nombre)
                    navController.navigate("mesero")
                }

                "PIZZERO" -> {
                    navController.navigate("pizzero")
                }

                else -> onLoginSuccess(user)
            }
        }
    }
}
