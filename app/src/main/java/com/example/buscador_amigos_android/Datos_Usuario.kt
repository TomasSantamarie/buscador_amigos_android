package com.example.buscador_amigos_android

import java.io.Serializable

class Usuario(private var correo:String): Serializable{
    private var nombre =""
    private var codigo = ""
    private var ubicacion =""
    private var amigos =ArrayList<Amigo>()
    private var latitud = 0.0
    private var longitud = 0.0

    constructor() : this("")

    fun getLongitud(): Double{
        return longitud
    }
    fun getLatitud(): Double{
        return latitud
    }
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
    fun setLatitud(lat: Double){
        this.latitud = lat
    }
    fun setLongitud(long: Double){
        this.longitud = long
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


    fun encontrarAmigo(nombre: String):String{
        for((indice, item) in amigos.withIndex()){
            if (item.getNombre() == nombre) {
                return item.getCorreo()
            }
        }
        return "error"
    }
    fun encontrarAmigoCorreo(correo: String): Int {
        for((indice, item) in amigos.withIndex()){
            if (item.getCorreo() == correo) {
                return indice
            }
        }
        return 10
    }

    fun delAmigo(nombre: String, correo: String): Boolean {
        for((indice, item) in amigos.withIndex()){
            if (item.getNombre() == nombre && item.getCorreo() == correo) {
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

class Amigo(private var nombre:String):Serializable{
    private var correo = ""
    constructor() : this("")
    fun getNombre(): String{
        return nombre
    }
    fun getCorreo(): String{
        return correo
    }
    fun setNombre(nombre: String){
        this.nombre = nombre
    }


    fun setCorreo(correo: String){
        this.correo = correo
    }

}