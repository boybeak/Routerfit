package com.v2ex.router

import com.github.boybeak.irouter.Navigator
import com.github.boybeak.irouter.core.annotation.Key
import com.github.boybeak.irouter.core.annotation.RouteTo
import com.v2ex.api.model.Topic

interface IRouterService {
    @RouteTo("topic/detail")
    fun topicDetail(@Key("topic") topic: Topic): Navigator
}