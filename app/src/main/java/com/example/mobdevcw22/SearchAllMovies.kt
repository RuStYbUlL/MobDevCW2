package com.example.mobdevcw22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.EditText

class SearchAllMovies : AppCompatActivity() {

    var resultAllMovies: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_all_movies)

        //make scrollable textview
        resultAllMovies?.setMovementMethod(ScrollingMovementMethod())
    }
}