package com.github.boybeak.irouter.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.boybeak.irouter.core.annotation.RoutePath
import com.github.boybeak.routerfit.app.R

@RoutePath("app/user")
class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val data = intent.getStringArrayListExtra("data")
    }

    override fun finish() {
        setResult(RESULT_OK, Intent().putExtra("result", "I am result"))
        super.finish()
    }

}