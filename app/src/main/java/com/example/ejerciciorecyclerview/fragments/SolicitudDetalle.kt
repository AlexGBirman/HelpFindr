package com.example.ejerciciorecyclerview.fragments

import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.*

class SolicitudDetalle : Fragment() {

    lateinit var v : View
    lateinit var txtName: TextView
    lateinit var txtScore : TextView
    lateinit var txtDireccion : TextView
    lateinit var txtPrecio : TextView
    lateinit var txtDescripcion : TextView
    lateinit var txtFecha : TextView
    lateinit var btnConfirmar : Button
    lateinit var btnRechazar : Button
    lateinit var fullName : String
    lateinit var clienteBuscado : Usuario

    val db = Firebase.firestore
    lateinit var auth : FirebaseAuth
    var todoBien = false

    companion object {
        fun newInstance() = SolicitudDetalle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_solicitud_detalle, container, false)
        auth = Firebase.auth


        txtName = v.findViewById(R.id.nameClient)
        txtScore = v.findViewById(R.id.score)
        txtDireccion = v.findViewById(R.id.direccion)
        txtPrecio = v.findViewById(R.id.precio)
        txtDescripcion = v.findViewById(R.id.descripcion)
        txtFecha = v.findViewById(R.id.fechaTrabajo)
        btnConfirmar = v.findViewById(R.id.aceptar)
        btnRechazar = v.findViewById(R.id.rechazarButton)

        txtName.text = SolicitudDetalleArgs.fromBundle(requireArguments()).clientName
        txtScore.text = SolicitudDetalleArgs.fromBundle(requireArguments()).clientScore
        txtDireccion.text = SolicitudDetalleArgs.fromBundle(requireArguments()).direccion
        txtPrecio.text = SolicitudDetalleArgs.fromBundle(requireArguments()).precio
        txtDescripcion.text = SolicitudDetalleArgs.fromBundle(requireArguments()).descripcionTrabajo
        fullName = SolicitudDetalleArgs.fromBundle(requireArguments()).proveedorName

        var fechaSegs = SolicitudDetalleArgs.fromBundle(requireArguments()).segundosTotales
        var fecha = Date(fechaSegs)

        txtFecha.text = "${fecha}"


        val docRef = auth.currentUser?.let { db.collection("prestadores").document(it.uid) }


        btnConfirmar.setOnClickListener {
            docRef?.get()?.addOnSuccessListener { snapshot ->
                if(snapshot != null){
                    val prestador = snapshot.toObject(Prestador::class.java)
                    if (prestador != null) {
                        var trabajoBuscado : Servicio = prestador.trabajos.firstOrNull{ it.descripcion == txtDescripcion.text }!!
                        if(trabajoBuscado != null){
                            var listaAceptados = prestador.trabajos.filter { laburito -> laburito.aceptado }
                            if(listaAceptados.isNotEmpty()){
                                for(laburo in listaAceptados){
                                    var tiempo1 = trabajoBuscado.fecha
                                    var tiempo2 = laburo.fecha

                                    var dif = tiempo1.toDate().time - tiempo2.toDate().time

                                    if(dif < 0){
                                        dif = dif*(-1)
                                    }
                                    dif = dif/(60*60*1000)%60
                                    if(dif <= 2){
                                        Snackbar.make(
                                            it,
                                            "No puedes aceptar el trabajo, tiene conflictos con otros trabajos ya aceptados",
                                            BaseTransientBottomBar.LENGTH_SHORT
                                        ).show()
                                        break
                                    }else{
                                        todoBien = true
                                    }
                                }
                            }else{
                                todoBien = true
                            }
                            if(todoBien){
                                trabajoBuscado.aceptado = true

                                db.collection("usuarios")
                                    .whereEqualTo("phone",trabajoBuscado.telefonoUsuario)
                                    .get()
                                    .addOnSuccessListener {taskN2 ->
                                        if(taskN2 != null){
                                            var losClientes = taskN2.toObjects(Usuario::class.java)

                                            clienteBuscado = losClientes[0]
                                            var tareaCliente = clienteBuscado.contrataciones.firstOrNull { it.descripcion == txtDescripcion.text }

                                            if (tareaCliente != null) {
                                                tareaCliente.aceptado = true
                                            }

                                            var docID = taskN2.documents[0].id

                                            db.collection("usuarios").document(docID).set(clienteBuscado)

                                            auth.currentUser?.let { it1 ->
                                                db.collection("prestadores").document(
                                                    it1.uid).set(prestador)
                                            }

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
                }
            }

        }

        btnRechazar.setOnClickListener {
            docRef?.get()?.addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    val prestador = snapshot.toObject(Prestador::class.java)
                    if (prestador != null) {
                        var trabajoBuscado: Servicio =
                            prestador.trabajos.firstOrNull { it.descripcion == txtDescripcion.text }!!
                        if (trabajoBuscado != null) {
                            trabajoBuscado.aceptado = false

                            db.collection("usuarios")
                                .whereEqualTo("phone",trabajoBuscado.telefonoUsuario)
                                .get()
                                .addOnSuccessListener { taskN2 ->
                                    if (taskN2 != null) {
                                        var losClientes = taskN2.toObjects(Usuario::class.java)

                                        clienteBuscado = losClientes[0]
                                        var tareaCliente =
                                            clienteBuscado.contrataciones.firstOrNull { it.descripcion == txtDescripcion.text }

                                        if (tareaCliente != null) {
                                            clienteBuscado.contrataciones.remove(tareaCliente)
                                        }

                                        var docID = taskN2.documents[0].id

                                        db.collection("usuarios").document(docID)
                                            .set(clienteBuscado)

                                        prestador.trabajos.remove(trabajoBuscado)
                                        docRef.set(prestador)
                                        Snackbar.make(
                                            it,
                                            "Rechazaste el trabajo de ${txtName.text} .",
                                            BaseTransientBottomBar.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                }
            }
        }


        return v
    }

}