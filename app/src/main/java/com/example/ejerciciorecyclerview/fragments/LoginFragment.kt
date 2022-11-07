
package com.example.ejerciciorecyclerview.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Rubro
import com.example.ejerciciorecyclerview.entities.User
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.coroutines.tasks.await
import kotlin.reflect.typeOf


class LoginFragment : Fragment() {

    private lateinit var v : View
    private lateinit var txtUser : EditText
    private lateinit var txtPass : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button
    private lateinit var auth : FirebaseAuth
    var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        auth = Firebase.auth
        txtUser = v.findViewById(R.id.txtUser)
        txtPass = v.findViewById(R.id.txtPass)
        btnLogin = v.findViewById(R.id.btnLogin)
        btnSignUp = v.findViewById(R.id.btnSignUp)

        return v
    }

    override fun onStart() {
        super.onStart()



        btnSignUp.setOnClickListener {
            val actionSignUp = LoginFragmentDirections.actionLoginFragment4ToSignUpFragment()
            v.findNavController().navigate(actionSignUp)
        }

        btnLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(txtUser.text.toString(), txtPass.text.toString())
                .addOnCompleteListener(requireContext() as Activity){task ->
                    if(task.isSuccessful){
                        Log.d("testo", "signInWithEmail:success")
                        val user = auth.currentUser
                        if (user != null) {
                            db.collection("usuarios")
                                .document(user.uid)
                                .get()
                                .addOnSuccessListener { snapshot ->
                                    if(snapshot != null){
                                        val usuarioBuscado = snapshot.toObject(Usuario::class.java)

                                        if(usuarioBuscado != null){
                                            val actionLogin = LoginFragmentDirections.actionLoginFragment4ToRubrosFragment()
                                            v.findNavController().navigate(actionLogin)
                                        }
                                    }else{
                                        db.collection("prestadores")
                                            .document(user.uid)
                                            .get()
                                            .addOnSuccessListener { snapshotPrestador ->
                                                if (snapshotPrestador != null){
                                                    val prestadorBuscado = snapshotPrestador.toObject(Prestador::class.java)

                                                    if(prestadorBuscado != null){
                                                        val actionLogin = LoginFragmentDirections.actionLoginFragment4ToSolicitudesFragment("Juan Carlos")
                                                        v.findNavController().navigate(actionLogin)
                                                    }
                                                }
                                            }
                                    }
                                }
                        }

                    }else{
                        Snackbar.make(it, "Usuario y/o contraseña incorrectos", Snackbar.LENGTH_SHORT).show()
                    }

                }
        }

    }

}