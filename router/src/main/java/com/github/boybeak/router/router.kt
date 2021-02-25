package com.github.boybeak.router

import com.github.boybeak.irouter.IRouter

val iRouter = IRouter.Builder()
    .errorActivity(ErrorActivity::class.java)
    .isDebug(true)
    .build()
    .create(AppRouter::class.java)