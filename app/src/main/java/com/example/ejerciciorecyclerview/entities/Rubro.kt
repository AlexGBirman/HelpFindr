package com.example.ejerciciorecyclerview.entities

import android.os.Parcel
import android.os.Parcelable

class Rubro (cantPrest: Int, rubro :String):
    Parcelable
{
    var cantPrest : Int
    var rubro : String

    constructor() : this(0,"")

    init{
        this.cantPrest = cantPrest!!
        this.rubro = rubro!!
    }

    constructor(source: Parcel) : this(
        source.readInt()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(cantPrest)
        writeString(rubro)
    }

    override fun toString(): String {
        return "Prestador(Cantidad Prestadores = $cantPrest, rubro = $rubro)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Rubro> = object : Parcelable.Creator<Rubro> {
            override fun createFromParcel(source: Parcel): Rubro = Rubro(source)
            override fun newArray(size: Int): Array<Rubro?> = arrayOfNulls(size)
        }
    }
}