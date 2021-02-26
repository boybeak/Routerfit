package com.github.boybeak.irouter.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.boybeak.irouter.IRouter
import com.github.boybeak.irouter.core.annotation.Inject
import com.github.boybeak.irouter.core.annotation.RoutePath
import com.github.boybeak.routerfit.app.R

@RoutePath("app/user")
class UserActivity : AppCompatActivity() {

    @Inject("data")
    private var data: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        IRouter.inject(this, intent)

        Toast.makeText(this, "${data?.size ?: 0}", Toast.LENGTH_SHORT).show()

    }

    override fun finish() {
        setResult(RESULT_OK, Intent().putExtra("result", "I am result"))
        super.finish()
    }

}