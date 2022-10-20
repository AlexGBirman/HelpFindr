package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.RubroAdapter
import com.example.ejerciciorecyclerview.entities.Rubro

class RubrosFragment : Fragment() {

    lateinit var v : View
    lateinit var recyclerRubros : RecyclerView
    var rubrosList = RubrosFragmentArgs.fromBundle(requireArguments()).misRubros.toMutableList()
    lateinit var adapter: RubroAdapter

    companion object {
        fun newInstance() = RubrosFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_rubros, container, false)
        recyclerRubros = v.findViewById(R.id.recRubros)


        return v
    }

    override fun onStart() {
        super.onStart()

        adapter = RubroAdapter(rubrosList)
        recyclerRubros.layoutManager = LinearLayoutManager(requireContext())
        recyclerRubros.adapter = adapter
    }


}