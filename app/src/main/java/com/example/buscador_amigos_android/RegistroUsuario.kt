package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class RegistroUsuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        findViewById<EditText>(R.id.usuarioNoValido).isVisible = false
        findViewById<EditText>(R.id.minCaracteres).isVisible = false
        findViewById<EditText>(R.id.noCoinciden).isVisible = false

        findViewById<Button>(R.id.login2).setOnClickListener { acceder() }

        findViewById<Button>(R.id.crearCuenta2).setOnClickListener { cambioPagina() }

        findViewById<EditText>(R.id.contrasena1).addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (findViewById<EditText>(R.id.contrasena1).text.toString().length > 8) {
                    findViewById<EditText>(R.id.minCaracteres).isVisible = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })

        findViewById<EditText>(R.id.contrasena1).addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (findViewById<EditText>(R.id.contrasena1).text.toString().length < 8) {
                    findViewById<EditText>(R.id.minCaracteres).isVisible = true
                    findViewById<EditText>(R.id.contrasena1).setBackgroundResource(R.drawable.borde_edittext_mal)
                } else {
                    findViewById<EditText>(R.id.contrasena1).setBackgroundResource(R.drawable.borde_edittext_bien)
                    findViewById<EditText>(R.id.minCaracteres).isVisible = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })

        findViewById<EditText>(R.id.contrasena2).addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!findViewById<EditText>(R.id.contrasena1).text.toString().equals(findViewById<EditText>(R.id.contrasena2).text.toString())) {
                    findViewById<EditText>(R.id.noCoinciden).isVisible = true
                    findViewById<EditText>(R.id.contrasena2).setBackgroundResource(R.drawable.borde_edittext_mal)
                } else {
                    findViewById<EditText>(R.id.contrasena2).setBackgroundResource(R.drawable.borde_edittext_bien)
                    findViewById<EditText>(R.id.noCoinciden).isVisible = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} })
    }

    private fun acceder() {

        val email = findViewById<EditText>(R.id.user2).text
        val pss = findViewById<EditText>(R.id.contrasena1).text
        findViewById<Button>(R.id.login2).setOnClickListener {
            if (email.isNotEmpty() && pss.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.toString(),
                        pss.toString()).addOnCompleteListener {

                    if (it.isSuccessful){
                        cambioPagina2(it.result?.user?.email ?:"", pss.toString())
                    }else {
                        alerta()
                        findViewById<EditText>(R.id.usuarioNoValido).isVisible = true
                    }
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