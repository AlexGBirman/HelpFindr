package com.example.ejerciciorecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.entities.User

class UserAdapter (
    var userList : MutableList<User>,
    var onClick : (Int) -> Unit
        ) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

        class UserHolder (v : View) : RecyclerView.ViewHolder(v) {
            private var view : View
            init {
                this.view = v
            }

            fun setUserName(name : String) {
                val txtName : TextView = view.findViewById(R.id.txtName)
                txtName.text = name
            }

            fun setUserScore(score : Int) {
                val txtScore : TextView = view.findViewById(R.id.txtScore)
                txtScore.text = score.toString()
            }

            fun setProfilePic(pic : String) {
                // TODO
            }

            fun getCard() : CardView {
                return view.findViewById(R.id.card)
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return (UserHolder(view))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.setUserName(userList[position].name)
        holder.setUserScore((userList[position].score))
        holder.setProfilePic((userList[position].profilePic))

        holder.getCard().setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return this.userList.size
    }

}