package com.example.buscador_amigos_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isGone
import com.example.buscador_amigos_android.databinding.ActivityCuentaBinding
import com.google.firebase.firestore.FirebaseFirestore

class Cuenta : AppCompatActivity() {
    private lateinit var binding: ActivityCuentaBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        binding.alertaNombre.isGone = true
        binding.alertaCodigo.isGone = true
        val bundle = intent.extras
        val nombre = bundle?.getString("nombre")
        val codigo = bundle?.getString("codigo")
        val email = bundle?.getString("email")
        val ubicacion = bundle?.getString("ubicacion")

        if (nombre != null || codigo != null || email != null) {
            binding.nombre.setText(nombre)
            binding.correo.setText(email)
            binding.codigo.setText(codigo)
            if (ubicacion != null) {
                if (ubicacion.length > 1)
                    binding.ubicacion.setText(ubicacion)
            }


        }


        binding.nombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.alertaNombre.isGone = binding.nombre.text.toString().length >= 4

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.codigo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.alertaCodigo.isGone = binding.codigo.text.toString().length >= 8
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.guardar.setOnClickListener {

            if ((binding.codigo.text.toString().length >= 8) and (binding.nombre.text.toString().length >= 4)) {
                db.collection("Usuarios").document(email.toString())
                    .get()
                    .addOnSuccessListener {
                        if (it != null) {
                            val usuario = it.toObject(Usuario::class.java)

                            usuario?.setNombre(binding.nombre.text.toString())
                            usuario?.setCodigo(binding.codigo.text.toString())
                            if (usuario != null && email != null) {
                                db.collection("Usuarios").document(email).set(usuario)
                                val text = "Cambios guardados!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(applicationContext, text, duration)
                                toast.show()
                            } else {
                                val text = "Los cambios no se pudieron guardar"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(applicationContext, text, duration)
                                toast.show()
                            }

                        }
                    }
            } else {
                val text = "Faltan requisitos"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }

        }

        binding.volver.setOnClickListener {
            val intent = Intent(this, Aplicacion::class.java).apply {
                putExtra("email", email)
            }
            startActivity(intent)
        }
    }


}