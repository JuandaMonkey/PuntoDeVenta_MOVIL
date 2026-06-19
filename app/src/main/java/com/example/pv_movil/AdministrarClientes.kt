package com.example.pv_movil

import androidx.appcompat.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pv_movil.adapters.ClienteAdapter
import com.google.gson.Gson
import core.services.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
// dtos
import core.dtos.cliente.ClienteRequestDTO
import core.dtos.cliente.ClientesResponseDTO
import core.dtos.cliente.ClienteResponseDTO
import core.dtos.cliente.ClienteCreateRequestDTO

class AdministrarClientes : Fragment() {

    // variables
    private lateinit var etClave: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEdad: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var layoutApellido: TextInputLayout
    private lateinit var btnNuevo: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button
    private lateinit var rvClientes: RecyclerView
    private lateinit var adapter: ClienteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_administrar_clientes, container, false)

        // inicializar variables
        etClave = view.findViewById(R.id.etClave)
        etNombre = view.findViewById(R.id.etNombre)
        etApellido = view.findViewById(R.id.etApellido)
        layoutApellido = view.findViewById(R.id.layoutApellido)
        etEdad = view.findViewById(R.id.etEdad)
        etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento)
        btnNuevo = view.findViewById(R.id.btnNuevo)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnEliminar = view.findViewById(R.id.btnEliminar)
        rvClientes = view.findViewById(R.id.rvClientes)

        // configurar componentes
        setupDatePicker()
        setupRecyclerView()
        setupButtons()
        loadClientes()

        // retornar la vista
        return view
    }

    // funciones auxiliares
    private fun setupDatePicker() {
        etFechaNacimiento.isFocusable = false
        etFechaNacimiento.isClickable = true
        etFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    // muestra el datepicker
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // crear el datepicker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // formato yyyy-MM-dd
                val formattedDate = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                etFechaNacimiento.setText(formattedDate)

                // calcular edad automáticamente
                val birthDate = Calendar.getInstance()
                birthDate.set(selectedYear, selectedMonth, selectedDay)

                // obtener la fecha actual
                var calculatedAge = calendar.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

                // si aún no se ha cumplido el cumpleaños este año, restar 1 a la edad
                if (calendar.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                    calculatedAge--
                }

                // actualizar el campo de edad
                etEdad.setText(calculatedAge.coerceAtLeast(0).toString())
            },
            year, month, day
        )

        // limitar la fecha máxima a hoy para evitar errores
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        // mostrar el datepicker
        datePickerDialog.show()
    }

    // configura el recyclerview
    private fun setupRecyclerView() {
        rvClientes.layoutManager = LinearLayoutManager(requireContext())
        adapter = ClienteAdapter(emptyList()) { cliente ->
            // al seleccionar un cliente de la lista
            etClave.setText(cliente.clave.toString())
            etNombre.setText(cliente.nombreCompleto)
            etApellido.setText("") 
            layoutApellido.visibility = View.GONE // escondemos el campo Apellido
            etEdad.setText(cliente.edad.toString())
            etFechaNacimiento.setText(cliente.fechaNacimiento)
        }
        // establecer el adapter
        rvClientes.adapter = adapter
    }

    // configura los botones
    private fun setupButtons() {
        btnNuevo.setOnClickListener {
            clearFields()
        }

        btnGuardar.setOnClickListener {
            saveCliente()
        }

        btnEliminar.setOnClickListener {
            // validar que se haya seleccionado un cliente
            val claveStr = etClave.text.toString()
            if (claveStr.isEmpty()) {
                Toast.makeText(requireContext(), "Seleccione un cliente para eliminar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // eliminar el cliente
            val clave = claveStr.toInt()
            val nombre = etNombre.text.toString()

            // mostrar un diálogo de confirmación
            showDeleteConfirmation(clave, nombre)
        }
    }

    private fun showDeleteConfirmation(clave: Int, nombre: String) {
        // mostrar un diálogo de confirmación
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar al cliente $nombre?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteCliente(clave)
            }
            // si se cancela, no hacer nada
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteCliente(clave: Int) {
        lifecycleScope.launch {
            try {
                // llamar al servicio de eliminación
                val request = ClienteRequestDTO(clave)
                val response = RetrofitClient.getClienteService(requireContext()).deleteCliente(request)

                if (response.isSuccessful && response.body()?.exito == true) {
                    // si se elimina correctamente, limpiar los campos
                    Toast.makeText(requireContext(), "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show()
                    clearFields()
                    loadClientes()
                // si no se elimina correctamente, mostrar un mensaje de error
                } else {
                    val errorMsg = try {
                        val errorJson = response.errorBody()?.string()
                        Gson().fromJson(errorJson, ClienteResponseDTO::class.java)?.mensaje
                    } catch (e: Exception) {
                        null
                    }
                    Toast.makeText(requireContext(), errorMsg ?: "Error al eliminar cliente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // limpia los campos
    private fun clearFields() {
        etClave.setText("")
        etNombre.setText("")
        etApellido.setText("")
        layoutApellido.visibility = View.VISIBLE // volvemos a mostrar el campo Apellido
        etEdad.setText("")
        etFechaNacimiento.setText("")
    }

    // guarda un cliente
    private fun saveCliente() {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val edadStr = etEdad.text.toString().trim()
        val fecha = etFechaNacimiento.text.toString().trim()

        // validaciones
        if (nombre.length < 2 || nombre.length > 60) {
            etNombre.error = "Nombre debe tener entre 2 y 60 caracteres"
            return
        }
        if (apellido.length < 2 || apellido.length > 60) {
            etApellido.error = "Apellido debe tener entre 2 y 60 caracteres"
            return
        }
        val edad = edadStr.toIntOrNull()
        if (edad == null || edad !in 0..150) {
            etEdad.error = "Edad debe ser entre 0 y 150"
            return
        }
        if (fecha.isEmpty()) {
            Toast.makeText(requireContext(), "Seleccione una fecha de nacimiento", Toast.LENGTH_SHORT).show()
            return
        }

        // guardar el cliente
        lifecycleScope.launch {
            try {
                // llamar al servicio de guardado
                val request = ClienteCreateRequestDTO(nombre, apellido, edad, fecha)
                val response = RetrofitClient.getClienteService(requireContext()).postCliente(request)

                if (response.isSuccessful && response.body()?.exito == true) {
                    // si se guarda correctamente, limpiar los campos
                    Toast.makeText(requireContext(), "Cliente guardado correctamente", Toast.LENGTH_SHORT).show()
                    clearFields()
                    loadClientes()
                // si no se guarda correctamente, mostrar un mensaje de error
                } else {
                    val errorMsg = try {
                        val errorJson = response.errorBody()?.string()
                        Gson().fromJson(errorJson, ClienteResponseDTO::class.java)?.mensaje
                    } catch (e: Exception) {
                        null
                    }
                    Toast.makeText(requireContext(), errorMsg ?: "Error al guardar cliente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // carga la lista de clientes
    private fun loadClientes() {
        lifecycleScope.launch {
            try {
                // llamar al servicio de clientes
                val response = RetrofitClient.getClienteService(requireContext()).getClientes()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.exito == true) {
                        adapter.updateList(body.data ?: emptyList())
                    } else {
                        Toast.makeText(requireContext(), body?.mensaje ?: "Error al obtener clientes", Toast.LENGTH_SHORT).show()
                    }
                // si no se obtienen correctamente, mostrar un mensaje de error
                } else {
                    val errorMsg = try {
                        val errorJson = response.errorBody()?.string()
                        Gson().fromJson(errorJson, ClientesResponseDTO::class.java)?.mensaje
                    } catch (e: Exception) {
                        null
                    }
                    Toast.makeText(requireContext(), errorMsg ?: "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Sin conexión: Revisa tu internet", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
