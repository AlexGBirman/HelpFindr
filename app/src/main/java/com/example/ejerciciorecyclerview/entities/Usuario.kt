package com.example.ejerciciorecyclerview.entities

import android.content.Context
import android.location.Geocoder
import com.google.firebase.firestore.GeoPoint
import java.util.*


class Usuario(nombre: String, apellido: String, geolocalizacion: GeoPoint, phone: String, contrataciones: List<Servicio>) {
    var nombre : String
    var apellido : String
    var geolocalizacion : GeoPoint
    var phone : String
    var contrataciones : List<Servicio>

    constructor() : this("","", GeoPoint(1.0,1.0),"", arrayListOf())

    init {
        this.nombre = nombre!!
        this.apellido = apellido!!
        this.geolocalizacion = geolocalizacion!!
        this.phone = phone!!
        this.contrataciones = contrataciones!!
    }

    fun getDireccion(context: Context): String? {
        var geocoder = Geocoder(context, Locale.getDefault())

        var adresses = geocoder.getFromLocation(geolocalizacion.latitude, geolocalizacion.longitude, 1)

        return adresses[0].getAddressLine(0)


    }

}