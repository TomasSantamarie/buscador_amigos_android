package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscador_amigos_android.databinding.ActivityAplicacionBinding
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
    }

    private fun initViews(){
        binding.messagesRecylerView.layoutManager = LinearLayoutManager(this)
        binding.messagesRecylerView.adapter = MessageAdapter(usuario)

        binding.sendMessageButton.setOnClickListener { sendMessage() }

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun sendMessage(){
        val message = Message(
            message = binding.messageTextField.text.toString(),
            from = usuario
        )

        db.collection("chats").document(chatId).collection("messages").document().set(message)

        binding.messageTextField.setText("")


    }
}