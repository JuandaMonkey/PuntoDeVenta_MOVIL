package com.example.pv_movil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
// utils
import core.utils.SessionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // configurar BottomNavigationView
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // cargar fragmento inicial
        if (savedInstanceState == null) {
            replaceFragment(AdministrarClientes())
        }

        // configurar el listener para los cambios de selección en el BottomNavigationView
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_clientes -> {
                    // solo reemplazar si no es el fragmento actual
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (currentFragment !is AdministrarClientes) {
                        replaceFragment(AdministrarClientes())
                    }
                    true
                }
                // agrega más casos para otros fragmentos según sea necesario
                R.id.nav_logout -> {
                    showLogoutConfirmation()
                    false
                }
                else -> false
            }
        }

        // manejar clics cuando el ítem ya está seleccionado (previene recargas innecesarias)
        bottomNavigation.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.nav_clientes) {
                // aquí se podria implementar scroll al inicio si tuvieras una lista larga,
                // pero por ahora simplemente ignoramos para evitar que se cierre la app.
            }
        }
    }

    // muestra un diálogo de confirmación para cerrar sesión
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas salir?")
            .setPositiveButton("Sí") { _, _ ->
                // limpiar sesión y volver al login
                SessionManager(this).clearSession()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            // si el usuario hace clic en "No", no hará nada
            .setNegativeButton("No", null)
            .show()
    }

    // reemplaza el fragmento actual con el nuevo fragmento
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
