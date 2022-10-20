package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.RubroAdapter
import com.example.ejerciciorecyclerview.entities.Rubro
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RubrosFragment : Fragment() {

    lateinit var v : View
    lateinit var recyclerRubros : RecyclerView
    lateinit var listaDeRubros : ArrayList<Rubro>
    lateinit var adapter : RubroAdapter
    var db = Firebase.firestore

    companion object {
        fun newInstance() = RubrosFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_rubros, container, false)
        recyclerRubros = v.findViewById(R.id.recRubros)
        listaDeRubros = arrayListOf()
        val docRef = db.collection("rubros")

        docRef.get().addOnSuccessListener { snapshot ->
            for(document in snapshot){
                val rubro = document.toObject<Rubro>()
                Log.d("testeo", "$rubro")

                listaDeRubros.add(rubro)
            }
            recyclerRubros.layoutManager = LinearLayoutManager(requireContext())
            adapter = RubroAdapter(listaDeRubros)
            recyclerRubros.adapter = adapter
        }
        return v
    }





}