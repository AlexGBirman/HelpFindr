package com.example.ejerciciorecyclerview.entities

import com.google.firebase.firestore.GeoPoint

class Prestador(apellido: String, app_role:String, nombre: String, rubro: String, geolocalizacion: GeoPoint?, phone: String?, trabajos: ArrayList<Servicio>)
{
    var apellido: String
    var app_role: String
    var nombre : String
    var rubro : String
    var geolocalizacion : GeoPoint
    var phone : String
    var trabajos : ArrayList<Servicio>


    constructor() : this("","","","", GeoPoint(0.0,0.0),"", arrayListOf())

    init{
        this.apellido = apellido!!
        this.app_role = app_role!!
        this.nombre = nombre!!
        this.rubro = rubro!!
        this.geolocalizacion = geolocalizacion!!
        this.phone = phone!!
        this.trabajos = trabajos!!

    }

    override fun toString(): String {
        return "Prestador(apellido='$apellido', App_Role='$app_role', nombre='$nombre', rubro = $rubro, Localizacion: $geolocalizacion), Phone: $phone"
    }

}