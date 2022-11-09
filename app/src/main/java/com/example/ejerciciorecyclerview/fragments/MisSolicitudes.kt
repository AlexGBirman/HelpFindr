package com.example.ejerciciorecyclerview.fragments

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
import com.example.ejerciciorecyclerview.adapters.SolicitudAdapter
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MisSolicitudes : Fragment() {

    companion object {
        fun newInstance() = MisSolicitudes()
    }

    lateinit var v: View
    lateinit var usuario : Usuario
    lateinit var misSolicitudes: RecyclerView
    lateinit var listaDeSolicitudes : List<Servicio>
    lateinit var adapter: SolicitudAdapter
    var db = Firebase.firestore
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_mis_solicitudes, container, false)
        auth = Firebase.auth
        listaDeSolicitudes = arrayListOf()
        misSolicitudes = v.findViewById(R.id.misSolicitudesRV)

        return v
    }

    override fun onStart() {
        super.onStart()

        val docRef = db.collection("usuarios")


        misSolicitudes.layoutManager = LinearLayoutManager(requireContext())


        auth.currentUser?.let {
            docRef.document(it.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        usuario = snapshot.toObject(Usuario::class.java)!!
                        listaDeSolicitudes = usuario?.contrataciones!!

                        Log.d("testeo", "$usuario")
                        Log.d("testeo", "$listaDeSolicitudes")
                    }
                }
            adapter = SolicitudAdapter(listaDeSolicitudes){ soli ->
                listaDeSolicitudes[soli]
            }
                misSolicitudes.adapter = adapter
        }
    }


}


