package com.example.ejerciciorecyclerview.fragments

import android.content.Intent
import android.net.Uri
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SolisAceptadasDetalleFragment : Fragment() {

    lateinit var v : View
    lateinit var db : FirebaseFirestore
    var puede = false

    lateinit var txtVNombreCliente : TextView
    lateinit var txtVDescSolicitud : TextView
    lateinit var barraRating : RatingBar

    lateinit var btnCalificar : Button
    lateinit var btnBorrar : Button
    lateinit var btnWpp : Button

    lateinit var nombreCliente : String
    lateinit var numeroDeTelefono : String
    lateinit var descripcionSoli : String

    companion object {
        fun newInstance() = SolisAceptadasDetalleFragment()
    }

    private lateinit var viewModel: SolisAceptadasDetalleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_solis_aceptadas_detalle, container, false)
        db = Firebase.firestore
        var docRef = db.collection("usuarios")
        puede = true

        txtVNombreCliente = v.findViewById(R.id.detalleNombreCliente)
        txtVDescSolicitud = v.findViewById(R.id.detalleDescSoliAceptada)
        barraRating = v.findViewById(R.id.estrellasCalificacion)

        btnCalificar = v.findViewById(R.id.confirmarCalificacion)
        btnBorrar = v.findViewById(R.id.borrarLaSolicitudAceptada)
        btnWpp = v.findViewById(R.id.btnContactarCliente)

        nombreCliente = SolisAceptadasDetalleFragmentArgs.fromBundle(requireArguments()).nombreCliente
        numeroDeTelefono = SolisAceptadasDetalleFragmentArgs.fromBundle(requireArguments()).numeroTelCliente
        descripcionSoli = SolisAceptadasDetalleFragmentArgs.fromBundle(requireArguments()).descSoliAceptada

        txtVNombreCliente.text = nombreCliente
        txtVDescSolicitud.text = descripcionSoli

        btnWpp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+54$numeroDeTelefono"

            val openWhatsappIntent = Intent(Intent.ACTION_VIEW)
            openWhatsappIntent.data = Uri.parse(url)
            startActivity(openWhatsappIntent)
        }

        btnCalificar.setOnClickListener {
            if(puede){
                docRef
                    .whereEqualTo("phone", numeroDeTelefono)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if(snapshot != null){
                            var clientes = snapshot.toObjects(Usuario::class.java)
                            if(clientes.isNotEmpty()){
                                var cliente = clientes[0]

                                var puntaje = barraRating.rating

                                cliente.agregarPuntaje(puntaje)

                                docRef.document(snapshot.documents[0].id).set(cliente)

                                Snackbar.make(v, "Calificaste exitosamente a $nombreCliente", Snackbar.LENGTH_SHORT).show()

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
                            var clientes = snapshot.toObjects(Usuario::class.java)
                            if(clientes.isNotEmpty()){
                                var cliente = clientes[0]

                                var soliBuscada = cliente.contrataciones.firstOrNull() { soli -> soli.descripcion == descripcionSoli }

                                cliente.contrataciones.remove(soliBuscada)

                                var docID = snapshot.documents[0].id

                                docRef.document(docID).set(cliente)

                                Snackbar.make(v, "Borraste exitosamente la solicitud", Snackbar.LENGTH_SHORT)
                            }
                        }
                    }
            }
        }

        return v
    }

}