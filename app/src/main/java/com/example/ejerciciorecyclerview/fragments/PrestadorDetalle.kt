package com.example.ejerciciorecyclerview.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ejerciciorecyclerview.R
import com.google.firebase.firestore.GeoPoint


class PrestadorDetalle : Fragment() {

    companion object {
        fun newInstance() = PrestadorDetalle()
    }

    lateinit var v : View
    lateinit var fullName : String
    lateinit var geoLocalization : String
    lateinit var btnContact : Button
    lateinit var txtName : TextView
    lateinit var txtGeo : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_prestador_detalle, container, false)

        fullName = PrestadorDetalleArgs.fromBundle(requireArguments()).fullName
        geoLocalization = PrestadorDetalleArgs.fromBundle(requireArguments()).geoLocalization

        txtName = v.findViewById(R.id.fullName)
        txtGeo = v.findViewById(R.id.geoLoc)
        btnContact = v.findViewById(R.id.btnContact)
        var phone = PrestadorDetalleArgs.fromBundle(requireArguments()).phone


        btnContact.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+54$phone"

            val openWhatsappIntent = Intent(Intent.ACTION_VIEW)
            openWhatsappIntent.data = Uri.parse(url)
            startActivity(openWhatsappIntent)
        }

        txtName.text = fullName
        txtGeo.text = geoLocalization

        return v
    }

}