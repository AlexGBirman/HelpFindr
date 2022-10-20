package com.example.ejerciciorecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.Rubro

class RubroAdapter(var rubrosList: MutableList<Rubro>) : RecyclerView.Adapter<RubroAdapter.RubroHolder>(){

    class RubroHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view : View
        init{
            this.view = v
        }

        fun setRubro(rubro : String){
            val txtRubro : TextView = view.findViewById(R.id.rubro)
            txtRubro.text = rubro
        }

        fun setCantPrest(cantPrest : Int){
            val txtCant : TextView = view.findViewById(R.id.cantPrest)
            txtCant.text = cantPrest.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RubroHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rubro,parent,false)
        return (RubroHolder(view))
    }

    override fun getItemCount(): Int {
        return rubrosList.size
    }

    override fun onBindViewHolder(holder: RubroHolder, position: Int) {
        holder.setRubro(rubrosList[position].rubro)
        holder.setCantPrest(rubrosList[position].cantPrest)
    }


}