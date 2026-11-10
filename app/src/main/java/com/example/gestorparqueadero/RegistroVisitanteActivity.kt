
package com.example.gestorparqueadero

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Visitante(
    val placa: String,
    val tipo: String,
    val apartamento: String,
    val nombre: String,
    val fechaHoraIngreso: String
)

class RegistroVisitanteActivity : AppCompatActivity() {

    private lateinit var etPlaca: EditText
    private lateinit var etApartamento: EditText
    private lateinit var etNombre: EditText
    private lateinit var spTipo: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_visitante)

        etPlaca = findViewById(R.id.etPlacaVisitante)
        etApartamento = findViewById(R.id.etApartamentoVisitante)
        etNombre = findViewById(R.id.etNombreVisitante)
        spTipo = findViewById(R.id.spTipoVisitante)

        val tipos = arrayOf("Carro", "Motocicleta")

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tipos
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spTipo.adapter = adaptador

        findViewById<Button>(R.id.btnRegistrarVisitante).setOnClickListener {
            registrarVisitante()
        }

        findViewById<Button>(R.id.btnVolverVisitante).setOnClickListener {
            finish()
        }
    }

    private fun registrarVisitante() {

        val placa = etPlaca.text.toString().trim().uppercase()
        val apartamento = etApartamento.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val tipo = spTipo.selectedItem.toString()

        if (placa.isEmpty() || apartamento.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(
                this,
                "Completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val fechaHora = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val visitante = Visitante(
            placa,
            tipo,
            apartamento,
            nombre,
            fechaHora
        )

        val preferencias = getSharedPreferences("ParkQR", MODE_PRIVATE)
        val gson = Gson()

        val jsonActual = preferencias.getString("visitantes", null)

        val tipoLista = object : TypeToken<MutableList<Visitante>>() {}.type

        val lista: MutableList<Visitante> =
            if (jsonActual.isNullOrEmpty()) {
                mutableListOf()
            } else {
                gson.fromJson(jsonActual, tipoLista)
            }

        lista.add(visitante)

        preferencias.edit()
            .putString("visitantes", gson.toJson(lista))
            .apply()

        Toast.makeText(
            this,
            "Visitante registrado correctamente",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }
}
