package com.example.ejerciciorecyclerview.fragments

import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResult
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.RubroAdapter
import com.example.ejerciciorecyclerview.adapters.SolicitudAdapter
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Rubro
import com.example.ejerciciorecyclerview.entities.Servicio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class SolicitudesFragment : Fragment() {

    lateinit var v : View
    lateinit var recyclerSolicitudes : RecyclerView
    lateinit var listaDeSolicitudes : List<Servicio>
    lateinit var adapter : SolicitudAdapter
    lateinit var prestador : Prestador
    var db = Firebase.firestore
    lateinit var geocoder : Geocoder
    lateinit var auth : FirebaseAuth

    companion object {
        fun newInstance() = SolicitudesFragment()
    }

    private lateinit var viewModel: SolicitudesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_solicitudes, container, false)
        auth = Firebase.auth
        recyclerSolicitudes = v.findViewById(R.id.solicitudRec)
        listaDeSolicitudes = arrayListOf()
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        val emplName = SolicitudesFragmentArgs.fromBundle(requireArguments()).employeeName



        val docRef = db.collection("prestadores")


        recyclerSolicitudes.layoutManager = LinearLayoutManager(requireContext())


        auth.currentUser?.let {
            docRef.document(it.uid)
                .get()
                .addOnSuccessListener {snapshot ->
                    if(snapshot != null){
                        prestador = snapshot.toObject(Prestador::class.java)!!

                        listaDeSolicitudes = prestador?.trabajos!!.filter {servicio ->  !servicio.aceptado }

                        Log.d("testeo", "$prestador")
                        Log.d("testeo", "$listaDeSolicitudes")
                    }

                    adapter = SolicitudAdapter(listaDeSolicitudes){ soli ->
                        val servicioBuscado = listaDeSolicitudes[soli]

                        val desc = servicioBuscado.descripcion
                        val fullName = prestador.nombre + " " + prestador.apellido
                        val clientName = servicioBuscado.cliente.name
                        val clientScore = servicioBuscado.cliente.score.toString()
                        val precio = servicioBuscado.precio.toString()

                        val addresses = geocoder.getFromLocation(servicioBuscado.geolocalizacion.latitude, servicioBuscado.geolocalizacion.longitude, 1)
                        val address = addresses[0].getAddressLine(0)

                        val actionSoliToDetails = SolicitudesFragmentDirections.actionSolicitudesFragmentToSolicitudDetalle(clientName,precio,clientScore,desc,address, fullName)
                        v.findNavController().navigate(actionSoliToDetails)
                    }

                    recyclerSolicitudes.adapter = adapter
                }
        }



        return v
    }


}