package com.example.buscador_amigos_android


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.buscador_amigos_android.databinding.ActivityUbicacionBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Ubicacion : AppCompatActivity() {
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
                        Log.d("Loc", "Ojo, que dice que si a dar por culo")
                        if (location != null) {
                            Log.d("Loc", "No es null el hdp")
                            // Obtiene la ciudad a partir de la ubicación actual
                            val geocoder = Geocoder(this, Locale.getDefault())
                            Log.d("Loc", "Iniciando el puto Geocoder")
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            Log.d("Loc", "Dame la puta ubicación")
                            val city = addresses?.get(0)?.locality
                            Log.d("Loc", "City de los huevos")
                            if (city != null) {
                                Log.v("Ciudad",city)
                            } else {
                                Log.d("Ciudad", "Vive en la luna, no hay ciudad")
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
                                                    val text = "Ubicación guardada!"
                                                    val duration = Toast.LENGTH_SHORT
                                                    val toast = Toast.makeText(applicationContext, text, duration)
                                                    toast.show()
                                                }

                                            }
                                        }
                                    }
                                }
                        }
                    }.addOnFailureListener {
                        Log.d("Mierda.EXE", it.message!!)
                    }.addOnCanceledListener {
                        Log.d("Loc", "Cancelame esta")
                    }
            }
        }


    }
}