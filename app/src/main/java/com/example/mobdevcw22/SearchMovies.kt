package com.example.mobdevcw22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import org.json.JSONObject
import javax.net.ssl.HttpsURLConnection


class SearchMovies : AppCompatActivity() {

    // the DAO for interacting with the movie database
    lateinit var movieDao: MovieDao

    var your_key: String? = null
    var url_string: String? = null
    var searchtext: EditText? = null
    var displayText: TextView? = null
    lateinit var retrieveMovieButton: Button
    lateinit var saveMovieToDB_Button: Button

    lateinit var title: String
    lateinit var year: String
    lateinit var rated: String
    lateinit var released: String
    lateinit var runtime: String
    lateinit var genre: String
    lateinit var director: String
    lateinit var writer: String
    lateinit var actors: String
    lateinit var plot: String
    var error: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        // creating the database
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "myMovieDB").build()
        movieDao = db.movieDao();

        // Movie user entered
        searchtext = findViewById(R.id.editText2)
        displayText = findViewById(R.id.textView2)
        retrieveMovieButton = findViewById(R.id.retrieveButton2)
        saveMovieToDB_Button = findViewById(R.id.saveToDB_button)

        retrieveMovieButton.setOnClickListener(){
            getMovie()
        }
        saveMovieToDB_Button.setOnClickListener(){
            if (error == null){
                saveMovie()
            }
            else{
                displayText?.setText("No such movie found. Cannot save to DB :(")

            }

        }

    }

    fun saveMovie() {
        getMovie()

        runBlocking {
            withContext(Dispatchers.IO) {
                //save to database
                var mov = Movie(
                    0,
                    title,
                    year,
                    rated,
                    released,
                    runtime,
                    genre,
                    director,
                    writer,
                    actors,
                    plot
                )

                movieDao.addMovie(mov)
            }
        }

    }

    fun getMovie() {
        Log.d("TAG", "getting movie name entered by user ")
        val enteredMovieName = searchtext!!.text.toString().trim()
        if (enteredMovieName == ""){
            return
        }
        Log.d("TAG", "user entered movie: $enteredMovieName")

        your_key = "2d0cdf8e"

        url_string = "https://www.omdbapi.com/?t=$enteredMovieName&apikey=2d0cdf8e"


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
                jsonData = extractJSON(stb)

                Log.d("TAG", "displaying details to user")
                displayText?.setText(jsonData)



            }

        }
    }

    fun extractJSON(stb: StringBuilder): String {
        Log.d("TAG", "extractJSON function called successfully")

        // Extraction process
        Log.d("TAG", "beginning extraction process")
        val json = JSONObject(stb.toString())


        // all data stored in json is passed to JSONArray
        title = json.getString("Title")
        year = json.getString("Year")
        rated = json.getString("Rated")
        released = json.getString("Released")
        runtime = json.getString("Runtime")
        genre = json.getString("Genre")
        director = json.getString("Director")
        writer = json.getString("Writer")
        actors = json.getString("Actors")
        plot = json.getString("Plot")
        Log.d("TAG", "extraction process successful")


        println("Title: '$title'\n" +
                "Year: $year\n" +
                "Rated: $rated\n" +
                "Released: $released\n" +
                "Runtime: $runtime\n" +
                "Genre: $genre\n" +
                "Director: $director\n" +
                "Writer: $writer\n" +
                "Actors: $actors\n" +
                "\n" +
                "Plot: $plot")

        var diaplayMovieDetail: String = "Title: \"$title\"\n" +
                "Year: $year\n" +
                "Rated: $rated\n" +
                "Released: $released\n" +
                "Runtime: $runtime\n" +
                "Genre: $genre\n" +
                "Director: $director\n" +
                "Writer: $writer\n" +
                "Actors: $actors\n" +
                "\n" +
                "Plot: \"$plot\""

        return diaplayMovieDetail
    }

}
















/*Student Id = 20200644
* UOW Id = w1833522
* */