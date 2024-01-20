package com.shubhamtripz.iMusic

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class DetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var artistTextView: TextView
    private lateinit var playPauseButton: ImageView
    private lateinit var backbtn: ImageView
    private lateinit var musicSeekBar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private var isSeeking = false
    private var lastPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageView = findViewById(R.id.detailImageView)
        titleTextView = findViewById(R.id.detailTitleTextView)
        artistTextView = findViewById(R.id.detailArtistTextView)
        playPauseButton = findViewById(R.id.playPauseButton)
        musicSeekBar = findViewById(R.id.musicSeekBar)
        backbtn = findViewById(R.id.backbtn)

        // Backbutton handle
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val title = intent.getStringExtra("title")
        val artist = intent.getStringExtra("artist")
        val imageUrl = intent.getStringExtra("imageUrl")
        val musicUrl = intent.getStringExtra("musicUrl")

        titleTextView.text = title
        artistTextView.text = artist

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.musicholder)
            .into(imageView)

        mediaPlayer = MediaPlayer()

        // Set up SeekBar change listener
        musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = progress * 1000 // progress in milliseconds
                    mediaPlayer.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
            }
        })

        // Set up periodic update of SeekBar progress
        val updateHandler = android.os.Handler(mainLooper)
        updateHandler.postDelayed(object : Runnable {
            override fun run() {
                if (!isSeeking) {
                    musicSeekBar.progress = mediaPlayer.currentPosition / 1000 // progress in seconds
                }
                updateHandler.postDelayed(this, 1000) // update every second
            }
        }, 1000)

        // Create onBackPressedCallback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Add onBackPressedCallback to onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)



        playPauseButton.setOnClickListener {
            if (isPlaying) {
                // Save the current position when pausing
                lastPosition = mediaPlayer.currentPosition

                mediaPlayer.pause()
                playPauseButton.setImageResource(R.drawable.play)
            } else {
                mediaPlayer.reset()
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                try {
                    mediaPlayer.setDataSource(this, Uri.parse(musicUrl))
                    mediaPlayer.prepare()

                    // Set the last saved position when starting
                    mediaPlayer.seekTo(lastPosition)

                    mediaPlayer.start()

                    // Set the maximum value for the SeekBar based on the media duration
                    musicSeekBar.max = mediaPlayer.duration / 1000

                    playPauseButton.setImageResource(R.drawable.pause)
                } catch (e: Exception) {
                    Toast.makeText(this, " " + e.message, Toast.LENGTH_SHORT).show()
                }
            }
            isPlaying = !isPlaying
        }

    }

    override fun onStop() {
        super.onStop()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
