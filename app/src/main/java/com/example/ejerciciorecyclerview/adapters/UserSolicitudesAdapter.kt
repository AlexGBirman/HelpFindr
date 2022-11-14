package com.example.ejerciciorecyclerview.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.User

class UserSolicitudesAdapter(var solicitudesList: List<Servicio>,
                             var onClick : (Int) -> Unit
) : RecyclerView.Adapter<UserSolicitudesAdapter.UserSolicitudHolder>()  {

    class UserSolicitudHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View
        init{
            this.view = v
        }

        fun setEstado(estado : Boolean){
            val descEstado : TextView = view.findViewById(R.id.estadoDeLaSolicitud)

            if(estado){
                descEstado.setTextColor(Color.parseColor("#00FF00"))
                descEstado.text = "Aceptada :)"
            }else{
                descEstado.setTextColor(Color.parseColor("#FFFF00"))
                descEstado.text = "Pendiente :/"
            }
        }


        fun setDescripcion(desc : String){
            val txtDesc : TextView = view.findViewById(R.id.descDeLaSolicitud)
            txtDesc.text = desc
        }

        fun setUser(prestador: String){
            val txtUser : TextView = view.findViewById(R.id.nombreDelPrestador)
            txtUser.text = prestador
        }

        fun getCardText(): CardView {
            return view.findViewById(R.id.cardUserSolicitud)

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSolicitudesAdapter.UserSolicitudHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_solicitud,parent,false)
        return (UserSolicitudesAdapter.UserSolicitudHolder(view))
    }

    override fun getItemCount(): Int {
        return solicitudesList.size
    }

    override fun onBindViewHolder(holder: UserSolicitudHolder, position: Int) {
        holder.setUser(solicitudesList[position].prestador)
        holder.setDescripcion(solicitudesList[position].descripcion)
        holder.setEstado(solicitudesList[position].aceptado)

        holder.getCardText().setOnClickListener{
            onClick(position)
        }
    }
}