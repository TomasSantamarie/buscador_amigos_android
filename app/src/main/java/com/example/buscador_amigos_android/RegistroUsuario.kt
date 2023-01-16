package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible

class RegistroUsuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        findViewById<EditText>(R.id.usuarioNoValido).isVisible = false
        findViewById<EditText>(R.id.minCaracteres).isVisible = false
        findViewById<EditText>(R.id.noCoinciden).isVisible = false

        findViewById<Button>(R.id.login2).setOnClickListener { acceder() }

        findViewById<Button>(R.id.crearCuenta2).setOnClickListener { cambioPagina() }

        findViewById<EditText>(R.id.contrasena1).addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (findViewById<EditText>(R.id.contrasena1).text.toString().length > 8) {
                    findViewById<EditText>(R.id.minCaracteres).isVisible = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })

        findViewById<EditText>(R.id.contrasena1).addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (findViewById<EditText>(R.id.contrasena1).text.toString().length < 8) {
                    findViewById<EditText>(R.id.minCaracteres).isVisible = true
                    findViewById<EditText>(R.id.contrasena1).setBackgroundResource(R.drawable.borde_edittext_mal)
                } else {
                    findViewById<EditText>(R.id.contrasena1).setBackgroundResource(R.drawable.borde_edittext_bien)
                    findViewById<EditText>(R.id.minCaracteres).isVisible = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })

        findViewById<EditText>(R.id.contrasena2).addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!findViewById<EditText>(R.id.contrasena1).text.toString().equals(findViewById<EditText>(R.id.contrasena2).text.toString())) {
                    findViewById<EditText>(R.id.noCoinciden).isVisible = true
                    findViewById<EditText>(R.id.contrasena2).setBackgroundResource(R.drawable.borde_edittext_mal)
                } else {
                    findViewById<EditText>(R.id.contrasena2).setBackgroundResource(R.drawable.borde_edittext_bien)
                    findViewById<EditText>(R.id.noCoinciden).isVisible = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })
    }
    private fun crearNombre(): Boolean {

        return true

    }

    private fun crearContrasena(): Boolean {

        return true
    }
    private fun acceder() {

    }
    private fun cambioPagina() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}