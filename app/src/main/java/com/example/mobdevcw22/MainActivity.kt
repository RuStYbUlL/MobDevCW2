package com.example.mobdevcw22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addMovieButton = findViewById<Button>(R.id.button1)
        val searchMovieButton = findViewById<Button>(R.id.button2)
        val searchActorButton = findViewById<Button>(R.id.button3)

        addMovieButton.setOnClickListener(){
            addMovieActivity();
        }

        searchMovieButton.setOnClickListener(){
            searchMovieActivity();
        }

        searchActorButton.setOnClickListener(){
            searchActorActivity();
        }

    }

    fun searchActorActivity() {
        val searchActorIntent = Intent(this, SearchActor::class.java)
        startActivity(searchActorIntent)
        Log.d("TAG", "onclick listener to searchActorButton created successfully")

    }

    fun searchMovieActivity() {
        val searchMovieIntent = Intent(this, SearchMovies::class.java)
        startActivity(searchMovieIntent)
        Log.d("TAG", "onclick listener to searchMovieButton created successfully")
    }

    fun addMovieActivity() {
        val addMovieIntent = Intent(this, AddMovieList::class.java)
        startActivity(addMovieIntent)
        Log.d("TAG", "onclick listener to addMovieButton created successfully")
    }

}