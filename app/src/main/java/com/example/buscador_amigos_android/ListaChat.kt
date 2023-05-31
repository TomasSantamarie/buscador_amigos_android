package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscador_amigos_android.databinding.ActivityListaChatBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListaChat : AppCompatActivity(), OnAmigoClickListener {
    private lateinit var binding: ActivityListaChatBinding
    private val db = FirebaseFirestore.getInstance()
    private var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaChatBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        val view = binding.root
        setContentView(view)

        val bundle = intent.extras
        email = bundle?.getString("email").toString()

        binding.listaAmigos.layoutManager = LinearLayoutManager(this)

        db.collection("Usuarios").document(email.toString())
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
        binding.volver.setOnClickListener {
            val intent = Intent(this, Aplicacion::class.java).apply {
                putExtra("email", email)
            }
            startActivity(intent)
        }


    }

    override fun onAmigoClick(amigo: Amigo, longAmigo: Double, latAmigo: Double, lugar: TextView) {
        val intent = Intent(this, Chat::class.java).apply {
            putExtra("email", email)
            putExtra("chatId",amigo.getChatId())
            putExtra("nombreAmigo",amigo.getNombre())
        }
        startActivity(intent)
    }

}
