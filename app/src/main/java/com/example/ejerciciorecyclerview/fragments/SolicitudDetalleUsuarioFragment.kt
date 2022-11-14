package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.*

class SolicitudDetalleUsuarioFragment : Fragment() {

    lateinit var v : View
    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    var puede = false

    lateinit var txtVNombrePrestador : TextView
    lateinit var txtVDescSolicitud : TextView
    lateinit var txtVFechaSoli : TextView
    lateinit var barraRating : RatingBar

    lateinit var btnCalificar : Button
    lateinit var btnBorrar : Button

    lateinit var nombrePrestador : String
    lateinit var numeroDeTelefono : String
    lateinit var descripcionSoli : String

    companion object {
        fun newInstance() = SolicitudDetalleUsuarioFragment()
    }

    private lateinit var viewModel: SolicitudDetalleUsuarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_solicitud_detalle_usuario, container, false)
        db = Firebase.firestore
        auth = Firebase.auth
        var docRef = db.collection("prestadores")
        var docClient = db.collection("usuarios").document(auth.currentUser?.uid!!)
        puede = true

        txtVNombrePrestador = v.findViewById(R.id.detalleNombrePrest)
        txtVDescSolicitud = v.findViewById(R.id.detalleDescSoliUser)
        txtVFechaSoli = v.findViewById(R.id.fechaSoliUsuario)
        barraRating = v.findViewById(R.id.barraDePuntuacion)

        btnCalificar = v.findViewById(R.id.confirmarPuntuacion)
        btnBorrar = v.findViewById(R.id.borrarLaSolicitud)

        nombrePrestador = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).nombreDelPrestador
        numeroDeTelefono = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).numeroDeTelefonoPrestador
        descripcionSoli = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).descDeLaSoli

        var segundosTrabajo = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).segundosTotales

        var currentLocalDateTime = LocalDateTime.now()
        var current = Date(currentLocalDateTime.year-1900, currentLocalDateTime.monthValue-1,currentLocalDateTime.dayOfMonth,currentLocalDateTime.hour,currentLocalDateTime.second)

        var segundosActuales = current.time

        var diferencia = segundosActuales - segundosTrabajo

        diferencia = diferencia/(60*60*1000)%60

        puede = diferencia > 2

        txtVNombrePrestador.text = nombrePrestador
        txtVDescSolicitud.text = descripcionSoli

        var fechaTrabajo = Date(segundosTrabajo)
        txtVFechaSoli.text = "$fechaTrabajo"


        btnCalificar.setOnClickListener {
            if(puede){
                docRef
                    .whereEqualTo("phone", numeroDeTelefono)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if(snapshot != null){
                            var prestadores = snapshot.toObjects(Prestador::class.java)
                            if(prestadores.isNotEmpty()){
                                var prestador = prestadores[0]

                                var puntaje = barraRating.rating

                                prestador.agregarPuntaje(puntaje)

                                docRef.document(snapshot.documents[0].id).set(prestador)

                                Snackbar.make(v, "Calificaste exitosamente a $nombrePrestador", Snackbar.LENGTH_SHORT).show()

                            }
                        }
                    }
            }else{
                Snackbar.make(v, "No puedes calificar antes de que se realice el servicio", Snackbar.LENGTH_SHORT).show()

            }
        }

        btnBorrar.setOnClickListener {
            if(puede){
                docClient
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if(snapshot != null){
                            var cliente = snapshot.toObject(Usuario::class.java)
                            if(cliente != null){
                                var soliBuscada = cliente.contrataciones.firstOrNull { soli -> soli.descripcion == descripcionSoli }

                                cliente.contrataciones.remove(soliBuscada)

                                docClient.set(cliente)

                                Snackbar.make(v, "Borraste exitosamente la solicitud", Snackbar.LENGTH_SHORT)
                            }
                        }
                    }
            }else{
                Snackbar.make(v, "No puedes borrar el trabajo hasta haberlo finalizado", Snackbar.LENGTH_SHORT).show()
            }
        }





        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SolicitudDetalleUsuarioViewModel::class.java)
        // TODO: Use the ViewModel
    }

}