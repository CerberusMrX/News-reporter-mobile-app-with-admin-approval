package com.example.newsrep.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsrep.R
import com.example.newsrep.data.NewsRepository
import com.example.newsrep.ui.theme.*
import androidx.compose.animation.core.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.OutlinedTextFieldDefaults

// Add these test credentials at the top of the file
private val TEST_CREDENTIALS = mapOf(
    "admin" to Pair("admin@newsrep.com", "admin123"),
    "reporter" to Pair("reporter@newsrep.com", "reporter123"),
    "user" to Pair("user@newsrep.com", "user123")
)

// First, let's define some colors at the top of the file
private val DeepOrange = Color(0xFFFF5722)    // Deep Orange
private val BrightOrange = Color(0xFFFF8C00)  // Bright Orange
private val Golden = Color(0xFFFFD700)        // Golden Yellow
private val LightYellow = Color(0xFFFFEB3B)   // Light Yellow
private val TextFieldBackground = Color(0xFFE65100)  // Dark Orange for text fields

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToAdmin: () -> Unit,
    onNavigateToReporter: () -> Unit,
    onNavigateToUser: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("user") }
    var showError by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            DeepOrange,      // Deep Orange
                            BrightOrange,    // Bright Orange
                            Golden,          // Golden Yellow
                            BrightOrange,    // Bright Orange
                            DeepOrange       // Deep Orange
                        ),
                        start = Offset(
                            x = cos(angle * (PI / 180).toFloat()) * 1000f + 500f,
                            y = sin(angle * (PI / 180).toFloat()) * 1000f + 500f
                        ),
                        end = Offset(
                            x = cos((angle + 180) * (PI / 180).toFloat()) * 1000f + 500f,
                            y = sin((angle + 180) * (PI / 180).toFloat()) * 1000f + 500f
                        )
                    )
                )
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo and Branding
            Image(
                painter = painterResource(id = R.drawable.news_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            
            Text(
                text = "Heatblast",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Your Gateway to Breaking News",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Delivering Truth, One Story at a Time",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login/Signup Card
            Card(
                modifier = Modifier
                    .widthIn(max = 450.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    DeepOrange.copy(alpha = 0.95f),
                                    Golden.copy(alpha = 0.95f),
                                    DeepOrange.copy(alpha = 0.95f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(450f, 450f)
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (isSignUp) "Create Account" else "Welcome Back",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Role Selection
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Select Role",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                if (!isSignUp) {
                                    RoleChip("User", selectedRole == "user") { selectedRole = "user" }
                                    RoleChip("Reporter", selectedRole == "reporter") { selectedRole = "reporter" }
                                    RoleChip("Admin", selectedRole == "admin") { selectedRole = "admin" }
                                } else {
                                    RoleChip("User", selectedRole == "user") { selectedRole = "user" }
                                    RoleChip("Reporter", selectedRole == "reporter") { selectedRole = "reporter" }
                                }
                            }
                        }

                        if (isSignUp) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Person,
                                        null,
                                        tint = Color.White
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = TextFieldBackground,
                                    unfocusedContainerColor = TextFieldBackground,
                                    disabledContainerColor = TextFieldBackground,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedLeadingIconColor = Color.White,
                                    unfocusedLeadingIconColor = Color.White.copy(alpha = 0.7f),
                                    cursorColor = Color.White
                                )
                            )
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    null,
                                    tint = Color.White
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = TextFieldBackground,
                                unfocusedContainerColor = TextFieldBackground,
                                disabledContainerColor = TextFieldBackground,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                focusedLeadingIconColor = Color.White,
                                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = Color.White
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Lock,
                                    null,
                                    tint = Color.White
                                )
                            },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = TextFieldBackground,
                                unfocusedContainerColor = TextFieldBackground,
                                disabledContainerColor = TextFieldBackground,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                focusedLeadingIconColor = Color.White,
                                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = Color.White
                            )
                        )

                        if (showError) {
                            Text(
                                text = if (isSignUp) "Please fill all fields" else "Invalid credentials",
                                color = Color(0xFFFF3D00),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (isSignUp) {
                                    if (email.isBlank() || password.isBlank() || name.isBlank()) {
                                        showError = true
                                    } else {
                                        val newUser = NewsRepository.signUp(email, name, selectedRole)
                                        if (newUser != null) {
                                            when (selectedRole) {
                                                "reporter" -> onNavigateToReporter()
                                                else -> onNavigateToUser()
                                            }
                                        } else {
                                            showError = true
                                        }
                                    }
                                } else {
                                    val (testEmail, testPassword) = TEST_CREDENTIALS[selectedRole] ?: Pair("", "")
                                    if (email == testEmail && password == testPassword) {
                                        when (selectedRole) {
                                            "admin" -> onNavigateToAdmin()
                                            "reporter" -> onNavigateToReporter()
                                            else -> onNavigateToUser()
                                        }
                                    } else {
                                        showError = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = DeepOrange
                            )
                        ) {
                            Text(
                                if (isSignUp) "Sign Up" else "Login",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isSignUp) "Already have an account? " else "Don't have an account? ",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = if (isSignUp) "Login" else "Sign Up",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .clickable { 
                                        isSignUp = !isSignUp
                                        showError = false
                                        selectedRole = "user"
                                    }
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleChip(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onSelect),
        color = if (selected) Color.White else TextFieldBackground,
        border = BorderStroke(1.dp, if (selected) Color.White else Color.White.copy(alpha = 0.7f))
    ) {
        Text(
            text = text,
            color = if (selected) DeepOrange else Color.White,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        color = Primary,
        strokeWidth = 2.dp
    )
}

@Composable
private fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = Primary,
        textContentColor = TextPrimary
    )
} 