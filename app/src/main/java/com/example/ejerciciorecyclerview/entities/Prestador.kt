package com.example.ejerciciorecyclerview.entities

import android.os.Parcel
import android.os.Parcelable

class Prestador (apellido: String, app_role :String, nombre: String, rubro: String):
    Parcelable
{
    var apellido: String
    var app_role: String
    var nombre : String
    var rubro : String

    constructor() : this("","","","")

    init{
        this.apellido = apellido!!
        this.app_role = app_role!!
        this.nombre = nombre!!
        this.rubro = rubro!!
    }

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(apellido)
        writeString(app_role)
        writeString(nombre)
        writeString(rubro)
    }

    override fun toString(): String {
        return "Prestador(apellido='$apellido', App_Role='$app_role', nombre='$nombre', rubro = $rubro)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Prestador> = object : Parcelable.Creator<Prestador> {
            override fun createFromParcel(source: Parcel): Prestador = Prestador(source)
            override fun newArray(size: Int): Array<Prestador?> = arrayOfNulls(size)
        }
    }
}