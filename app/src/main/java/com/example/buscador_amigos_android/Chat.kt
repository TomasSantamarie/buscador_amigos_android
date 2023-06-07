package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscador_amigos_android.databinding.ActivityChatBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Chat : AppCompatActivity() {
    private var chatId = ""
    private var usuario = ""
    private var db = Firebase.firestore
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        val view = binding.root
        setContentView(view)

        val bundle = intent.extras
        usuario = bundle?.getString("email").toString()
        chatId = bundle?.getString("chatId").toString()
        var nombreAmigo = bundle?.getString("nombreAmigo").toString()

        binding.tituloAmigo.text = "Chat con " + nombreAmigo

        if(chatId.isNotEmpty() && usuario.isNotEmpty()) {
            initViews()
        }
        binding.volver.setOnClickListener {
            val intent = Intent(this, ListaChat::class.java).apply {
                putExtra("email", usuario)
            }
            startActivity(intent)
        }


        binding.sendMensajeButton.setOnClickListener { enviarMensaje() }
    }

    private fun initViews(){
        binding.mensajesRecylerView.layoutManager = LinearLayoutManager(this)
        binding.mensajesRecylerView.adapter = MensajeAdapter(usuario)


        val chatRef = db.collection("chats").document(chatId)
        chatRef.collection("mensajes").orderBy("fecha", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { mensajes ->
                val listMessages = mensajes.toObjects(Mensaje::class.java)
                (binding.mensajesRecylerView.adapter as MensajeAdapter).setData(listMessages)
            }

        chatRef.collection("mensajes").orderBy("fecha", Query.Direction.ASCENDING)
            .addSnapshotListener { mensajes, error ->
                if(error == null){
                    mensajes?.let {
                        val listMessages = it.toObjects(Mensaje::class.java)
                        (binding.mensajesRecylerView.adapter as MensajeAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun enviarMensaje(){
        if (binding.mensajeTextField.text.toString().isNotEmpty()){
            val mensaje = Mensaje(
                mensaje = binding.mensajeTextField.text.toString(),
                from = usuario
            )

            db.collection("chats").document(chatId).collection("mensajes").document().set(mensaje)

            binding.mensajeTextField.setText("")
        }

    }
}