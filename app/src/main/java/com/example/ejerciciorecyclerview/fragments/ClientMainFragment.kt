package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ejerciciorecyclerview.R

class ClientMainFragment : Fragment() {

    lateinit var v : View
    companion object {
        fun newInstance() = ClientMainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_client_main, container, false)


        return v
    }


}