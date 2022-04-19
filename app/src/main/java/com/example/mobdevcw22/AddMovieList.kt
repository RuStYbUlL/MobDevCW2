package com.example.mobdevcw22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream



class AddMovieList : AppCompatActivity() {
    lateinit var jsonText : ListView;
    // array to store all values of json file
    var titleArray = arrayListOf<String>()
    var yearArray = arrayListOf<String>()
    var ratedArray = arrayListOf<String>()
    var releasedArray = arrayListOf<String>()
    var runtimeArray = arrayListOf<String>()
    var genreArray = arrayListOf<String>()
    var directorArray = arrayListOf<String>()
    var writerArray = arrayListOf<String>()
    var actorsArray = arrayListOf<String>()
    var plotArray = arrayListOf<String>()


//     the DAO for interacting with the movie database
    lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        jsonText = findViewById<ListView>(R.id.jsonList)

        //create the database
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "myMovieDB").build()
        movieDao = db.movieDao()

        Log.d("TAG", "Calling savemovieList function")
        saveMovieList()





    }

    fun saveMovieList(){
        getJson()

        // Save to DB
        runBlocking {
            withContext(Dispatchers.IO){
                for (i in 0..titleArray.size-1){
                    var movieArray = Movie(
                        0,
                        titleArray[i],
                        yearArray[i],
                        ratedArray[i],
                        releasedArray[i],
                        runtimeArray[i],
                        genreArray[i],
                        directorArray[i],
                        writerArray[i],
                        actorsArray[i],
                        plotArray[i]
                    )
                    movieDao.addMovie(movieArray)
                }
            }
        }
    }

    fun getJson() {


        // Read json file
        var json: String?

        try {
            val inputStream: InputStream = assets.open("movies.json")
            json = inputStream.bufferedReader().use { it.readText() }

            // all data stored in json is passed to JSONArray
            var jsonArray = JSONArray(json)
            for (i in 0..jsonArray.length() - 1) {

                // jsonObject accesses each value of json file
                val jsonObject = jsonArray.getJSONObject(i)

                // store all values of json file to array
                titleArray.add(jsonObject.getString("Title"))
                yearArray.add(jsonObject.getString("Year"))
                ratedArray.add(jsonObject.getString("Rated"))
                releasedArray.add(jsonObject.getString("Released"))
                runtimeArray.add(jsonObject.getString("Runtime"))
                genreArray.add(jsonObject.getString("Genre"))
                directorArray.add(jsonObject.getString("Director"))
                writerArray.add(jsonObject.getString("Writer"))
                actorsArray.add(jsonObject.getString("Actors"))
                plotArray.add(jsonObject.getString("Plot"))

            }

        } catch (e: IOException) {

        }
        // ArrayAdapter used for ListView
        var adapt = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, titleArray)
        jsonText.adapter = adapt
        Toast.makeText(applicationContext, "Movies added!", Toast.LENGTH_LONG).show()
    }


}















/*Student Id = 20200644
* UOW Id = w1833522
* */