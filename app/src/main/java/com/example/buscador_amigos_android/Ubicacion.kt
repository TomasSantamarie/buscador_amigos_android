package com.example.buscador_amigos_android


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
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
    private var longitud: Double = 0.0
    private var latitud: Double = 0.0
    private var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbicacionBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        val view = binding.root
        setContentView(view)

        binding.alerta.isGone = true
        val bundle = intent.extras
        email = bundle?.getString("email").toString()
        localizacionUsuario = LocationServices.getFusedLocationProviderClient(this)

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
                        longitud = usuario.getLongitud()
                        latitud = usuario.getLatitud()
                        if (usuario.getUbicacion().length < 1) {
                            Log.v("Sin ubicacion", "Si pasa")
                            binding.alerta.isGone = false
                            binding.listaAmigos.isGone = true
                        }

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
            binding.alerta.isGone = false
            if (ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1)
            } else {
                localizacionUsuario.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                            CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    }).addOnSuccessListener(this) { location ->
                    if (location != null) {

                        // Obtiene la ciudad a partir de la ubicación actual
                        val geocoder = Geocoder(this, Locale.getDefault())

                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        longitud = location.longitude
                        Log.v("Long", longitud.toString())
                        latitud = location.latitude
                        Log.v("Lat", latitud.toString())
                        val city = addresses?.get(0)?.locality

                        if (city != null) {
                            Log.v("Ciudad", city)

                            db.collection("Usuarios").document(email)
                                .get()
                                .addOnSuccessListener {
                                    if (it != null) {
                                        val usuario = it.toObject(Usuario::class.java)
                                        if (usuario != null) {
                                            usuario.setUbicacion(city)
                                            usuario.setLatitud(latitud)
                                            Log.v("Long", longitud.toString())
                                            usuario.setLongitud(longitud)
                                            Log.v("Lat", latitud.toString())
                                            db.collection("Usuarios").document(email).set(usuario)
                                            if ((usuario.getUbicacion().length > 1)) {
                                                Log.v("Con ubicacion", "Si pasa")
                                                binding.alerta.isGone = true
                                                binding.listaAmigos.isGone = false
                                            }

                                            val text = "Ubicación: $city guardada!"
                                            val duration = Toast.LENGTH_SHORT
                                            val toast =
                                                Toast.makeText(applicationContext, text, duration)
                                            toast.show()

                                        }
                                    }
                                }
                        } else {
                            Log.v("Ciudad", "No esta")

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

    override fun onAmigoClick(amigo: Amigo, longAmigo: Double, latAmigo: Double, lugar: TextView) {

        if (lugar.length()>1 && !longAmigo.equals(longitud) && !latAmigo.equals(latitud)){
            val intent = Intent(this, Mapa::class.java).apply {
                putExtra("email", email)
                putExtra("LongUsuario", longitud)
                Log.d("LongUsuario", longitud.toString())
                putExtra("LatUsuario", latitud)
                Log.d("LatUsuario", latitud.toString())
                putExtra("LongAmigo", longAmigo)
                Log.d("LongAmigo", longAmigo.toString())
                putExtra("LatAmigo", latAmigo)
                Log.d("LatAmigo", latAmigo.toString())
            }
            startActivity(intent)
        } else{
            if (longAmigo.equals(longitud) && latAmigo.equals(latitud)){
                val text = "${amigo.getNombre()} y Tú estáis en el mismo lugar"
                val duration = Toast.LENGTH_SHORT
                val toast =
                    Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else {
                val text = "${amigo.getNombre()} no ha guardado su ubicación!!"
                val duration = Toast.LENGTH_SHORT
                val toast =
                    Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
    }


}
