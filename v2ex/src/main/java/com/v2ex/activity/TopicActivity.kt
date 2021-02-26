package com.v2ex.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.github.boybeak.irouter.IRouter
import com.github.boybeak.irouter.core.annotation.Inject
import com.github.boybeak.irouter.core.annotation.RoutePath
import com.v2ex.R
import com.v2ex.api.model.Topic

@RoutePath("topic/detail")
class TopicActivity : AppCompatActivity() {

    @Inject("topic")
    private var topic: Topic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        IRouter.inject(this, intent)

        findViewById<AppCompatTextView>(R.id.titleTV).text = topic!!.content

    }

}