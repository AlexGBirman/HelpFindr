package com.example.ejerciciorecyclerview.fragments

import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.SolicitudesAceptadasAdapter
import com.example.ejerciciorecyclerview.adapters.UserSolicitudesAdapter
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SolicitudesAceptadasFragment : Fragment() {
    lateinit var v : View
    lateinit var recyclerSolisAceptadas : RecyclerView
    lateinit var listaDeSolicitudes : List<Servicio>
    lateinit var adapter : SolicitudesAceptadasAdapter
    lateinit var prestador : Prestador
    lateinit var usuario : Usuario
    var db = Firebase.firestore
    lateinit var geocoder : Geocoder
    lateinit var auth : FirebaseAuth

    companion object {
        fun newInstance() = SolicitudesAceptadasFragment()
    }

    private lateinit var viewModel: SolicitudesAceptadasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_solicitudes_aceptadas, container, false)
        auth = Firebase.auth
        recyclerSolisAceptadas = v.findViewById(R.id.solicitudesAceptadasRec)
        listaDeSolicitudes = arrayListOf()
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        recyclerSolisAceptadas.layoutManager = LinearLayoutManager(requireContext())

        var userID = auth.currentUser?.uid

        val docRef = userID?.let { db.collection("prestadores").document(it) }
        docRef?.get()?.addOnSuccessListener { snapshot ->
            if(snapshot != null){
                prestador = snapshot.toObject(Prestador::class.java)!!

                listaDeSolicitudes = prestador?.trabajos?.filter { it.aceptado }!!
            }
            adapter = SolicitudesAceptadasAdapter(listaDeSolicitudes){soli ->
                val contratacionBuscada = listaDeSolicitudes[soli]

                var nombreClient = contratacionBuscada.cliente.name
                var desc = contratacionBuscada.descripcion
                var phone = contratacionBuscada.telefonoUsuario
                var fecha = contratacionBuscada.fecha.toDate().time
                var fechaParseada = contratacionBuscada.fecha.toDate()
                Log.d("testeo", "$fechaParseada")

                val actionToDetalleDeSolicitud = SolicitudesAceptadasFragmentDirections.actionSolicitudesAceptadasFragmentToSolisAceptadasDetalleFragment(nombreClient,desc,phone, fecha)
                v.findNavController().navigate(actionToDetalleDeSolicitud)
            }

            recyclerSolisAceptadas.adapter = adapter
        }

        return v
    }
}