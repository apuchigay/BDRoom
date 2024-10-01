package com.example.clase20243009.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var nombre by remember { mutableStateOf("")}
    var apellido by remember { mutableStateOf("")}
    var edad by remember { mutableStateOf("")}
    var scope = rememberCoroutineScope()

    var context = LocalContext.current

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ){
        TextField(
            value =  nombre,
            onValueChange = {nombre = it},
            label = { Text(text = "Nombre")}
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value =  apellido,
            onValueChange = {apellido = it},
            label = { Text(text = "Apellido")}
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value =  edad,
            onValueChange = {edad = it},
            label = { Text(text = "Edad")},
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )


        Button(onClick = {
            val user = User(
                nombre = nombre,
                apellido = apellido,
                edad = edad.toIntOrNull() ?:0
            )
            scope.launch {
                withContext(Dispatchers.IO){
                    userRepository.insert(user)
                }
                Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        var users by remember { mutableStateOf(listOf<User>()) }

        Button(
            onClick = {
                scope.launch {
                    users = withContext(Dispatchers.IO){
                        userRepository.getAllUser()
                    }
                }
            }
        ) {
            Text("Listar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column{
            users.forEach{ user ->
                Text("${user.nombre} ${user.apellido}, Age: ${user.edad}")
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}