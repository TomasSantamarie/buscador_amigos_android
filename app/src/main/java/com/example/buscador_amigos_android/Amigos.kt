package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.buscador_amigos_android.databinding.ActivityAmigosBinding
import com.google.firebase.firestore.FirebaseFirestore

class Amigos : AppCompatActivity() {
    private lateinit var binding: ActivityAmigosBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmigosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle = intent.extras
        val emailUsuario = bundle?.getString("email")
        val nombreUsuario = bundle?.getString("nombre")
        val ubicacionUsuario = bundle?.getString("ubicacion")

        binding.anadir.setOnClickListener {
            buscarAmigo(emailUsuario,nombreUsuario,ubicacionUsuario)

        }
        binding.volver.setOnClickListener {
            val intent= Intent(this, Aplicacion::class.java).apply {
                putExtra("email",emailUsuario)
            }
            startActivity(intent)
        }
    }

    private fun buscarAmigo(
        emailUsuario: String?,
        nombreUsuario: String?,
        ubicacionUsuario: String?
    ) {
        val email = binding.correo.text.toString()

        Log.v("mensaje", email)
        db.collection("Usuarios").document(email)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null) {
                        if (usuario.getCorreo().equals(emailUsuario)) {
                            val text = "No puedes añadirte como amigo"
                            val duration = Toast.LENGTH_SHORT
                            val toast =
                                Toast.makeText(applicationContext, text, duration)
                            toast.show()
                        } else {
                            if (usuario.getCodigo().equals(binding.codigo.text.toString())
                            ) {

                                var amigo = Amigo("","")

                                if (nombreUsuario != null) {
                                    amigo.setNombre(nombreUsuario)
                                }
                                if (ubicacionUsuario != null) {
                                    amigo.setUbicacion(ubicacionUsuario)
                                }
                                usuario.getAmigos().add(amigo)
                                db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)
                                val text = "Añadido a tu lista de amigos"
                                val duration = Toast.LENGTH_SHORT
                                val toast =
                                    Toast.makeText(applicationContext, text, duration)
                                toast.show()
                                amigo.setNombre(usuario.getNombre())
                                amigo.setUbicacion(usuario.getUbicacion())
                                if (emailUsuario != null) {
                                    anadirAmigo(emailUsuario,amigo)
                                }

                            } else {
                                val text = "Correo o código incorrectos"
                                val duration = Toast.LENGTH_SHORT
                                val toast =
                                    Toast.makeText(applicationContext, text, duration)
                                toast.show()
                            }
                        }
                    }else{
                        val text = "Correo o código incorrectos"
                        val duration = Toast.LENGTH_SHORT
                        val toast =
                            Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }
                }else{
                    val text = "Correo o código incorrectos"
                    val duration = Toast.LENGTH_SHORT
                    val toast =
                        Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }

            }

    }

    private fun anadirAmigo(email: String, amigo: Amigo) {
        db.collection("Usuarios").document(email)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null){
                        usuario.getAmigos().add(amigo)
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)

                        Log.v("usuario", usuario.getAmigos().toString())
                    }
                }
            }
    }
}