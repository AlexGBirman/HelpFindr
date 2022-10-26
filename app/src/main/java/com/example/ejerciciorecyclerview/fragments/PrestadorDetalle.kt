package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ejerciciorecyclerview.R

class PrestadorDetalle : Fragment() {

    companion object {
        fun newInstance() = PrestadorDetalle()
    }

    lateinit var v : View
    var idPrestador : Int = 0
    lateinit var idView : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_prestador_detalle, container, false)

        idPrestador = PrestadorDetalleArgs.fromBundle(requireArguments()).idPrestador
        idView = idPrestador.toString()

        return v
    }

}