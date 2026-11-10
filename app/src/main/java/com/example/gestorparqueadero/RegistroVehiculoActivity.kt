
package com.example.gestorparqueadero

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegistroVehiculoActivity : AppCompatActivity() {

    private lateinit var etPlaca: EditText
    private lateinit var etPropietario: EditText
    private lateinit var etFechaIngreso: EditText
    private lateinit var spTipoVehiculo: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_vehiculo)

        etPlaca = findViewById(R.id.etPlaca)
        etPropietario = findViewById(R.id.etPropietario)
        etFechaIngreso = findViewById(R.id.etFechaIngreso)
        spTipoVehiculo = findViewById(R.id.spTipoVehiculo)

        val tipos = arrayOf("Carro", "Motocicleta")

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tipos
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spTipoVehiculo.adapter = adaptador

        findViewById<Button>(R.id.btnRegistrarVehiculo).setOnClickListener {
            registrarVehiculo()
        }

        findViewById<Button>(R.id.btnVolverVehiculo).setOnClickListener {
            finish()
        }
    }

    private fun registrarVehiculo() {
        val placa = etPlaca.text.toString().trim().uppercase()
        val propietario = etPropietario.text.toString().trim()
        val fecha = etFechaIngreso.text.toString().trim()
        val tipo = spTipoVehiculo.selectedItem.toString()

        if (placa.isEmpty() || propietario.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(
                this,
                "Completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val vehiculo = Vehiculo(
            placa = placa,
            tipo = tipo,
            propietario = propietario,
            fechaIngreso = fecha
        )

        val preferencias = getSharedPreferences(
            "ParkQR",
            MODE_PRIVATE
        )

        val gson = Gson()
        val jsonActual = preferencias.getString("vehiculos", null)

        val tipoLista = object : TypeToken<MutableList<Vehiculo>>() {}.type

        val lista: MutableList<Vehiculo> =
            if (jsonActual.isNullOrEmpty()) {
                mutableListOf()
            } else {
                gson.fromJson(jsonActual, tipoLista)
            }

        if (lista.any { it.placa.equals(placa, ignoreCase = true) }) {
            Toast.makeText(
                this,
                "Ya existe un vehículo con esa placa",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lista.add(vehiculo)

        preferencias.edit()
            .putString("vehiculos", gson.toJson(lista))
            .apply()

        Toast.makeText(
            this,
            "Vehículo registrado correctamente",
            Toast.LENGTH_LONG
        ).show()

        setResult(Activity.RESULT_OK)
        finish()
    }
}
