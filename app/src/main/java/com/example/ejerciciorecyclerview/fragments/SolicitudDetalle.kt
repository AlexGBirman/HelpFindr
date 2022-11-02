package com.example.ejerciciorecyclerview.fragments

import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SolicitudDetalle : Fragment() {

    lateinit var v : View
    lateinit var txtName: TextView
    lateinit var txtScore : TextView
    lateinit var txtDireccion : TextView
    lateinit var txtPrecio : TextView
    lateinit var txtDescripcion : TextView
    lateinit var btnConfirmar : Button
    lateinit var fullName : String
    val db = Firebase.firestore

    companion object {
        fun newInstance() = SolicitudDetalle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_solicitud_detalle, container, false)


        txtName = v.findViewById(R.id.nameClient)
        txtScore = v.findViewById(R.id.score)
        txtDireccion = v.findViewById(R.id.direccion)
        txtPrecio = v.findViewById(R.id.precio)
        txtDescripcion = v.findViewById(R.id.descripcion)
        btnConfirmar = v.findViewById(R.id.aceptar)


        txtName.text = SolicitudDetalleArgs.fromBundle(requireArguments()).clientName
        txtScore.text = SolicitudDetalleArgs.fromBundle(requireArguments()).clientScore
        txtDireccion.text = SolicitudDetalleArgs.fromBundle(requireArguments()).direccion
        txtPrecio.text = SolicitudDetalleArgs.fromBundle(requireArguments()).precio
        txtDescripcion.text = SolicitudDetalleArgs.fromBundle(requireArguments()).descripcionTrabajo
        fullName = SolicitudDetalleArgs.fromBundle(requireArguments()).proveedorName

        val docRef = db.collection("prestadores").document(fullName)


        btnConfirmar.setOnClickListener {
            docRef
                .get()
                .addOnSuccessListener { snapshot ->
                    if(snapshot != null){
                        val prestador = snapshot.toObject(Prestador::class.java)
                        if (prestador != null) {
                            var trabajoBuscado : Servicio = prestador.trabajos.firstOrNull{ it.descripcion == txtDescripcion.text }!!
                            if(trabajoBuscado != null){
                                trabajoBuscado.aceptado = true

                                db.collection("prestadores").document(fullName).set(prestador)

                                Snackbar.make(
                                    it,
                                    "Aceptaste el trabajo de ${txtName.text}, deberás comunicarte a través whatsapp para confirmar detalles",
                                    BaseTransientBottomBar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

        }



        return v
    }

}