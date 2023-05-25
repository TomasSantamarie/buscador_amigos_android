package com.example.buscador_amigos_android

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

interface OnAmigoClickListener {
    fun onAmigoClick(amigo: Amigo, longAmigo: Double, latAmigo: Double, lugar: TextView)
}

class AmigosAdapter(var amigos: ArrayList<Amigo>, private val listener: OnAmigoClickListener) :
    RecyclerView.Adapter<AmigosAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.objeto_lista,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val amigo = amigos[position]
        holder.agregar(amigo, listener)
        Log.d("ADD", amigo.getCorreo())


    }


    override fun getItemCount(): Int = amigos.size


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val db = FirebaseFirestore.getInstance()
        private val nombre = view.findViewById<TextView>(R.id.nombre_ListaAmigo)
        private val lugar = view.findViewById<TextView>(R.id.ubicacion_ListaAmigo)
        private val email = view.findViewById<TextView>(R.id.correo_ListaAmigo)

        fun agregar(amigo: Amigo, listener: OnAmigoClickListener) {
            var longAmigo = 0.0
            var latAmigo = 0.0
            nombre.text = amigo.getNombre()
            email.text = amigo.getCorreo()
            db.collection("Usuarios").document(amigo.getCorreo())
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        val usuario = it.toObject(Usuario::class.java)

                        if (usuario != null) {
                            lugar.text = usuario.getUbicacion()

                            longAmigo = usuario.getLongitud()
                            latAmigo = usuario.getLatitud()
                        }
                    }
                }

            view.setOnClickListener {
                db.collection("Usuarios").document(amigo.getCorreo())
                    .get()
                    .addOnSuccessListener {
                        if (it != null) {
                            val usuario = it.toObject(Usuario::class.java)
                            if (usuario != null) {
                                longAmigo = usuario.getLongitud()
                                latAmigo = usuario.getLatitud()
                            }
                        }
                    }
                listener.onAmigoClick(amigo, longAmigo, latAmigo,lugar)
                Log.d("CLICK", amigo.getCorreo())

            }
        }

    }
}