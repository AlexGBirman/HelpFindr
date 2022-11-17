package com.example.ejerciciorecyclerview.fragments

import android.app.Activity
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*

class SignUpFragment : Fragment() {

    private lateinit var v : View
    private lateinit var txtUser : EditText
    private lateinit var txtPass : EditText
    private lateinit var txtName : EditText
    private lateinit var txtLastName : EditText
    private lateinit var txtAddress : EditText
    private lateinit var txtPostCode : EditText
    private lateinit var txtPhoneNumber : EditText
    private lateinit var txtRubro : EditText


    private lateinit var btnClient : Button
    private lateinit var btnEmployee : Button

    private lateinit var auth : FirebaseAuth
    var db = Firebase.firestore

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_sign_up, container, false)

        auth = Firebase.auth

        txtUser = v.findViewById(R.id.txtSignName)
        txtPass = v.findViewById(R.id.txtSignPass)
        txtName = v.findViewById(R.id.txtPersonNameSignUp)
        txtLastName = v.findViewById(R.id.txtPersonLastNameSignUp)
        txtAddress = v.findViewById(R.id.txtAddressSignUp)
        txtPostCode = v.findViewById(R.id.postCode)
        txtPhoneNumber = v.findViewById(R.id.txtPhoneNumberSIgnUp)
        txtRubro = v.findViewById(R.id.txtRubroSignUp)

        btnClient = v.findViewById(R.id.btnClient)
        btnEmployee = v.findViewById(R.id.btnEmployee)
        btnClient.setBackgroundColor(Color.YELLOW)
        btnEmployee.setBackgroundColor(Color.YELLOW)
        return v
    }

    private fun getLocFromAddress(stNameNumber : String, postCode : String) : GeoPoint?{
        var coder = Geocoder(requireContext(), Locale.getDefault())
        var addresses = listOf<Address>()
        lateinit var geolocalizacion : GeoPoint
        var formattedAddress = "$stNameNumber, C$postCode CABA, Argentina"

        try{
            addresses = coder.getFromLocationName(formattedAddress,1)
            if(addresses == null){
                return null
            }

            var location = addresses[0]
            Log.d("testeo", "$location")

            geolocalizacion = GeoPoint(location.latitude,location.longitude)

        }catch (ex : IOException){
            ex.printStackTrace()
        }
        return geolocalizacion

    }

    override fun onStart() {
        super.onStart()

        btnClient.setOnClickListener {
            auth.createUserWithEmailAndPassword(txtUser.text.toString(), txtPass.text.toString())
                .addOnCompleteListener(requireContext() as Activity){task ->
                    if(task.isSuccessful){
                        Log.d("testeo", "success")
                        val user = auth.currentUser

                        var direccion = txtAddress.text.toString()
                        var codPostal = txtPostCode.text.toString()
                        var geolocalizacion = getLocFromAddress(direccion, codPostal)!!
                        var usuario = Usuario(txtName.text.toString(), txtLastName.text.toString(), geolocalizacion, txtPhoneNumber.text.toString(), arrayListOf(), arrayListOf())

                        if (user != null) {
                            db.collection("usuarios").document(user.uid).set(usuario)
                                .addOnSuccessListener { Log.d("testeo", "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w("testeo", "Error writing document", e) }
                            val actionHome = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment4()
                            v.findNavController().navigate(actionHome)
                        }
                    }
                }
        }

        btnEmployee.setOnClickListener {
            auth.createUserWithEmailAndPassword(txtUser.text.toString(), txtPass.text.toString())
                .addOnCompleteListener(requireContext() as Activity){task ->
                    if(task.isSuccessful){
                        Log.d("testeo", "succes")
                        val user = auth.currentUser

                        var direccion = txtAddress.text.toString()
                        var codPostal = txtPostCode.text.toString()
                        var geolocalizacion = getLocFromAddress(direccion, codPostal)!!

                        var prestador = Prestador(txtLastName.text.toString(), "PRESTADOR", txtName.text.toString(), txtRubro.text.toString(), geolocalizacion, txtPhoneNumber.text.toString(), arrayListOf(), arrayListOf())

                        if (user != null) {
                            db.collection("prestadores").document(user.uid).set(prestador)
                            val actionHome = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment4()
                            v.findNavController().navigate(actionHome)
                        }
                    }
                }
        }
    }

}