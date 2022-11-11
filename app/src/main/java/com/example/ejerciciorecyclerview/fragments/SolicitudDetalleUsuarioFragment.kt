package com.example.ejerciciorecyclerview.fragments

import android.media.Rating
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SolicitudDetalleUsuarioFragment : Fragment() {

    lateinit var v : View
    lateinit var db : FirebaseFirestore
    var puede = false

    lateinit var txtVNombrePrestador : TextView
    lateinit var txtVDescSolicitud : TextView
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
        var docRef = db.collection("prestadores")
        puede = true

        txtVNombrePrestador = v.findViewById(R.id.detalleNombrePrest)
        txtVDescSolicitud = v.findViewById(R.id.detalleDescSoliUser)
        barraRating = v.findViewById(R.id.barraDePuntuacion)

        btnCalificar = v.findViewById(R.id.confirmarPuntuacion)
        btnBorrar = v.findViewById(R.id.borrarLaSolicitud)

        nombrePrestador = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).nombreDelPrestador
        numeroDeTelefono = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).numeroDeTelefonoPrestador
        descripcionSoli = SolicitudDetalleUsuarioFragmentArgs.fromBundle(requireArguments()).descDeLaSoli

        txtVNombrePrestador.text = nombrePrestador
        txtVDescSolicitud.text = descripcionSoli


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
            }
        }

        btnBorrar.setOnClickListener {
            if(puede){
                docRef
                    .whereEqualTo("phone", numeroDeTelefono)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if(snapshot != null){
                            var prestadores = snapshot.toObjects(Prestador::class.java)
                            if(prestadores.isNotEmpty()){
                                var prestador = prestadores[0]

                                var soliBuscada = prestador.trabajos.filter { soli -> soli.descripcion == descripcionSoli }

                                prestador.trabajos.remove(soliBuscada[0])

                                var docID = snapshot.documents[0].id

                                docRef.document(docID).set(prestador)

                                Snackbar.make(v, "Borraste exitosamente la solicitud", Snackbar.LENGTH_SHORT)
                            }
                        }
                    }
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