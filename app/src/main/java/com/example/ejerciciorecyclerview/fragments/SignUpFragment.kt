package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.ejerciciorecyclerview.R

class SignUpFragment : Fragment() {

    private lateinit var v : View
    private lateinit var txtUser : EditText
    private lateinit var txtPass : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_sign_up, container, false)

        return v
    }

}