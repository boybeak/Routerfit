package com.v2ex.api

import com.github.boybeak.safecall.SafeCallAdapterFactory
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val api = Retrofit.Builder()
    .baseUrl("https://www.v2ex.com/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(SafeCallAdapterFactory.create())
    .build()
    .create(ApiService::class.java)