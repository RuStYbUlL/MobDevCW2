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
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SearchAllMovies : AppCompatActivity() {

    var resultAllMovies: TextView? = null
    lateinit var searchAllMovieButton: Button
    var searchAllMovieText: EditText? = null

    var url_string: String? = null

    var titleArray = arrayListOf<String>()
    var releasedArray = arrayListOf<String>()


    // the DAO for interacting with the movie database
    lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_all_movies)

        // creating the database
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "myMovieDB").build()
        movieDao = db.movieDao();

        searchAllMovieButton = findViewById(R.id.allMovieSearchButton)
        searchAllMovieText = findViewById(R.id.allMovieText)
        resultAllMovies = findViewById(R.id.allMovieResultText)

        resultAllMovies?.setMovementMethod(ScrollingMovementMethod()) // make scrollable textview


        // set onclick listener for buttons
        searchAllMovieButton.setOnClickListener(){
            getAllMovies()
        }

    }

    fun getAllMovies() {
        Log.d("TAG", "getting movie name entered by user ")
        var enteredMovieName = searchAllMovieText!!.text.toString().trim().lowercase()

        if (enteredMovieName == ""){
            return
        }

        Log.d("TAG", "user entered movie: $enteredMovieName")


        url_string = "https://www.omdbapi.com/?s=/$enteredMovieName/*&apikey=2d0cdf8e" // used '/$enteredMovieName/* to get any matching result

        var jsonData: String = ""

        runBlocking {
            withContext(Dispatchers.IO){
                // stb contains all of the json
                val stb = StringBuilder("")


                val url = URL(url_string)
                Log.d("TAG", "connecting to API")
                val con = url.openConnection() as HttpsURLConnection
                val bf: BufferedReader
                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream))
                    Log.d("TAG", "connection to API successful")
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    return@withContext
                }

                Log.d("TAG", "storing retrieved json data from API to string builder 'stb'")
                var jsonLine = bf.readLine()
                println("[jsonLine] - " + jsonLine.toString())
                while(jsonLine != null){
                    stb.append(jsonLine)
                    jsonLine = bf.readLine()
                }
                Log.d("TAG", "successfully stored API json to stb")

                // pick up all the data
                Log.d("TAG", "calling function to extractJSON")
                jsonData = extractJSON(stb) as String
                Log.d("TAG", "contents of jsonData: " + jsonData)

                // replace square brackets '[]' and commas ',' from 'actorResult'
                Log.d("TAG", "Before replace method "+ jsonData)
                jsonData = jsonData.replace("[", "")
                jsonData = jsonData.replace("]", "")
                Log.d("TAG", "After replace method "+ jsonData)

                // split string to array
                var splitStrToList: List<String> = jsonData.split(",")


                for (i in 0 until splitStrToList.size -1) {
                    resultAllMovies?.append(splitStrToList.get(i))
                    resultAllMovies?.append("\n")
                }
            }
        }

    }

    fun extractJSON(stb: StringBuilder): String {
        Log.d("TAG", "extractJSON function called successfully")

        // Extraction process
        Log.d("TAG", "beginning extraction process")
        val json = JSONObject(stb.toString())
        val jsonArray = json.getJSONArray("Search")
        Log.d("TAG", "contents of jsonArray: " + jsonArray.toString())

        for (i in 0..jsonArray.length() - 1) {

            // jsonObject accesses each value of json file
            val jsonObject = jsonArray.getJSONObject(i)

            // store all values of json file to array
            titleArray.add(jsonObject.getString("Title"))
            releasedArray.add(jsonObject.getString("Year"))

        }
        Log.d("TAG", "contents of releasedArray: " + releasedArray.toString())
        return titleArray.toString()
    }
}



/*
* References
*
* https://developer.mozilla.org/en-US/docs/Web/API/URL_Pattern_API
* */













/*Student Id = 20200644
* UOW Id = w1833522
* */