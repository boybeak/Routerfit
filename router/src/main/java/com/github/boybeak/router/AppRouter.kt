package com.github.boybeak.router

import com.github.boybeak.router.interceptor.NetInterceptor
import com.github.boybeak.irouter.Navigator
import com.github.boybeak.irouter.core.annotation.RouteTo

interface AppRouter {
    @RouteTo("app/user", interceptors = [
        NetInterceptor::class
    ])
    fun user(): Navigator

    @RouteTo("app/error")
    fun seeError(): Navigator
}