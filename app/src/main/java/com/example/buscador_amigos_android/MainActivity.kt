package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<EditText>(R.id.error).isVisible = false
        findViewById<Button>(R.id.login).setOnClickListener { acceder() }
        findViewById<Button>(R.id.crearCuenta).setOnClickListener { cambioPagina() }




    }

    private fun checkNombre(): Boolean {

        return true

    }

    private fun checkContrasena(): Boolean {

        return true
    }
    private fun acceder() {

    }
    private fun cambioPagina() {
        val intent = Intent(this, RegistroUsuario::class.java)
        startActivity(intent)
    }
}