package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.camposAnadir.isGone = true

        val bundle = intent.extras
        val emailUsuario = bundle?.getString("email")
        val nombreUsuario = bundle?.getString("nombre")
        val ubicacionUsuario = bundle?.getString("ubicacion")

        binding.listaAmigos.layoutManager = LinearLayoutManager(this)
        refrescar(emailUsuario)

        binding.anadir.setOnClickListener {

            Log.v("mensaje", binding.anadir.text.toString())
            if (binding.anadir.text.toString().equals("AGREGAR")){
                binding.titulo.text="Añadir Amigo"
                binding.anadir.text="AÑADIR"
                binding.volver.text="VOLVER"
                binding.texto.text="La persona que añadas como amigo, podrá saber la ubicación que tengas guardada."
                binding.camposAnadir.isGone = false
            }
            if (binding.correo.text.toString().length >= 4) {
                if (binding.anadir.text.toString().equals("AÑADIR"))
                    buscarAmigo(emailUsuario, nombreUsuario, ubicacionUsuario)
                else {
                    if (binding.anadir.text.toString().equals("ELIMINAR"))
                        eliminarAmigo(emailUsuario)
                }
            }

        }
        binding.volver.setOnClickListener {


            if(binding.volver.text.toString().equals("VOLVER")) {
                val intent = Intent(this, Aplicacion::class.java).apply {
                    putExtra("email", emailUsuario)
                }
                startActivity(intent)
            }else{
                binding.titulo.text="Borrar Amigo"
                binding.anadir.text="ELIMINAR"
                binding.tituloCorreo.text="Nombre del amigo"
                binding.parteCodigo.isGone = true
                binding.volver.text="VOLVER"
                binding.texto.text="La persona que elimines como amigo, ya no podrá saber la ubicación que tengas guardada."
                binding.camposAnadir.isGone = false

                Log.v("mensaje", binding.anadir.text.toString())
            }
        }
    }

    private fun refrescar(emailUsuario: String?) {
        db.collection("Usuarios").document(emailUsuario.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null) {

                        binding.listaAmigos.adapter = AmigosAdapter(usuario.getAmigos())
                        val divider = DividerItemDecoration(binding.listaAmigos.context, DividerItemDecoration.VERTICAL)
                        binding.listaAmigos.addItemDecoration(divider)

                    }
                }
            }
    }



    private fun eliminarAmigo(emailUsuario: String?) {
        var nombreAmigo = binding.correo.text.toString()

        Log.v("Nombre Amigo", nombreAmigo)
        if (emailUsuario != null) {
            db.collection("Usuarios").document(emailUsuario)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        val usuario = it.toObject(Usuario::class.java)

                        if (usuario != null) {
                            val nombreUsuario = usuario.getNombre()
                            if (usuario.getNombre().equals(nombreAmigo)) {
                                val text = "No puedes eliminarte"
                                val duration = Toast.LENGTH_SHORT
                                val toast =
                                    Toast.makeText(applicationContext, text, duration)
                                toast.show()
                            } else {
                                val emailAmigo = usuario.encontrarAmigo(nombreAmigo)
                                if(usuario.delAmigo(nombreAmigo)){
                                    db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)
                                    val text = "Eliminado de tu lista de amigos"
                                    val duration = Toast.LENGTH_SHORT
                                    val toast =
                                        Toast.makeText(applicationContext, text, duration)
                                    toast.show()

                                    Log.v("emailAmigo",emailAmigo)
                                    if (emailAmigo != "error") {
                                        deleteAmigo(emailAmigo,usuario.getNombre())
                                        refrescar(emailUsuario)
                                    }else {
                                        val text = "Algo falla"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast =
                                            Toast.makeText(applicationContext, text, duration)
                                        toast.show()
                                    }
                                }else {
                                    val text = "No lo tienes como amigo"
                                    val duration = Toast.LENGTH_SHORT
                                    val toast =
                                        Toast.makeText(applicationContext, text, duration)
                                    toast.show()
                                }

                            }
                        }else{
                            val text = "No existe nuestro usuario"
                            val duration = Toast.LENGTH_SHORT
                            val toast =
                                Toast.makeText(applicationContext, text, duration)
                            toast.show()
                        }
                    }else{
                        val text = "No existe nuestro usuario"
                        val duration = Toast.LENGTH_SHORT
                        val toast =
                            Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }

                }
        }



    }

    private fun deleteAmigo(emailAmigo: String, nombreUsuario: String) {


        db.collection("Usuarios").document(emailAmigo)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null){
                        usuario.delAmigo(nombreUsuario)
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)

                        Log.v("usuario", usuario.getAmigos().toString())
                    }
                }
            }

    }

    private fun buscarAmigo(
        emailUsuario: String?,
        nombreUsuario: String?,
        ubicacionUsuario: String?
    ) {
        val emailAmigo = binding.correo.text.toString()

        Log.v("mensaje", emailAmigo)
        db.collection("Usuarios").document(emailAmigo)
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


                                if (ubicacionUsuario != null) {
                                    amigo.setUbicacion(ubicacionUsuario)
                                }
                                if (emailUsuario != null) {
                                    amigo.setCorreo(emailUsuario)
                                }
                                if (nombreUsuario != null) {
                                    amigo.setNombre(nombreUsuario)

                                    if (usuario.encontrarAmigo(nombreUsuario)
                                            .equals(emailUsuario)
                                    ) {
                                        val text = "Ya lo tienes como amigo"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast =
                                            Toast.makeText(applicationContext, text, duration)
                                        toast.show()
                                    } else {
                                        usuario.getAmigos().add(amigo)
                                        db.collection("Usuarios").document(usuario.getCorreo())
                                            .set(usuario)
                                        val text = "Añadido a tu lista de amigos"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast =
                                            Toast.makeText(applicationContext, text, duration)
                                        toast.show()
                                        amigo.setNombre(usuario.getNombre())
                                        amigo.setUbicacion(usuario.getUbicacion())
                                        amigo.setCorreo(usuario.getCorreo())
                                        if (emailUsuario != null) {
                                            anadirAmigo(emailUsuario, amigo)
                                        }
                                    }
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

    private fun anadirAmigo(emailUsuario: String, amigo: Amigo) {
        db.collection("Usuarios").document(emailUsuario)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null){
                        usuario.getAmigos().add(amigo)
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)
                        refrescar(emailUsuario)

                        Log.v("usuario", usuario.getAmigos().toString())
                    }
                }
            }
    }
}