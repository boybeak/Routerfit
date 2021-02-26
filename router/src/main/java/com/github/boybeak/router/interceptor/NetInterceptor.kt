package com.github.boybeak.router.interceptor

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.github.boybeak.irouter.Interceptor

class NetInterceptor : Interceptor {
    override fun intercept(context: Context, path: String, intent: Intent): Boolean {
        Toast.makeText(context, "Interceptored", Toast.LENGTH_SHORT).show()
        return false
    }
}