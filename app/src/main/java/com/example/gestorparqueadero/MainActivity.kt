package com.example.gestorparqueadero

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var loginLayout: LinearLayout

    private lateinit var etCedula: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvMensaje: TextView
    private lateinit var tvRegistrarse: TextView
    private lateinit var tvOlvido: TextView

    private lateinit var menuLayout: LinearLayout
    private lateinit var tvBienvenidaMenu: TextView
    private lateinit var btnIngreso: Button
    private lateinit var btnSalida: Button
    private lateinit var btnVisitante: Button
    private lateinit var btnRegistroVehiculo: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var ivMenuIcon: ImageView

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        preferenceManager = PreferenceManager(this)

        // Elementos del login
        loginLayout = findViewById(R.id.loginLayout)
        etCedula = findViewById(R.id.etCedula)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvMensaje = findViewById(R.id.tvMensaje)
        tvRegistrarse = findViewById(R.id.tvRegistrarse)
        tvOlvido = findViewById(R.id.tvOlvido)

        // Elementos del menú
        menuLayout = findViewById(R.id.menuLayout)
        tvBienvenidaMenu = findViewById(R.id.tvBienvenidaMenu)
        btnIngreso = findViewById(R.id.btnIngreso)
        btnSalida = findViewById(R.id.btnSalida)
        btnVisitante = findViewById(R.id.btnVisitante)
        btnRegistroVehiculo = findViewById(R.id.btnRegistroVehiculo)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        ivMenuIcon = findViewById(R.id.ivMenuIcon)

        // Comprobar si existe un usuario guardado
        if (preferenceManager.estaLogueado()) {
            mostrarMenu()
        }

        // Iniciar sesión
        btnLogin.setOnClickListener {

            val cedula = etCedula.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (cedula.isEmpty() || password.isEmpty()) {

                tvMensaje.text = "Ingresa cédula y contraseña"

                return@setOnClickListener
            }

            val usuario = preferenceManager.obtenerUsuario()

            if (
                usuario != null &&
                usuario.cedula == cedula &&
                usuario.password == password
            ) {

                preferenceManager.guardarHoraInicio(
                    System.currentTimeMillis()
                )

                mostrarMenu()

            } else {

                tvMensaje.text =
                    "Cédula o contraseña incorrecta"
            }
        }

        // Abrir registro
        tvRegistrarse.setOnClickListener {
            mostrarDialogoRegistro()
        }

        // Recuperación de contraseña
        tvOlvido.setOnClickListener {

            Toast.makeText(
                this,
                "Contacta al administrador",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Ingreso QR
        btnIngreso.setOnClickListener {

            Toast.makeText(
                this,
                "Ingreso mediante código QR",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Salida QR
        btnSalida.setOnClickListener {

            Toast.makeText(
                this,
                "Salida mediante código QR",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Visitante
        btnVisitante.setOnClickListener {

            Toast.makeText(
                this,
                "Registro de visitante",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Registro de vehículo
        btnRegistroVehiculo.setOnClickListener {

            Toast.makeText(
                this,
                "Registro de vehículo",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener {

            preferenceManager.eliminarUsuario()

            mostrarLogin()
        }

        // Información del usuario
        ivMenuIcon.setOnClickListener {

            mostrarDialogoInfoUsuario()
        }
    }

    private fun mostrarMenu() {

        loginLayout.visibility = View.GONE
        menuLayout.visibility = View.VISIBLE
        tvMensaje.text = ""

        val usuario = preferenceManager.obtenerUsuario()

        if (usuario != null) {

            tvBienvenidaMenu.text =
                "Bienvenido, ${usuario.nombre} ${usuario.apellido}"

        } else {

            tvBienvenidaMenu.text =
                "Bienvenido, Usuario"
        }
    }

    private fun mostrarLogin() {

        loginLayout.visibility = View.VISIBLE
        menuLayout.visibility = View.GONE

        etCedula.text.clear()
        etPassword.text.clear()

        tvMensaje.text = ""
    }

    private fun mostrarDialogoRegistro() {

        val view = layoutInflater.inflate(
            R.layout.dialog_registro,
            null
        )

        val etNombre =
            view.findViewById<EditText>(
                R.id.etNombreRegistro
            )

        val etApellido =
            view.findViewById<EditText>(
                R.id.etApellidoRegistro
            )

        val etCedula =
            view.findViewById<EditText>(
                R.id.etCedulaRegistro
            )

        val etPassword =
            view.findViewById<EditText>(
                R.id.etPasswordRegistro
            )

        val dialog = AlertDialog.Builder(this)
            .setTitle("Registrarse")
            .setView(view)
            .setPositiveButton("Registrar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {

            val botonRegistrar =
                dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
                )

            botonRegistrar.setOnClickListener {

                val nombre =
                    etNombre.text.toString().trim()

                val apellido =
                    etApellido.text.toString().trim()

                val cedula =
                    etCedula.text.toString().trim()

                val password =
                    etPassword.text.toString().trim()

                if (
                    nombre.isEmpty() ||
                    apellido.isEmpty() ||
                    cedula.isEmpty() ||
                    password.isEmpty()
                ) {

                    Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                val usuario = Usuario(
                    nombre = nombre,
                    apellido = apellido,
                    cedula = cedula,
                    password = password
                )

                preferenceManager.guardarUsuario(usuario)

                Toast.makeText(
                    this,
                    "Registro exitoso. Inicia sesión.",
                    Toast.LENGTH_LONG
                ).show()

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun mostrarDialogoInfoUsuario() {

        val usuario =
            preferenceManager.obtenerUsuario()

        if (usuario == null) {

            Toast.makeText(
                this,
                "No hay usuario registrado",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val hora =
            preferenceManager.obtenerHoraInicio()

        val mensaje = """
            Nombre: ${usuario.nombre} ${usuario.apellido}
            
            Cédula: ${usuario.cedula}
            
            Inicio de sesión: $hora
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Información de sesión")
            .setMessage(mensaje)
            .setPositiveButton(
                "Aceptar",
                null
            )
            .show()
    }
}