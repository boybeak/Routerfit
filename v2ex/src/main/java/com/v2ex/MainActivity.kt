package com.v2ex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.v2ex.api.api
import com.v2ex.api.callback.OnApiSuccess
import com.v2ex.api.model.Topic

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api.hotTopics().onSuccess(object : OnApiSuccess<List<Topic>>() {
            override fun onSuccess(r: List<Topic>) {
                Toast.makeText(this@MainActivity, "${r.size}", Toast.LENGTH_SHORT).show()
            }
        }).observe(this)
    }
}