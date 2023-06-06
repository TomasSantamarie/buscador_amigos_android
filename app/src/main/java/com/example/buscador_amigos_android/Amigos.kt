package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscador_amigos_android.databinding.ActivityAmigosBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Amigos : AppCompatActivity(), OnAmigoClickListener {
    private lateinit var binding: ActivityAmigosBinding
    private val db = FirebaseFirestore.getInstance()
    private var ChatId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmigosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        binding.camposAnadir.isGone = true
        binding.accionAdministrar.isGone = true
        binding.camposAdministrar2.isGone = true

        val bundle = intent.extras
        val emailUsuario = bundle?.getString("email")
        val nombreUsuario = bundle?.getString("nombre")
        //val ubicacionUsuario = bundle?.getString("ubicacion")

        val nombreAmigo = bundle?.getString("nombreAmigo")
        //val CorreoAmigo = bundle?.getString("CorreoAmigo")

        binding.correo.setText(nombreAmigo)

        binding.listaAmigos.layoutManager = LinearLayoutManager(this)
        refrescar(emailUsuario)

        binding.anadir.setOnClickListener {

            Log.v("mensaje", binding.anadir.text.toString())
            if (binding.anadir.text.toString().equals("AGREGAR")) {
                binding.titulo.text = "Añadir Amigo"
                binding.anadir.text = "AÑADIR"
                binding.volver.text = "VOLVER"
                binding.texto.text =
                    "La persona que añadas como amigo, podrá saber la ubicación que tengas guardada."
                binding.camposAnadir.isGone = false
                binding.editar.isGone = true
                binding.correo.setText("")
            }
            if (binding.correo.text.toString().isNotEmpty()) {
                if (binding.anadir.text.toString().equals("AÑADIR"))
                    buscarAmigo(emailUsuario, nombreUsuario)
            }
            if (!binding.selecAmigo.text.toString().equals("Ninguno")) {
                if (binding.anadir.text.toString().equals("ELIMINAR"))
                    eliminarAmigo(emailUsuario)
                else {
                    if (binding.anadir.text.toString()
                            .equals("ACTUALIZAR") && binding.nuevo.text.toString().isNotEmpty()
                    )
                        editarAmigo(
                            emailUsuario,
                            binding.nuevo.text.toString(),
                            binding.selecAmigo.text.toString()
                        )
                }
                binding.selecAmigo.text = "Ninguno"

            }

        }
        binding.editar.setOnClickListener {
            binding.titulo.text = "Editar Nombre"
            binding.anadir.text = "ACTUALIZAR"
            binding.tituloCorreo.text = "Nombre del amigo"
            binding.volver.text = "VOLVER"
            binding.texto.text =
                "Para cambiar el nombre del amigo que quieras, necesitaremos su nombre y el nuevo que quieras ponerle"
            binding.tituloCodigo.text = "Nuevo Nombre"
            binding.accionAdministrar.isGone = false
            binding.camposAdministrar2.isGone = false
            binding.editar.isGone = true

        }
        binding.volver.setOnClickListener {


            if (binding.volver.text.toString().equals("VOLVER")) {
                val intent = Intent(this, Aplicacion::class.java).apply {
                    putExtra("email", emailUsuario)
                }
                startActivity(intent)
            } else {
                binding.titulo.text = "Borrar Amigo"
                binding.anadir.text = "ELIMINAR"
                binding.tituloCorreo.text = "Nombre del amigo"
                binding.parteCodigo.isGone = true
                binding.volver.text = "VOLVER"
                binding.texto.text =
                    "La persona que elimines como amigo, ya no podrá saber la ubicación que tengas guardada."
                binding.accionAdministrar.isGone = false
                binding.actualizar.isGone = true
                binding.camposAdministrar2.isGone = false
                binding.editar.isGone = true

                Log.v("mensaje", binding.anadir.text.toString())
            }
        }
    }

    override fun onAmigoClick(amigo: Amigo, longAmigo: Double, latAmigo: Double, lugar: TextView) {
        if (!binding.anadir.text.toString().equals("AÑADIR")) {
            binding.correo.setText(amigo.getCorreo())
            binding.selecAmigo.text = amigo.getNombre()
        }
    }


    private fun refrescar(emailUsuario: String?) {
        db.collection("Usuarios").document(emailUsuario.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null) {

                        binding.listaAmigos.adapter = AmigosAdapter(usuario.getAmigos(), this)
                        val divider = DividerItemDecoration(
                            binding.listaAmigos.context,
                            DividerItemDecoration.VERTICAL
                        )
                        binding.listaAmigos.addItemDecoration(divider)

                    }
                }
            }
    }

    private fun editarAmigo(emailUsuario: String?, nombreNuevo: String, nombreViejo: String) {
        db.collection("Usuarios").document(emailUsuario.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null) {
                        usuario.getAmigos()[usuario.encontrarAmigoCorreo(binding.correo.text.toString())].setNombre(
                            nombreNuevo
                        )
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)
                        val text = "Hemos cambiado el nombre de $nombreViejo a $nombreNuevo"
                        val duration = Toast.LENGTH_SHORT
                        val toast =
                            Toast.makeText(applicationContext, text, duration)
                        toast.show()
                        refrescar(emailUsuario)
                    }
                }
            }
    }

    private fun eliminarAmigo(emailUsuario: String?) {
        var nombreAmigo = binding.selecAmigo.text.toString()

        Log.v("Nombre Amigo", nombreAmigo)
        if (emailUsuario != null) {
            db.collection("Usuarios").document(emailUsuario)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        val usuario = it.toObject(Usuario::class.java)

                        if (usuario != null) {

                                val emailAmigo = binding.correo.text.toString()
                                if (usuario.delAmigo(nombreAmigo, emailAmigo)) {
                                    db.collection("Usuarios").document(usuario.getCorreo())
                                        .set(usuario)
                                    val text = "Eliminado de tu lista de amigos"
                                    val duration = Toast.LENGTH_SHORT
                                    val toast =
                                        Toast.makeText(applicationContext, text, duration)
                                    toast.show()

                                    Log.v("emailAmigo", emailAmigo)
                                    if (emailAmigo != "error") {
                                        deleteAmigo(
                                            emailAmigo,
                                            usuario.getNombre(),
                                            usuario.getCorreo()
                                        )
                                        refrescar(emailUsuario)
                                    } else {
                                        val text = "Algo falla"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast =
                                            Toast.makeText(applicationContext, text, duration)
                                        toast.show()
                                    }
                                } else {
                                    val text = "No lo tienes como amigo"
                                    val duration = Toast.LENGTH_SHORT
                                    val toast =
                                        Toast.makeText(applicationContext, text, duration)
                                    toast.show()
                                }


                        } else {
                            val text = "No existe nuestro usuario"
                            val duration = Toast.LENGTH_SHORT
                            val toast =
                                Toast.makeText(applicationContext, text, duration)
                            toast.show()
                        }
                    } else {
                        val text = "No existe nuestro usuario"
                        val duration = Toast.LENGTH_SHORT
                        val toast =
                            Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }

                }
        }


    }

    private fun deleteAmigo(emailAmigo: String, nombreUsuario: String, emailUsuario: String?) {


        db.collection("Usuarios").document(emailAmigo)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null) {
                        if (emailUsuario != null) {
                            usuario.delAmigo(nombreUsuario, emailUsuario)
                        }
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)

                        Log.v("usuario", usuario.getAmigos().toString())
                    }
                }
            }

    }

    private fun buscarAmigo(
        emailUsuario: String?,
        nombreUsuario: String?,
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

                                var amigo = Amigo("")

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
                                        crearChat(nombreUsuario,usuario.getNombre(),emailUsuario,emailAmigo)
                                        amigo.setChatId(ChatId)
                                        usuario.getAmigos().add(amigo)
                                        db.collection("Usuarios").document(usuario.getCorreo())
                                            .set(usuario)
                                        val text = "Añadido a tu lista de amigos"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast =
                                            Toast.makeText(applicationContext, text, duration)
                                        toast.show()
                                        amigo.setNombre(usuario.getNombre())
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
                    } else {
                        val text = "Correo o código incorrectos"
                        val duration = Toast.LENGTH_SHORT
                        val toast =
                            Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }
                } else {
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

                    if (usuario != null) {
                        usuario.getAmigos().add(amigo)
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)
                        refrescar(emailUsuario)


                        Log.v("usuario", usuario.getAmigos().toString())
                    }
                }
            }
    }

    private fun crearChat(
        nombreUsuario: String,
        nombreAmigo: String,
        emailUsuario: String?,
        emailAmigo: String
    ) {
        val chatId = UUID.randomUUID().toString()
        val users = listOf(nombreUsuario, nombreAmigo)

        ChatId = chatId
        var chat = Chats(
            id = chatId,
            nombre = "Chat de $nombreUsuario y $nombreAmigo",
            usuarios = users
        )

        db.collection("chats").document(chatId).set(chat)
        chat.nombre = "Chat con $nombreAmigo"
        if (emailUsuario != null) {
            db.collection("Usuarios").document(emailUsuario).collection("chats").document(chatId).set(chat)
        }
        chat.nombre = "Chat con $nombreUsuario"
        db.collection("Usuarios").document(emailAmigo).collection("chats").document(chatId).set(chat)

    }
}