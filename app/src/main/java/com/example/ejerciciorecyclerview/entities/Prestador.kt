package com.example.ejerciciorecyclerview.entities

import com.google.firebase.firestore.GeoPoint

class Prestador(apellido: String, app_role:String, nombre: String, rubro: String, geolocalizacion: GeoPoint?, phone: String?, trabajos: ArrayList<Servicio>, puntajes : ArrayList<Float>)
{
    var apellido: String
    var app_role: String
    var nombre : String
    var rubro : String
    var geolocalizacion : GeoPoint
    var phone : String
    var trabajos : ArrayList<Servicio>
    var puntajes : ArrayList<Float>
    var puntajeTotal : Float


    constructor() : this("","","","", GeoPoint(0.0,0.0),"", arrayListOf(), arrayListOf())

    init{
        this.apellido = apellido!!
        this.app_role = app_role!!
        this.nombre = nombre!!
        this.rubro = rubro!!
        this.geolocalizacion = geolocalizacion!!
        this.phone = phone!!
        this.trabajos = trabajos!!
        this.puntajes = puntajes!!
        this.puntajeTotal = calcPuntajeTotal(this.puntajes)

    }

    private fun calcPuntajeTotal(listaPuntajes:List<Float>) : Float{
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

    override fun toString(): String {
        return "Prestador(apellido='$apellido', App_Role='$app_role', nombre='$nombre', rubro = $rubro, Localizacion: $geolocalizacion), Phone: $phone"
    }

}