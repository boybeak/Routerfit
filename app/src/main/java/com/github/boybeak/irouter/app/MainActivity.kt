package com.github.boybeak.irouter.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.boybeak.router.iRouter
import com.github.boybeak.routerfit.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun showUser(v: View) {
        iRouter.user().startActivityForResult(this, 100) { requestCode, resultCode, data ->
            Toast.makeText(this@MainActivity, data.getStringExtra("result"), Toast.LENGTH_SHORT).show()
        }
    }

}