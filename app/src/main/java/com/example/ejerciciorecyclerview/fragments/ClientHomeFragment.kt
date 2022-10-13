package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.ejerciciorecyclerview.R

class ClientHomeFragment : Fragment() {

    lateinit var v : View
    lateinit var btnSearch : View

    companion object {
        fun newInstance() = ClientHomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_client_home, container, false)

        btnSearch = v.findViewById(R.id.btnSearch)

        return v
    }

    override fun onStart() {
        super.onStart()

        btnSearch.setOnClickListener {
            val actionSearch = ClientHomeFragmentDirections.actionClientHomeFragmentToUsersFragment()
            v.findNavController().navigate(actionSearch)
        }
    }

}