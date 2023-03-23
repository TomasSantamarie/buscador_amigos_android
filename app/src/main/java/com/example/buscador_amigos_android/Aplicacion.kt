package com.example.buscador_amigos_android

import android.app.Activity
import android.content.Intent
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
        val bundle = intent.extras
        val email = bundle?.getString("email")


        if (email != null) {
            db.collection("Usuarios").document(email.toString())
                .get()

                  .addOnSuccessListener {
                    if (it != null) {
                        val usuario = it.toObject(Usuario::class.java)

                        if (usuario != null) {

                            binding.nombre.setText(usuario.getNombre())
                            binding.contador.setText(usuario.getAmigos().size.toString())

                            binding.cuenta.setOnClickListener{
                                val intent = Intent(this, Cuenta::class.java).apply {
                                    putExtra("nombre",usuario.getNombre())
                                    putExtra("email",usuario.getCorreo())
                                    putExtra("codigo",usuario.getCodigo())
                                }
                                startActivity(intent)
                            }

                        }

                    }
                }
        }






    }
}