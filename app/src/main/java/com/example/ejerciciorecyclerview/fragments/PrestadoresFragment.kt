package com.example.ejerciciorecyclerview.fragments

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
import com.example.ejerciciorecyclerview.adapters.PrestadorAdapter
import com.example.ejerciciorecyclerview.adapters.RubroAdapter
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Rubro
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PrestadoresFragment : Fragment() {

    companion object {
        fun newInstance() = PrestadoresFragment()
    }

    lateinit var v : View
    var idPrestador : Int = 0
    lateinit var txtRubro : String
    lateinit var recyclerPrestadores : RecyclerView
    lateinit var listaDePrestadores : ArrayList<Prestador>
    lateinit var adapter :PrestadorAdapter
    var db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_prestadores, container, false)
        recyclerPrestadores = v.findViewById(R.id.recPrest)
        listaDePrestadores = arrayListOf()
        txtRubro = PrestadoresFragmentArgs.fromBundle(requireArguments()).txtRubro

        val docRef = db.collection("prestadores")

        docRef
            .whereEqualTo("rubro", txtRubro)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    listaDePrestadores = snapshot.toObjects(Prestador::class.java) as ArrayList<Prestador>
                }

                recyclerPrestadores.layoutManager = LinearLayoutManager(requireContext())
                adapter = PrestadorAdapter(listaDePrestadores) {
                    idPrestador = listaDePrestadores[it].id
                    val actionPrestadoresToDetalle = PrestadoresFragmentDirections.actionPrestadoresToPrestadorDetalle(idPrestador)
                    v.findNavController().navigate(actionPrestadoresToDetalle)
                }
                recyclerPrestadores.layoutManager = LinearLayoutManager(requireContext())
                recyclerPrestadores.adapter = adapter
            }

        return v
    }


}