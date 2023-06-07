package com.example.buscador_amigos_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class MensajeAdapter(private val user: String): RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {

    private var mensajes: List<Mensaje> = emptyList()

    fun setData(list: List<Mensaje>){
        mensajes = ArrayList(list)
        //messages.forEach { Log.d("Message", it.toString()) }
        //Log.d("Message", "---------------------------------------------------")
        notifyDataSetChanged()
        //notifyItemRangeChanged(0, messages.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        return MensajeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.objeto_mensaje,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val message = mensajes[position]

        if(user == message.from){
            //Log.d("CHANGE", "${message.from} ${message.mensaje} changed to OWN")
            holder.itemView.findViewById<ConstraintLayout>(R.id.miMensajeLayout).visibility = View.VISIBLE
            holder.itemView.findViewById<ConstraintLayout>(R.id.suMensajeLayout).visibility = View.GONE
            holder.itemView.findViewById<TextView>(R.id.miMensajeTextView).text = message.mensaje
        } else {
            //Log.d("CHANGE", "${message.from} ${message.mensaje} changed to OTHER")
            holder.itemView.findViewById<ConstraintLayout>(R.id.miMensajeLayout).visibility = View.GONE
            holder.itemView.findViewById<ConstraintLayout>(R.id.suMensajeLayout).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.suMensajeTextView).text = message.mensaje
        }

    }

    override fun getItemCount(): Int {
        return mensajes.size
    }

    class MensajeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}