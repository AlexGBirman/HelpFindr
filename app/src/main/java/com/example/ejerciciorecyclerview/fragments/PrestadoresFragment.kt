package com.example.ejerciciorecyclerview.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.PrestadorAdapter
import com.example.ejerciciorecyclerview.adapters.RubroAdapter
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Rubro
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class PrestadoresFragment : Fragment() {

    companion object {
        fun newInstance() = PrestadoresFragment()
    }


    val PERMISSION_REQUEST_ACCES_LOCATION = 100
    lateinit var v : View
    lateinit var fullName : String
    lateinit var geoLocalization : GeoPoint
    lateinit var usersGeolocalization : GeoPoint
    lateinit var scorePrestador : String
    lateinit var txtRubro : String
    lateinit var recyclerPrestadores : RecyclerView
    lateinit var listaDePrestadores : ArrayList<Prestador>
    lateinit var adapter :PrestadorAdapter
    lateinit var geocoder: Geocoder
    lateinit var adresses: List<Address>
    lateinit var address : String
    lateinit var phone : String
    lateinit var btnMap : Button
    lateinit var btnDist : Button
    lateinit var btnScore : Button
    var db = Firebase.firestore
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_prestadores, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getCurrentLocation()

        recyclerPrestadores = v.findViewById(R.id.recPrest)
        listaDePrestadores = arrayListOf()
        txtRubro = PrestadoresFragmentArgs.fromBundle(requireArguments()).txtRubro
        btnMap = v.findViewById(R.id.btnMap)
        btnDist = v.findViewById(R.id.orderByDist)
        btnScore = v.findViewById(R.id.orderByScore)

        btnMap.setOnClickListener {
            val actionPrestadoresToDetalle = PrestadoresFragmentDirections.actionPrestadoresFragmentToMapFragment(txtRubro)
            v.findNavController().navigate(actionPrestadoresToDetalle)
        }

        btnDist.setOnClickListener {
            var sortedListByDist = listaDePrestadores.sortedByDescending { it.geolocalizacion.compareTo(usersGeolocalization) }
            Log.d("testeo", "$sortedListByDist")
            adapter.update(sortedListByDist)
        }

        btnScore.setOnClickListener{
            var sortedListByScore = listaDePrestadores.sortedWith(compareBy { it.puntajeTotal }).reversed()
            Log.d("testeo", "$sortedListByScore")
            adapter.update(sortedListByScore)
        }


        geocoder = Geocoder(requireContext(), Locale.getDefault())

        val docRef = db.collection("prestadores")

        docRef
            .whereEqualTo("rubro", txtRubro)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    listaDePrestadores = snapshot.toObjects(Prestador::class.java) as ArrayList<Prestador>
                }

                recyclerPrestadores.layoutManager = LinearLayoutManager(requireContext())
                adapter = PrestadorAdapter(listaDePrestadores) {
                    fullName = listaDePrestadores[it].nombre + " " + listaDePrestadores[it].apellido
                    phone = listaDePrestadores[it].phone
                    geoLocalization = listaDePrestadores[it].geolocalizacion
                    if(geoLocalization.latitude != null && geoLocalization.longitude != null){
                        adresses = geocoder.getFromLocation(geoLocalization.latitude, geoLocalization.longitude, 1)
                        address = adresses[0].getAddressLine(0)
                    }

                    scorePrestador = listaDePrestadores[it].puntajeTotal.toString()

                    val actionPrestadoresToDetalle = PrestadoresFragmentDirections.actionPrestadoresToPrestadorDetalle(fullName,address, phone, txtRubro, scorePrestador)
                    v.findNavController().navigate(actionPrestadoresToDetalle)
                }
                recyclerPrestadores.layoutManager = LinearLayoutManager(requireContext())
                recyclerPrestadores.adapter = adapter
            }



        return v
    }

    private fun getCurrentLocation(){
        var geolocalization : LatLng? = null

        if(checkPermission()){
            if(isLocationEnabled()){
                //here we obtain the location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireContext() as Activity){ task ->
                    val location: Location? = task.result

                    if(location == null){
                        Toast.makeText(requireContext(), "Null Recieved", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        usersGeolocalization = GeoPoint(location.latitude, location.longitude)
                    }

                }

            }else{
                //setting opens here
                Toast.makeText(requireContext(), "Turn on Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }
        }else{
            //request permission
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireContext() as Activity,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_REQUEST_ACCES_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCES_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }else{
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission() : Boolean {

        return (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    private fun isLocationEnabled() : Boolean{

        var locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER)


    }


}