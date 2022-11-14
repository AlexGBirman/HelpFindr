package com.example.ejerciciorecyclerview.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentManager
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.activities.MainActivity
import com.example.ejerciciorecyclerview.entities.Prestador
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.jar.Manifest

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var v :View
    private lateinit var map:GoogleMap
    val db = Firebase.firestore
    val docRef = db.collection("prestadores")
    var listaPrestadores : List<Prestador> = arrayListOf()
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient



    companion object {
        private const val PERMISSION_REQUEST_ACCES_LOCATION = 100
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_map, container, false)
        createFragment()


        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun createFragment(){
        val mapFragment :SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        addMarkerToUser()

        createMarker()

    }

    private fun addMarkerToUser(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        getCurrentLocation()

    }

    private fun getCurrentLocation(){
        var geolocalization : LatLng? = null

        if(checkPermission()){
            if(isLocationEnabled()){
                //here we obtain the location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireContext() as Activity){task ->
                    val location:Location? = task.result

                    if(location == null){
                        Toast.makeText(requireContext(), "Null Recieved", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        geolocalization = LatLng(location.latitude, location.longitude)

                        map.addMarker(MarkerOptions().position(geolocalization!!).title("Usted!!"))
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(geolocalization, 18f),
                            4500,
                            null
                        )
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

        var locationManager:LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


    }


    private fun createMarker(){
        var txtRubro = MapFragmentArgs.fromBundle(requireArguments()).txtRubro
        docRef
            .whereEqualTo("rubro",txtRubro)
            .get()
            .addOnSuccessListener { snapshot ->
                if(snapshot != null){
                    listaPrestadores = snapshot.toObjects(Prestador::class.java)
                }

                if(listaPrestadores.isNotEmpty()){
                    for(prestador in listaPrestadores){
                        val longitud = prestador.geolocalizacion.longitude
                        val latitud = prestador.geolocalizacion.latitude

                        val name = prestador.nombre + " " + prestador.apellido
                        val rubro = prestador.rubro

                        val coordinates = LatLng(latitud, longitud)

                        val marker = MarkerOptions().position(coordinates).title("$name : $rubro")
                        map.addMarker(marker)
                    }
                }
            }
    }

}

