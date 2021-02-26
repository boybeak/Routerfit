package com.v2ex.api

import com.github.boybeak.safecall.SafeCall
import com.v2ex.api.model.Topic
import retrofit2.http.GET

interface ApiService {
    @GET("topics/hot.json")
    fun hotTopics(): SafeCall<List<Topic>>
    @GET("topics/latest.json")
    fun latestTopics(): SafeCall<List<Topic>>
}