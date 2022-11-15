package com.example.ejerciciorecyclerview.fragments
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.User
import com.example.ejerciciorecyclerview.entities.Usuario
import com.example.ejerciciorecycvalerview.fragments.TimePickerFragment
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


class PrestadorDetalle : Fragment() {

    companion object {
        fun newInstance() = PrestadorDetalle()
    }

    lateinit var v : View
    lateinit var fullName : String
    lateinit var geoLocalization : String
    lateinit var puntajePrestador : String
    lateinit var btnContact : Button
    lateinit var txtName : TextView
    lateinit var txtGeo : TextView
    lateinit var txtScore : TextView
    lateinit var proveedores : List<Prestador>
    lateinit var editDescription: EditText
    lateinit var etFecha : EditText
    lateinit var laFecha : LocalDate
    lateinit var etHora : EditText
    lateinit var laHora : Time
    lateinit var usuario : Usuario

    val db = Firebase.firestore
    val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_prestador_detalle, container, false)

        fullName = PrestadorDetalleArgs.fromBundle(requireArguments()).fullName
        geoLocalization = PrestadorDetalleArgs.fromBundle(requireArguments()).geoLocalization
        puntajePrestador = PrestadorDetalleArgs.fromBundle(requireArguments()).scorePrestador

        etFecha = v.findViewById(R.id.etFecha)
        etHora = v.findViewById(R.id.etHora)

        etFecha.setOnClickListener { showDatePickerDialog() }
        etHora.setOnClickListener { showTimePickerDialog() }

        txtName = v.findViewById(R.id.fullName)
        txtGeo = v.findViewById(R.id.geoLoc)
        btnContact = v.findViewById(R.id.btnContact)
        btnContact.setBackgroundColor(Color.YELLOW)
        editDescription = v.findViewById(R.id.editDescription)
        txtScore = v.findViewById(R.id.puntajeDelPrestadorDetalle)

        var phone = PrestadorDetalleArgs.fromBundle(requireArguments()).phone
        var rubro = PrestadorDetalleArgs.fromBundle(requireArguments()).rubro

        txtName.text = fullName
        txtGeo.text = geoLocalization
        txtScore.text = puntajePrestador

        val docRef = db.collection("prestadores")
        val docUsers = db.collection("usuarios").document(auth.currentUser!!.uid)

        btnContact.setOnClickListener{
            docRef
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener { snapshot ->
                    if(snapshot != null){
                        proveedores = snapshot.toObjects(Prestador::class.java)
                        var proveedor = proveedores[0]

                        var date = Date((laFecha.year - 1900),(laFecha.monthValue - 1),laFecha.dayOfMonth,laHora.hours,laHora.minutes)
                        var fechaDeTrabajo = Timestamp(date)

                        docUsers
                            .get()
                            .addOnSuccessListener { task ->
                                if(task != null){
                                    usuario = task.toObject(Usuario::class.java)!!

                                    var servicioNuevo = Servicio("${editDescription.text}", 0.0, rubro,usuario.geolocalizacion,
                                        proveedor.nombre, User((usuario.nombre + " " + usuario.apellido), usuario.puntajeTotal.toDouble(), "",""),fechaDeTrabajo,usuario.phone, phone)

                                    usuario.contrataciones.add(servicioNuevo)
                                    proveedor.trabajos.add(servicioNuevo)

                                    docUsers.set(usuario)
                                    docRef.document(snapshot.documents[0].id).set(proveedor)

                                    make(it, "Solicitaste Exitosamente los servicios de $fullName, deberás aguardar su confirmación", LENGTH_SHORT).show()
                                }
                            }
                    }
                }
        }


        return v
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment{hours, minutes -> onTimeSelected(hours,minutes)}

        timePicker.show(childFragmentManager, "timePicker")
    }

    fun onTimeSelected(hours : Int, minutes: Int){
        laHora = Time(hours,minutes,0)

        Log.d("testeo", "$laHora")

        etHora.setText("${laHora.hours}:${laHora.minutes}")
    }

    private fun showDatePickerDialog(){
        val datePicker = DatePickerFragment {day,month, year -> onDaySelected(day,month,year)}

        datePicker.show(childFragmentManager, "datePicker")
    }

    fun onDaySelected(day:Int, month:Int,year:Int){
        var dia = day.toString()
        var mes = (month+1).toString()
        var anio = year.toString()

        if(day < 10){
            dia = "0$day"
        }
        if(month+1 < 10){
           mes = "0${month + 1}"
        }


        var formatDate = "$anio-$mes-$dia"

        laFecha = LocalDate.parse(formatDate)

        etFecha.setText("${laFecha.year}/${laFecha.monthValue}/${laFecha.dayOfMonth}")


        Log.d("testeo", "${laFecha.year}, ${laFecha.monthValue}, ${laFecha.dayOfMonth}")

    }

}