package com.github.boybeak.irouter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public interface Interceptor {
    /**
     * @param context the context passed by Navigator's startActivity or startActivityForResult method,
     *                do not hold a instance of it for long time in case of memory leak.
     * @param path this path that intent to go.
     * @param intent the generated intent.
     * @return return true if intercept, false if not.
     * @see Navigator#startActivity(Context context)
     * @see Navigator#startActivityForResult(Context context, int requestCode, Navigator.OnActivityResult onActivityResult)
     */
    boolean intercept(@NonNull Context context, @NonNull String path, @NonNull Intent intent);
}
