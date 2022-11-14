package com.example.ejerciciorecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Prestador
import com.example.ejerciciorecyclerview.entities.Servicio

class PrestadorAdapter (
    var prestadoresList: List<Prestador>,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<PrestadorAdapter.PrestadorHolder>() {

    class PrestadorHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun setNombre(nombre : String){
            val txtNombre : TextView = view.findViewById(R.id.nombre)
            txtNombre.text = nombre
        }
        fun setApellido(apellido : String){
            val txtApellido : TextView = view.findViewById(R.id.apellido)
            txtApellido.text = apellido
        }
        fun getCard(): CardView{
            return view.findViewById(R.id.cardPrestador)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestadorHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prestador, parent, false)
        return (PrestadorHolder(view))
    }

    override fun getItemCount(): Int {
        return prestadoresList.size
    }

    override fun onBindViewHolder(holder: PrestadorHolder, position: Int) {
        holder.setNombre(prestadoresList[position].nombre)
        holder.setApellido(prestadoresList[position].apellido)

        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }


    fun update(modelList : List<Prestador>){
        prestadoresList = modelList

        notifyDataSetChanged()
    }

}