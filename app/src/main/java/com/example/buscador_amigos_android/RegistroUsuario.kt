package com.example.buscador_amigos_android

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.buscador_amigos_android.databinding.ActivityMainBinding
import com.example.buscador_amigos_android.databinding.ActivityRegistroUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroUsuarioBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.usuarioNoValido.isVisible = false
        binding.minCaracteres.isVisible = false
        binding.noCoinciden.isVisible = false
        binding.login2.isEnabled = false

        binding.login2.setOnClickListener { acceder() }

        binding.crearCuenta2.setOnClickListener { cambioPagina() }

        binding.contrasena1.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.contrasena1.text.toString().length > 8) {
                    binding.minCaracteres.isVisible = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })

        binding.contrasena1.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.contrasena1.text.toString().length < 8) {
                    binding.minCaracteres.isVisible = true
                    binding.contrasena1.setBackgroundResource(R.drawable.borde_edittext_mal)
                } else {
                    binding.contrasena1.setBackgroundResource(R.drawable.borde_edittext_bien)
                    binding.minCaracteres.isVisible = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })

        binding.contrasena2.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!binding.contrasena1.text.toString().equals(findViewById<EditText>(R.id.contrasena2).text.toString())) {
                    binding.noCoinciden.isVisible = true
                    binding.login2.isEnabled = false
                    binding.contrasena2.setBackgroundResource(R.drawable.borde_edittext_mal)
                } else {
                    binding.contrasena2.setBackgroundResource(R.drawable.borde_edittext_bien)
                    binding.noCoinciden.isVisible = false
                    binding.login2.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun acceder() {

        val email = binding.user2.text
        val pss = binding.contrasena1.text

            if (email.isNotEmpty() && pss.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.toString(),
                        pss.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        var usuario = Usuario(email.toString())
                        usuario.setNombre(email.toString())
                        db.collection("Usuarios").document(usuario.getCorreo()).set(usuario)
                        cambioPagina2(it.result?.user?.email ?:"", pss.toString())
                    }else {
                        alerta()
                        binding.usuarioNoValido.isVisible = true
                    }
                }
            }

    }
    private fun cambioPagina() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun alerta(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autentificando al usuario")
        builder.setPositiveButton("Aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun cambioPagina2(email:String, pss: String){

        val registroIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("contraseña", pss)
        }
        startActivity(registroIntent)
    }
}