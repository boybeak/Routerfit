package com.v2ex.router

import com.github.boybeak.irouter.IRouter
import com.v2ex.BuildConfig
import com.v2ex.activity.ErrorActivity

val iRouter = IRouter.Builder()
    .isDebug(BuildConfig.DEBUG)
    .errorActivity(ErrorActivity::class.java)
    .build()
    .create(IRouterService::class.java)
