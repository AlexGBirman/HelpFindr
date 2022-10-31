package com.example.ejerciciorecyclerview.entities

import com.google.firebase.firestore.GeoPoint

class Servicio(descripcion: String, precio: Double, rubro: String, geolocalizacion: GeoPoint?, prestador: String, cliente: User)
{
    var descripcion: String
    var precio: Double
    var rubro : String
    var geolocalizacion : GeoPoint
    var prestador : String
    var aceptado :  Boolean
    var cliente : User


    constructor() : this("",0.0,"", GeoPoint(0.0,0.0),"", User("",0.0,"",""))

    init{
        this.descripcion = descripcion!!
        this.precio = precio!!
        this.rubro = rubro!!
        this.geolocalizacion = geolocalizacion!!
        this.prestador = prestador!!
        this.aceptado = false
        this.cliente = cliente!!
    }

    override fun toString(): String {
        return "Prestador(Descripcion='$descripcion', Precio='$precio', rubro='$rubro', Localizacion: $geolocalizacion), Prestador: $prestador, Cliente: ${cliente.name}"
    }

}