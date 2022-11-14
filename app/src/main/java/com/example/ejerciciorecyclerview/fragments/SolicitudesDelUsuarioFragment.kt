package com.example.ejerciciorecyclerview.fragments

import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.SolicitudAdapter
import com.example.ejerciciorecyclerview.adapters.UserSolicitudesAdapter
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SolicitudesDelUsuarioFragment : Fragment() {
    lateinit var v : View
    lateinit var recyclerSolicitudes : RecyclerView
    lateinit var listaDeSolicitudes : List<Servicio>
    lateinit var adapter : UserSolicitudesAdapter
    lateinit var usuario : Usuario
    var db = Firebase.firestore
    lateinit var geocoder : Geocoder
    lateinit var auth : FirebaseAuth


    companion object {
        fun newInstance() = SolicitudesDelUsuarioFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_solicitudes_del_usuario, container, false)
        auth = Firebase.auth
        recyclerSolicitudes = v.findViewById(R.id.userSolicitudesRec)
        listaDeSolicitudes = arrayListOf()
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        recyclerSolicitudes.layoutManager = LinearLayoutManager(requireContext())

        var userID = auth.currentUser?.uid

        val docRef = db.collection("usuarios")

        if (userID != null) {
            docRef
                .document(userID)
                .get()
                .addOnSuccessListener { snapshot ->
                    if(snapshot != null){
                        usuario = snapshot.toObject(Usuario::class.java)!!

                        listaDeSolicitudes = usuario?.contrataciones!!
                    }
                    adapter = UserSolicitudesAdapter(listaDeSolicitudes){soli ->
                        val contratacionBuscada = listaDeSolicitudes[soli]

                        if(contratacionBuscada.aceptado){

                            var nombrePrest = contratacionBuscada.prestador
                            var desc = contratacionBuscada.descripcion
                            var phone = contratacionBuscada.telefonoPrestador

                            val actionToDetalleDeSolicitud = SolicitudesDelUsuarioFragmentDirections.actionSolicitudesDelUsuarioFragmentToSolicitudDetalleUsuarioFragment(nombrePrest,desc,phone)
                            v.findNavController().navigate(actionToDetalleDeSolicitud)

                        }else{
                            Snackbar.make(
                                v,
                                "La solicitud aun no fue aceptada, aguarde respuesta del proveedor por favor",
                                BaseTransientBottomBar.LENGTH_SHORT
                            ).show()
                        }

                    }
                    recyclerSolicitudes.adapter = adapter
                }
        }




        return v
    }

}