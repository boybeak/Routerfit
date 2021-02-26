package com.v2ex.api.callback

import com.github.boybeak.safecall.SafeCall
import retrofit2.Call
import retrofit2.Response

abstract class OnApiSuccess<R> : SafeCall.OnSuccess<R> {
    override fun onResponse(call: Call<R>, response: Response<R>) {
        val r = response.body()
        if (r != null) {
            onSuccess(r)
        }
    }

    abstract fun onSuccess(r: R)

}