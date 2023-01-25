package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<EditText>(R.id.error).isVisible = false
        findViewById<Button>(R.id.login).setOnClickListener { acceder() }
        findViewById<Button>(R.id.crearCuenta).setOnClickListener { cambioPagina() }

        var bundle = intent.extras
        val pss = bundle?.getString("contraseña")
        val email = bundle?.getString("email")


        findViewById<EditText>(R.id.user).setText(email)
        findViewById<EditText>(R.id.textPassword).setText(pss)

    }

    private fun acceder() {

        val email = findViewById<EditText>(R.id.user).text
        val pss = findViewById<EditText>(R.id.textPassword).text
        findViewById<Button>(R.id.login).setOnClickListener{
            if (email.isNotEmpty() && pss.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(),
                        pss.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        cambioPagina2()
                    }else {
                        findViewById<EditText>(R.id.error).isVisible = true
                    }
                }
            }
        }
    }
    private fun cambioPagina() {
        val intent = Intent(this, RegistroUsuario::class.java)
        startActivity(intent)
    }

    private fun cambioPagina2() {
        val intent = Intent(this, Aplicacion::class.java)
        startActivity(intent)
    }
}