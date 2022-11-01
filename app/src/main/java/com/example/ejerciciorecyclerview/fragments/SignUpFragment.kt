package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.example.ejerciciorecyclerview.R

class SignUpFragment : Fragment() {

    private lateinit var v : View
    private lateinit var txtUser : EditText
    private lateinit var txtPass : EditText
    private lateinit var btnClient : Button
    private lateinit var btnEmployee : Button

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_sign_up, container, false)

        btnClient = v.findViewById(R.id.btnClient)
        btnEmployee = v.findViewById(R.id.btnEmployee)

        return v
    }

    override fun onStart() {
        super.onStart()

        btnClient.setOnClickListener {
            val actionHomeClient = SignUpFragmentDirections.actionSignUpFragmentToClienteActivity()
            v.findNavController().navigate(actionHomeClient)
        }

        btnEmployee.setOnClickListener {
            val actionHomeEmployee = SignUpFragmentDirections.actionSignUpFragmentToPrestadorActivity()
            v.findNavController().navigate(actionHomeEmployee)
        }
    }

}