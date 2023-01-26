package com.example.buscador_amigos_android

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buscador_amigos_android.databinding.ActivityAplicacionBinding
import com.example.buscador_amigos_android.databinding.ActivityRegistroUsuarioBinding

class Aplicacion : AppCompatActivity() {

    private lateinit var binding: ActivityAplicacionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAplicacionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}