package com.example.buscador_amigos_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.login).setOnClickListener { acceder() }
        findViewById<Button>(R.id.crearCuenta).setOnClickListener { cambioPagina() }




    }

    private fun checkNombre(): Boolean {

        return true

    }

    private fun checkContraseña(): Boolean {

        return true
    }
    private fun acceder() {

    }
    private fun cambioPagina() {

    }
}