package com.example.ejerciciorecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Servicio
import com.example.ejerciciorecyclerview.entities.User

class SolicitudAdapter (var solicitudesList: List<Servicio>,
                       var onClick : (Int) -> Unit
) : RecyclerView.Adapter<SolicitudAdapter.SolicitudHolder>() {

    class SolicitudHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view : View
        init{
            this.view = v
        }

        fun setDescripcion(desc : String){
            val txtDesc : TextView = view.findViewById(R.id.desc)
            txtDesc.text = desc
        }

        fun setUser(cliente : User){
            val txtUser : TextView = view.findViewById(R.id.cliente)
            txtUser.text = cliente.name
        }

        fun getCardText(): CardView {
            return view.findViewById(R.id.cardSolicitud)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud,parent,false)
        return (SolicitudHolder(view))
    }

    override fun getItemCount(): Int {
        return solicitudesList.size
    }

    override fun onBindViewHolder(holder: SolicitudHolder, position: Int) {
        holder.setUser(solicitudesList[position].cliente)
        holder.setDescripcion(solicitudesList[position].descripcion)

        holder.getCardText().setOnClickListener{
            onClick(position)
        }

    }

}