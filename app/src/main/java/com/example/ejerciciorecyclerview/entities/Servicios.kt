package com.example.ejerciciorecyclerview.entities

import com.google.firebase.firestore.GeoPoint

class Servicio(descripcion: String, precio: Double, rubro: String, geolocalizacion: GeoPoint?, prestador: String)
{
    var descripcion: String
    var precio: Double
    var rubro : String
    var geolocalizacion : GeoPoint
    var prestador : String
    var aceptado :  Boolean


    constructor() : this("",0.0,"", GeoPoint(0.0,0.0),"")

    init{
        this.descripcion = descripcion!!
        this.precio = precio!!
        this.rubro = rubro!!
        this.geolocalizacion = geolocalizacion!!
        this.prestador = prestador!!
        this.aceptado = false
    }

    override fun toString(): String {
        return "Prestador(Descripcion='$descripcion', Precio='$precio', rubro='$rubro', Localizacion: $geolocalizacion), Prestador: $prestador"
    }

}