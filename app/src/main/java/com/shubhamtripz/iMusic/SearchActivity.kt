package com.shubhamtripz.iMusic

import android.annotation.SuppressLint
import android.app.AlertDialog.*
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shubhamtripz.mintsongspodcast.ApiSearchServices
import com.shubhamtripz.mintsongspodcast.Popular_song_model
import com.shubhamtripz.mintsongspodcast.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var searchRecyclerView: RecyclerView

    private lateinit var progressDialog:ProgressDialog

    private val apiService = ApiSearchServices.create()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        searchRecyclerView = findViewById(R.id.searchRecyclerView)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Searching")
        progressDialog.create()

//       val builder =AlertDialog.Builder(this)
//        builder.setView(R.layout.progress_dialog)
//      progressDialog=builder.create()


        searchButton.setOnClickListener {
            progressDialog.show()
//        mProgressDialog.show()
            val userInput = searchEditText.text.toString()
            performSearch(userInput)
        }

        val back2 = findViewById<ImageView>(R.id.back2btn)

        back2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performSearch(query: String) {
        val formattedQuery = query.trim()
        val apiCall = apiService.getUsersByTitle(formattedQuery)

        apiCall.enqueue(object : Callback<List<Popular_song_model>> {
            override fun onResponse(call: Call<List<Popular_song_model>>, response: Response<List<Popular_song_model>>) {
                if (response.isSuccessful) {
                    progressDialog.hide()
                    val searchResults = response.body()
                    displaySearchResults(searchResults)
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<List<Popular_song_model>>, t: Throwable) {
                // Handle network failure
            }
        })
    }


    private fun displaySearchResults(searchResults: List<Popular_song_model>?) {
        if (searchResults != null) {
            val adapter = SearchAdapter(searchResults) { clickedSong ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("title", clickedSong.Title)
                    putExtra("artist", clickedSong.Artist)
                    putExtra("imageUrl", clickedSong.Image)
                    putExtra("musicUrl", clickedSong.Link)
                }
                startActivity(intent)
            }

            searchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            searchRecyclerView.adapter = adapter
        }


    }
}

private fun ProgressDialog.setView(progressDialog: Int, i: Int, i1: Int, i2: Int, i3: Int) {

}
