package com.github.boybeak.irouter;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

class ActivityStarter {

    ActivityStarter() {
    }

    void startActivity(Context context, Intent intent, Callback callback) {
        if (callback.onPreStart()) {
            return;
        }
        context.startActivity(intent);
        callback.onPostStart();
    }

    void startActivityForResult(Fragment fragment, Intent intent, int requestCode, Callback callback) {
        if (callback.onPreStart()) {
            return;
        }
        fragment.startActivityForResult(intent, requestCode);
        callback.onPostStart();
    }

    interface Callback {
        boolean onPreStart();
        void onPostStart();
    }

}
