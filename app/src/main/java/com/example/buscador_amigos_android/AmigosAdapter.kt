package com.example.buscador_amigos_android

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text


class AmigosAdapter(var amigos: ArrayList<Amigo>) : RecyclerView.Adapter<AmigosAdapter.ViewHolder>() {

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
        holder.agregar(amigo)
        Log.d("ADD", amigo.getCorreo())
    }

    override fun getItemCount(): Int = amigos.size



    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        private val nombre =  view.findViewById<TextView>(R.id.nombre_ListaAmigo)
        private val lugar =  view.findViewById<TextView>(R.id.ubicacion_ListaAmigo)
        fun agregar(amigo: Amigo) {
            nombre.text = amigo.getNombre()
            lugar.text = amigo.getUbicacion()

            view.setOnClickListener {
                Log.d("CLICK", amigo.getCorreo())
            }
        }

    }
}