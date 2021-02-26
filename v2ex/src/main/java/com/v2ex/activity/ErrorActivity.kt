package com.v2ex.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import com.github.boybeak.irouter.Constants
import com.github.boybeak.irouter.IRouter
import com.v2ex.R

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        findViewById<AppCompatTextView>(R.id.errorTV).text = intent.getStringExtra(Constants.KEY_PATH)
    }
}