package com.example.buscador_amigos_android

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.view.isVisible
import com.example.buscador_amigos_android.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.error.isVisible = false
        binding.login.setOnClickListener { acceder() }
        binding.crearCuenta.setOnClickListener { cambioPagina() }

        var bundle = intent.extras
        val pss = bundle?.getString("contraseña")
        val email = bundle?.getString("email")


        binding.user.setText(email)
        binding.textPassword.setText(pss)

        binding.textPassword.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.user.setBackgroundResource(R.drawable.borde_edittext_bien)
                binding.textPassword.setBackgroundResource(R.drawable.borde_edittext_bien)}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })


        binding.user.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.user.setBackgroundResource(R.drawable.borde_edittext_bien)
                binding.textPassword.setBackgroundResource(R.drawable.borde_edittext_bien)}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun acceder() {

        val email = binding.user.text
        val pss = binding.textPassword.text

            if (email.isNotEmpty() && pss.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(),
                        pss.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.v("correo", email.toString())
                        cambioPagina2(email.toString())
                    }else {
                        binding.error.isVisible = true
                        binding.user.setBackgroundResource(R.drawable.borde_edittext_mal)
                        binding.textPassword.setBackgroundResource(R.drawable.borde_edittext_mal)
                    }
                }
            }

    }
    private fun cambioPagina() {
        val intent = Intent(this, RegistroUsuario::class.java)
        startActivity(intent)

    }

    private fun cambioPagina2(email: String) {
        val intent = Intent(this, Aplicacion::class.java).apply {
            putExtra("email", email)
        }
        startActivity(intent)
    }
}