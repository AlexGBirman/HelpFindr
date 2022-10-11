package com.example.ejerciciorecyclerview.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciorecyclerview.R
import com.example.ejerciciorecyclerview.adapters.UserAdapter
import com.example.ejerciciorecyclerview.entities.User

class UsersFragment : Fragment() {

    companion object {
        fun newInstance() = UsersFragment()
    }

    lateinit var v : View

    lateinit var recyclerUser : RecyclerView

    var usersList : MutableList<User> = mutableListOf()
    lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_users, container, false)
        recyclerUser = v.findViewById(R.id.recyclerUsers)

        usersList.add(User("Pepe",10,"https://xsgames.co/randomusers/assets/avatars/male/35.jpg","1234"))
        usersList.add(User("Pedro",9,"https://xsgames.co/randomusers/assets/avatars/male/61.jpg","1234"))
        usersList.add(User("Jose",8,"https://xsgames.co/randomusers/assets/avatars/male/64.jpg","1234"))
        usersList.add(User("Guillermo",6,"https://xsgames.co/randomusers/assets/avatars/male/39.jpg","1234"))
        usersList.add(User("Valeria",4,"https://xsgames.co/randomusers/assets/avatars/male/32.jpg","1234"))
        usersList.add(User("Nito",7,"https://xsgames.co/randomusers/assets/avatars/male/42.jpg","1234"))
        usersList.add(User("Andrea",1,"https://xsgames.co/randomusers/assets/avatars/male/41.jpg","1234"))
        usersList.add(User("Jazmin",6,"https://xsgames.co/randomusers/assets/avatars/male/30.jpg","1234"))
        usersList.add(User("Uberto",8,"https://xsgames.co/randomusers/assets/avatars/male/27.jpg","1234"))
        usersList.add(User("Manuel",5,"https://xsgames.co/randomusers/assets/avatars/male/38.jpg","1234"))


        return v
    }

    override fun onStart() {
        super.onStart()

        adapter = UserAdapter(usersList) {
            val actionDetails = UsersFragmentDirections.actionUsersFragmentToDetailsFragment2(usersList[it].name, usersList[it].score)
            v.findNavController().navigate(actionDetails)
        }
        recyclerUser.layoutManager = LinearLayoutManager(requireContext())
        recyclerUser.adapter = adapter
    }
}