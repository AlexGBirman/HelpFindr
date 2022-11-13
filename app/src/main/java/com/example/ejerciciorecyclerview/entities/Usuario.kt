package com.example.ejerciciorecyclerview.entities

import android.content.Context
import android.location.Geocoder
import com.google.firebase.firestore.GeoPoint
import java.util.*
import kotlin.collections.ArrayList


class Usuario(nombre: String, apellido: String, geolocalizacion: GeoPoint, phone: String, contrataciones: ArrayList<Servicio>, puntajes : ArrayList<Float>) {
    var nombre : String
    var apellido : String
    var geolocalizacion : GeoPoint
    var phone : String
    var contrataciones : ArrayList<Servicio>
    var puntajes : ArrayList<Float>
    var puntajeTotal : Float

    constructor() : this("","", GeoPoint(1.0,1.0),"", arrayListOf(), arrayListOf())

    init {
        this.nombre = nombre!!
        this.apellido = apellido!!
        this.geolocalizacion = geolocalizacion!!
        this.phone = phone!!
        this.contrataciones = contrataciones!!
        this.puntajes = puntajes!!
        this.puntajeTotal = calcPuntajeTotal(this.puntajes)
    }

    private fun calcPuntajeTotal(listaPuntajes:ArrayList<Float>) : Float{
        var sumaTotal = 0.0.toFloat()

        if(listaPuntajes.isNotEmpty()){
            sumaTotal = listaPuntajes.sum()/listaPuntajes.size
        }

        return sumaTotal
    }

    fun agregarPuntaje(punto: Float){
        this.puntajes.add(punto)
        this.puntajeTotal = this.calcPuntajeTotal(this.puntajes)
    }

    fun getDireccion(context: Context): String? {
        var geocoder = Geocoder(context, Locale.getDefault())

        var adresses = geocoder.getFromLocation(geolocalizacion.latitude, geolocalizacion.longitude, 1)

        return adresses[0].getAddressLine(0)


    }

}