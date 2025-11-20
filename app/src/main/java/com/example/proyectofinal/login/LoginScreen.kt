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
import com.example.proyectofinal.ViewModel.MeseroViewModel
import com.example.proyectofinal.ViewModel.ViewModelProvider.AuthViewModelFactory
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
    val factory = remember { AuthViewModelFactory(adminService, adminViewModel) }
    val viewModel: AuthViewModel = viewModel(factory = factory)
    val context = LocalContext.current

    // Crear instancia de MeseroViewModel
    val meseroViewModel: MeseroViewModel = viewModel()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginState = viewModel.loginState
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
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(240.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Bienvenido a Pizzería",
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Inicia sesión para continuar",
                color = Color.White.copy(0.8f),
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 28.dp)
            )

            // Tarjeta de login
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xDD1E1E1E))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //  Usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                null,
                                tint = Color(0xFF5E17EB)
                            )
                        },
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5E17EB),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFF5E17EB),
                            focusedLabelColor = Color(0xFF5E17EB),
                            unfocusedLabelColor = Color.LightGray
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(Modifier.height(20.dp))

                    //  Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF5E17EB)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Ver contraseña",
                                    tint = Color(0xFF5E17EB)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5E17EB),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFF5E17EB),
                            focusedLabelColor = Color(0xFF5E17EB),
                            unfocusedLabelColor = Color.LightGray
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )

                    Spacer(Modifier.height(26.dp))

                    Button(
                        onClick = { viewModel.login(username, password) },
                        enabled = canSubmit,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E17EB),
                            disabledContainerColor = Color(0x55FFFFFF),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Iniciar sesión", fontSize = 19.sp)
                    }

                    AnimatedVisibility(visible = isLoading) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFFF9800))
                        }
                    }

                    AnimatedVisibility(visible = loginState is LoginState.Error) {
                        val message = (loginState as? LoginState.Error)?.message ?: ""
                        Text(
                            text = message,
                            color = Color(0xFFFF6B6B),
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }

    if (loginState is LoginState.Success) {
        val user = (loginState as LoginState.Success).user
        LaunchedEffect(user) {
            Toast.makeText(
                context,
                "Bienvenido ${user.nombre} (${user.cargo})",
                Toast.LENGTH_SHORT
            ).show()

            // Verificar el cargo del usuario
            when {
                user.cargo.equals("ADMIN", ignoreCase = true) ||
                        user.cargo.equals("ADMINISTRADOR", ignoreCase = true) -> {
                    // Establecer administrador actual
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

                user.cargo.equals("MESERO", ignoreCase = true) -> {
                    // Establecer mesero actual
                    meseroViewModel.establecerMeseroActual(
                        id = user.id,
                        nombre = user.nombre
                    )
                    // Navegar a la pantalla de mesero
                    navController.navigate("meseroHomeScreen")
                }

                else -> {
                    // Otro tipo de usuario
                    onLoginSuccess(user)
                }
            }
        }
    }
}