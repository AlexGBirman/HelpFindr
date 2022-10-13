
package com.example.ejerciciorecyclerview.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.User
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {

    private lateinit var v : View
    private lateinit var txtUser : EditText
    private lateinit var txtPass : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button

    private var userList : MutableList<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        txtUser = v.findViewById(R.id.txtUser)
        txtPass = v.findViewById(R.id.txtPass)
        btnLogin = v.findViewById(R.id.btnLogin)
        btnSignUp = v.findViewById(R.id.btnSignUp)

        userList.add(User("Alex", 10, "", "1234"))
        userList.add(User("Karen", 10, "", "1234"))
        userList.add(User("Milka", 10, "", "1234"))

        return v
    }

    override fun onStart() {
        super.onStart()

        btnSignUp.setOnClickListener {
            val actionSignUp = LoginFragmentDirections.actionLoginFragment4ToSignUpFragment()
            v.findNavController().navigate(actionSignUp)
        }

        btnLogin.setOnClickListener {
            if (txtUser.text.isEmpty() && txtPass.text.isEmpty()) {
                Snackbar.make(it, "Ingrese usuario y contraseña", Snackbar.LENGTH_SHORT).show()
            }
            else if (userList.firstOrNull { it.name == txtUser.text.toString() } != null && userList.firstOrNull { it.pass == txtPass.text.toString() } != null){
                val actionLogin = LoginFragmentDirections.actionLoginFragment4ToClientHomeFragment()
                v.findNavController().navigate(actionLogin)
            }
            else {
                Snackbar.make(it, "Usuario y/o contraseña incorrectos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}