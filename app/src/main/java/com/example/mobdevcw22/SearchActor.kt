package com.example.mobdevcw22

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class SearchActor : AppCompatActivity() {

    // the DAO for interacting with the movie database
    lateinit var movieDao: MovieDao

    var searchActor: EditText? = null
    var resultMovies: TextView? = null
    lateinit var searchActorButton: Button
    lateinit var actorArray: List<Movie>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)

        // creating the database
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "myMovieDB").build()
        movieDao = db.movieDao();

        searchActor = findViewById(R.id.actorNameText)
        searchActorButton = findViewById(R.id.actorSearchButton)
        resultMovies = findViewById(R.id.actorResultText)

        //make scrollable textview
        resultMovies?.setMovementMethod(ScrollingMovementMethod())

        searchActorButton.setOnClickListener(){
            searchActorFromDB()
        }
    }

    fun searchActorFromDB() {
        Log.d("TAG", "getting actor name that user entered")
        val enteredActorName = searchActor!!.text.toString()
        if (enteredActorName == "") {
            return
        }
        Log.d("TAG", "user entered actor name '$enteredActorName' in search bar")

        runBlocking {
            withContext(Dispatchers.IO){
                var searchActorString: String = "%$enteredActorName%"
                actorArray =  movieDao.loadAllActors(searchActorString.lowercase())

            }
        }


        resultMovies?.setText(actorArray.toString())
//        val query = "SELECT title, actor FROM Movie WHERE actors LIKE :enteredName"


    }
}