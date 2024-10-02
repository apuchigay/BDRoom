package com.example.clase20243009.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clase20243009.Model.User
import com.example.clase20243009.Repository.userRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserApp(userRepository: userRepository) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var idDelete by remember { mutableStateOf("") }
    var idUpdate by remember { mutableStateOf("") }
    var scope = rememberCoroutineScope()
    var context = LocalContext.current
    var users by remember { mutableStateOf(listOf<User>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFA500),
                        Color(0xFFB27300)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la pantalla
            Text(text = "Gestión de Usuarios", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para nombre
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(text = "Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para apellido
            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text(text = "Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para edad
            TextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text(text = "Edad") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para registrar usuario
            Button(
                onClick = {
                    val user = User(
                        nombre = nombre,
                        apellido = apellido,
                        edad = edad.toIntOrNull() ?: 0
                    )
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            userRepository.insert(user)
                        }
                        Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB27300)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrar Usuario", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para listar usuarios
            Button(
                onClick = {
                    scope.launch {
                        users = withContext(Dispatchers.IO) {
                            userRepository.getAllUser()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB27300)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Listar Usuarios", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar lista de usuarios en tarjetas usando LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(users) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        //elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "ID: ${user.id}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Nombre: ${user.nombre} ${user.apellido}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Edad: ${user.edad}", style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(8.dp))

                            // Botones de Editar y Eliminar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        nombre = user.nombre
                                        apellido = user.apellido
                                        edad = user.edad.toString()
                                        idUpdate = user.id.toString()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                ) {
                                    Text(text = "Editar", color = Color.White)
                                }

                                Button(
                                    onClick = {
                                        val id = user.id
                                        scope.launch {
                                            val deletedCount: Int
                                            withContext(Dispatchers.IO) {
                                                deletedCount = userRepository.deleteById(id)
                                            }
                                            if (deletedCount > 0) {
                                                Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                                // Actualizar la lista después de eliminar
                                                users = withContext(Dispatchers.IO) {
                                                    userRepository.getAllUser()
                                                }
                                            } else {
                                                Toast.makeText(context, "Usuario no existe", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                                ) {
                                    Text(text = "Eliminar", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
