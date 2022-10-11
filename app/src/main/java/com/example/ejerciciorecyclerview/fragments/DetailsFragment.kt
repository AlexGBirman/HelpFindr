package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ejerciciorecyclerview.R
import org.w3c.dom.Text

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    lateinit var v : View
    lateinit var detName : TextView
    lateinit var detScore : TextView
    lateinit var receivedName : String
    lateinit var receivedScore : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_details, container, false)

        detName = v.findViewById(R.id.detName)
        detScore = v.findViewById(R.id.detScore)
        receivedName = DetailsFragmentArgs.fromBundle(requireArguments()).userName
        receivedScore = DetailsFragmentArgs.fromBundle(requireArguments()).userScore.toString()

        detName.text = "Nombre: " + receivedName
        detScore.text = "Puntaje: " + receivedScore

        return v
    }


}