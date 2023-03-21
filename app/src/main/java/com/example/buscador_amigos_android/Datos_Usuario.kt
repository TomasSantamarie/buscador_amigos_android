package com.example.buscador_amigos_android

import java.io.Serializable

class Usuario(private var correo:String): Serializable{
    private var nombre =""
    private var codigo = ""
    private var ubicacion =""
    private var amigos =ArrayList<Amigo>()
    constructor() : this("")

    fun getCorreo(): String{
        return correo
    }
    fun getNombre(): String{
        return nombre
    }
    fun getCodigo(): String{
        return codigo
    }
    fun getUbicacion():String{
        return ubicacion
    }
    fun getAmigos() : ArrayList<Amigo> {
        return amigos
    }

    fun setCorreo(correo: String){
        this.correo = correo
    }
    fun setNombre(nombre: String){
        this.nombre = nombre
    }
    fun setCodigo(codigo : String){
        this.codigo = codigo
    }
    fun setUbicacion(ubicacion: String){
        this.ubicacion = ubicacion
    }

    fun setAmigos(amigos: ArrayList<Amigo>){
        this.amigos = amigos
    }
    fun addAmigo(amigo: Amigo){
        amigos.add(amigo)
    }

    fun delAmigo(nombre: String): Boolean {
        for((indice, item) in amigos.withIndex()){
            if (item.getNombre() == nombre) {
                amigos.removeAt(indice)
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "[Usuario { Nombre $nombre, Correo $correo, Codigo $codigo, Ubicacion $ubicacion} ]"
    }
}

class Amigo(private var nombre:String, private var ubicacion:String):Serializable{
    constructor() : this("", "")
    fun getNombre(): String{
        return nombre
    }
    fun getUbicacion(): String{
        return ubicacion
    }
    fun setNombre(nombre: String){
        this.nombre = nombre
    }
    fun setUbicacion(ubicacion: String){
        this.ubicacion = ubicacion
    }
}