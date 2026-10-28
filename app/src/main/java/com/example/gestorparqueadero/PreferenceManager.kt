package com.example.gestorparqueadero

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "ParkQR",
            Context.MODE_PRIVATE
        )

    fun guardarUsuario(usuario: Usuario) {
        prefs.edit()
            .putString("nombre", usuario.nombre)
            .putString("apellido", usuario.apellido)
            .putString("cedula", usuario.cedula)
            .putString("password", usuario.password)
            .apply()
    }

    fun obtenerUsuario(): Usuario? {

        val cedula = prefs.getString("cedula", null)

        if (cedula == null) {
            return null
        }

        return Usuario(
            nombre = prefs.getString("nombre", "") ?: "",
            apellido = prefs.getString("apellido", "") ?: "",
            cedula = cedula,
            password = prefs.getString("password", "") ?: ""
        )
    }

    fun eliminarUsuario() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun estaLogueado(): Boolean {
        return prefs.contains("cedula")
    }

    fun guardarHoraInicio(timestamp: Long) {
        prefs.edit()
            .putLong("hora_inicio", timestamp)
            .apply()
    }

    fun obtenerHoraInicio(): String {

        val timestamp = prefs.getLong(
            "hora_inicio",
            0L
        )

        if (timestamp == 0L) {
            return "No disponible"
        }

        val sdf = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        )

        return sdf.format(Date(timestamp))
    }
}