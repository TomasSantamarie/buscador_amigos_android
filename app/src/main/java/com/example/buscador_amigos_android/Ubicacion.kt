package com.example.buscador_amigos_android


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscador_amigos_android.databinding.ActivityUbicacionBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Ubicacion : AppCompatActivity(), OnAmigoClickListener {
    private lateinit var binding: ActivityUbicacionBinding
    private lateinit var localizacionUsuario: FusedLocationProviderClient
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbicacionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        localizacionUsuario = LocationServices.getFusedLocationProviderClient(this)

        binding.listaAmigos.layoutManager = LinearLayoutManager(this)

        db.collection("Usuarios").document(email.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    val usuario = it.toObject(Usuario::class.java)

                    if (usuario != null) {

                        binding.listaAmigos.adapter = AmigosAdapter(usuario.getAmigos(),this)
                        val divider = DividerItemDecoration(binding.listaAmigos.context, DividerItemDecoration.VERTICAL)
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

        binding.guardar.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1)
            } else {
                localizacionUsuario.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                    override fun isCancellationRequested() = false
                })
                    .addOnSuccessListener(this) { location ->
                        if (location != null) {

                            // Obtiene la ciudad a partir de la ubicación actual
                            val geocoder = Geocoder(this, Locale.getDefault())

                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)

                            val city = addresses?.get(0)?.locality

                            if (city != null) {
                                Log.v("Ciudad",city)
                            } else {
                                Log.v("Ciudad","No esta")

                            }
                            db.collection("Usuarios").document(email.toString())
                                .get()
                                .addOnSuccessListener {
                                    if (it != null) {
                                        val usuario = it.toObject(Usuario::class.java)

                                        if (usuario != null) {
                                            if (city != null) {
                                                usuario.setUbicacion(city)
                                                if (email != null) {
                                                    db.collection("Usuarios").document(email).set(usuario)
                                                    val text = "Ubicación: $city guardada!"
                                                    val duration = Toast.LENGTH_SHORT
                                                    val toast = Toast.makeText(applicationContext, text, duration)
                                                    toast.show()
                                                    //reescribir(usuario.getAmigos(),city,usuario.getNombre())
                                                }

                                            }
                                        }
                                    }
                                }
                        }
                    }.addOnFailureListener {
                        Log.d(".EXE", it.message!!)

                    }.addOnCanceledListener {
                        Log.d("Loc", "Cancelado")

                    }
            }
        }


    }

    override fun onAmigoClick(amigo: Amigo) {
        TODO("Not yet implemented")
    }

    /*
    private fun reescribir(amigos: ArrayList<Amigo>, city: String, nombre: String) {

        var cant = amigos.size
        for (i in 1..cant){
            db.collection("Usuarios").document(amigos[i-1].getCorreo())
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        val usuario = it.toObject(Usuario::class.java)
                        if (usuario != null) {
                            var aux = usuario.posicionAmigo(nombre)
                            if (aux > 0) {
                                usuario.getAmigos()[aux].setUbicacion(city)
                                db.collection("Usuarios").document(amigos[i-1].getCorreo()).set(usuario)
                            }else
                                Log.v("No encontrado","En la lista de amigos de ${usuario.getNombre()} no se ha encontrado a $nombre")
                        }
                    }
                }
        }
    }
    */

}
