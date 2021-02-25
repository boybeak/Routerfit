package com.github.boybeak.router

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.github.boybeak.irouter.Constants

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        findViewById<TextView>(R.id.pathTV).text = intent.getStringExtra(Constants.KEY_PATH)
    }
}