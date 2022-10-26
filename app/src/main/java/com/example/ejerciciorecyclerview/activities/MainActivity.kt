package com.example.ejerciciorecyclerview.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ejerciciorecyclerview.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val parentJob = Job()
        val scope = CoroutineScope(Dispatchers.Default + parentJob)

        suspend fun getAll(){

            val db = Firebase.firestore

            Log.d("hola", "hola soy seba, esto de abajo es la db: ")

            suspend fun getAll(){
                var rubrosList = arrayListOf<String>()
                var questionRef = db.collection("rubros")

                try{
                    val data = questionRef.get().await()
                    for(document in data){
                        Log.d("Prueba", "ACA ESTOY ${document.id} => ${document.data}")
                    }
                }catch (e: Exception){
                    e.message?.let { Log.d("error", it) }
                }
            }
        }
        scope.launch {
            getAll()
        }

    }

}