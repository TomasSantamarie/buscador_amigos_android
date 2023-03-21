package com.example.buscador_amigos_android

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.buscador_amigos_android.databinding.ActivityAplicacionBinding
import com.example.buscador_amigos_android.databinding.ActivityRegistroUsuarioBinding
import com.google.firebase.firestore.FirebaseFirestore

class Aplicacion : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityAplicacionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAplicacionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var bundle = intent.extras
        var email = bundle?.getString("email")
        if (email != null) {
            Log.v("email", email)
        }

        if (email != null) {
            db.collection("Usuarios").document(email)
                .get()

                  .addOnSuccessListener {
                    if (it != null) {
                        val usuario = it.toObject(Usuario::class.java)

                        if (usuario != null) {

                            binding.nombre.text = usuario.getNombre()
                            binding.contador.setText(usuario.getAmigos().size)


                        }

                    }
                }
        }


    }
}