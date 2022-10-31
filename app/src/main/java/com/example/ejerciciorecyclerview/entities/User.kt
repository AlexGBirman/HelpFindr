package com.example.ejerciciorecyclerview.entities

import com.google.firebase.firestore.GeoPoint

open class User (name : String,score : Double, profilePic : String?, pass : String?)
{
    val name : String
    val score : Double
    val profilePic : String
    val pass : String

    constructor() : this("",0.0,"", "")

    init {
        this.name = name!!
        this.score = score!!
        this.profilePic = profilePic!!
        this.pass = pass!!
    }

    override fun toString(): String {
        return "Nombre: $name, Score: $score"
    }


}