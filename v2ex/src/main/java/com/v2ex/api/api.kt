package com.v2ex.api

import com.github.boybeak.safecall.SafeCallAdapterFactory
import retrofit2.Retrofit

val api = Retrofit.Builder()
    .baseUrl("https://www.v2ex.com/api/")
    .addCallAdapterFactory(SafeCallAdapterFactory())
    .build()
    .create(ApiService::class.java)