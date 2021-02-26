package com.v2ex.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.v2ex.R
import com.v2ex.api.api
import com.v2ex.api.callback.OnApiSuccess
import com.v2ex.api.model.Topic
import com.v2ex.router.iRouter

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api.hotTopics().onSuccess(object : OnApiSuccess<List<Topic>>(){
            override fun onSuccess(r: List<Topic>) {
                iRouter.topicDetail(r[0]).startActivity(this@MainActivity)
            }
        }).onFailure { call, t ->
            Log.v(TAG, "onFailure thread=${Thread.currentThread().name}")
//            Toast.makeText(this@MainActivity, "onFailure", Toast.LENGTH_SHORT).show()
        }.onComplete {
            Log.v(TAG, "onComplete thread=${Thread.currentThread().name}")
//            Toast.makeText(this@MainActivity, "onComplete", Toast.LENGTH_SHORT).show()
        }.onStart {
//            Toast.makeText(this@MainActivity, "onStart", Toast.LENGTH_SHORT).show()
        }.observe(this)
    }
}