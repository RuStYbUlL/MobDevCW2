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

        searchActor = findViewById(R.id.allMovieText)
        searchActorButton = findViewById(R.id.allMovieSearchButton)
        resultMovies = findViewById(R.id.allMovieResultText)

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
//        resultMovies?.setText("Movie: " + actorArray.title +" - " + actorArray.released + "\n\n")
        var actorResult= actorArray.toString()

        // replace square brackets '[]' and commas ',' from 'actorResult'
        Log.d("TAG", "Before replace method "+ actorResult)
        actorResult = actorResult.replace("[", "")
        actorResult = actorResult.replace(",", "")
        actorResult = actorResult.replace("]", "")
        Log.d("TAG", "After replace method "+ actorResult)

        // display results to user
        resultMovies?.setText(actorResult)


    }
}