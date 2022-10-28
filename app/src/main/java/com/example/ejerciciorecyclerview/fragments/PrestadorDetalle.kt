package com.example.ejerciciorecyclerview.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
    lateinit var proveedores : List<Prestador>
    val db = Firebase.firestore


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
        var rubro = PrestadorDetalleArgs.fromBundle(requireArguments()).rubro

        val docRef = db.collection("prestadores")

        btnContact.setOnClickListener{
            val actionToMap = PrestadorDetalleDirections.actionPrestadorDetalleToMapFragment()
            v.findNavController().navigate(actionToMap)
            /*docRef
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener { snapshot ->
                    if(snapshot != null){
                        proveedores = snapshot.toObjects(Prestador::class.java)
                        var proveedor = proveedores[0]
                        proveedor.trabajos.add(Servicio("Trabajo", 0.0, rubro,GeoPoint(0.0,0.0), proveedor.nombre))
                        docRef.document(fullName).set(proveedor)
                    }
                    make(it, "Solicitaste Exitosamente los servicios de $fullName, deberás aguardar su confirmación", LENGTH_SHORT).show()


                }*/

        }

        /*docRef
            .whereEqualTo("phone", phone)
            .get()
            .addOnSuccessListener { snapshot ->
                if(snapshot != null){
                    proveedores = snapshot.toObjects(Prestador::class.java)
                    var senior = proveedores[0]
                    senior.trabajos.add(Servicio("Trabajo", 0.0, rubro,GeoPoint(0.0,0.0), senior.nombre))
                    docRef.document(fullName).set(senior)
                }
            }*/

        /*btnContact.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+54$phone"

            val openWhatsappIntent = Intent(Intent.ACTION_VIEW)
            openWhatsappIntent.data = Uri.parse(url)
            startActivity(openWhatsappIntent)
        }*/

        txtName.text = fullName
        txtGeo.text = geoLocalization

        return v
    }

}