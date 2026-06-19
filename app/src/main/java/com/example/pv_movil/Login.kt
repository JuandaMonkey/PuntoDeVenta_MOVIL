package com.example.pv_movil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import core.services.RetrofitClient
import core.utils.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
// dtos
import core.dtos.auth.LoginRequestDTO
import core.dtos.auth.LoginResponseDTO

class Login : AppCompatActivity() {

    // declaración de variables
    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // inicialización de variables
        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContrasena)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)

        // evento click del botón de inicio de sesión
        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            // validación de campos vacíos
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                tvError.text = "Completa todos los campos"
                return@setOnClickListener
            }
            // llamada a la función de inicio de sesión
            doLogin(usuario, contrasena)
        }
    }

    // función de inicio de sesión
    private fun doLogin(usuario: String, contrasena: String) {
        btnLogin.isEnabled = false
        tvError.text = ""

        // uso de corrutinas para realizar la llamada a la api en un hilo separado
        lifecycleScope.launch {
            try {
                // llamada a la api para iniciar sesión
                val response = RetrofitClient.getAuthService(this@Login).login(
                    LoginRequestDTO(usuario, contrasena))

                // procesamiento de la respuesta de la api
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.exito == true) {
                        // guardar el token de forma segura
                        val sessionManager = SessionManager(this@Login)
                        sessionManager.saveToken(body.token ?: "")

                        // inicio de sesión exitoso y redirección a la pantalla principal
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // mensaje del back (200 OK con exito: false)
                        tvError.text = body?.mensaje ?: "Credenciales incorrectas"
                        btnLogin.isEnabled = true
                    }
                } else {
                    // mensaje del back desde errorBody (401, 400, etc)
                    val errorMsg = try {
                        val errorJson = response.errorBody()?.string()
                        val errorObj = Gson().fromJson(errorJson, LoginResponseDTO::class.java)
                        errorObj?.mensaje
                    } catch (e: Exception) {
                        null
                    }

                    // Si no hay mensaje del back, usamos uno genérico según el código HTTP
                    tvError.text = errorMsg ?: when (response.code()) {
                        401 -> "Usuario o contraseña incorrectos"
                        else -> "Error del servidor: ${response.code()}"
                    }
                    btnLogin.isEnabled = true
                }
            } catch (e: Exception) {
                // mensaje de error en caso de fallo en la conexión
                tvError.text = "Sin conexión: Revisa tu internet"
                btnLogin.isEnabled = true
            }
        }
    }
}
