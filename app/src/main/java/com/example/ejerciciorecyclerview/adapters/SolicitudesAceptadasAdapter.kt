package com.example.ejerciciorecyclerview.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Servicio

class SolicitudesAceptadasAdapter(var solisAceptadasList : List<Servicio>,
                                  var onClick : (Int) -> Unit
) : RecyclerView.Adapter<SolicitudesAceptadasAdapter.SolicitudesAceptadasHolder>() {

    class SolicitudesAceptadasHolder(v : View) : RecyclerView.ViewHolder(v){
        private var view : View
        init{
            this.view = v
        }

        fun setDescripcion(desc : String){
            val txtDesc : TextView = view.findViewById(R.id.acepSoliDesc)
            txtDesc.text = desc
        }

        fun setUser(usuario: String){
            val txtUser : TextView = view.findViewById(R.id.clientAcepName)
            txtUser.text = usuario
        }

        fun getCardText(): CardView {
            return view.findViewById(R.id.cardSolisAceptadas)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudesAceptadasHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solis_aceptadas, parent,false)
        return SolicitudesAceptadasHolder(view)
    }

    override fun onBindViewHolder(holder: SolicitudesAceptadasHolder, position: Int) {
        holder.setUser(solisAceptadasList[position].cliente.name)
        holder.setDescripcion(solisAceptadasList[position].descripcion)

        holder.getCardText().setOnClickListener{
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return solisAceptadasList.size
    }
}