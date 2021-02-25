package com.github.boybeak.irouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class ResultManager {

    private static final String TAG_DELEGATE_FRAGMENT = "com.github.boybeak.irouter.TAG_DELEGATE_FRAGMENT",
            KEY_REDIRECT_INTENT = "com.github.boybeak.irouter.KEY_REDIRECT_INTENT",
            KEY_REQUEST_CODE = "com.github.boybeak.irouter.KEY_REQUEST_CODE",
            KEY_ID = "com.github.boybeak.irouter.KEY_REQUEST_ID";

    private static final ResultManager sManager = new ResultManager();
    static ResultManager getInstance() {
        return sManager;
    }

    private final Handler handler = new Handler();

    private final Map<String, Navigator.OnActivityResult> idCallbackMap = new HashMap<>();

    private ResultManager(){}

    void startActivityForResult(Context context, Intent intent, int requestCode, Navigator.OnActivityResult onActivityResult,
                                OnPreStartActivity onPreStartActivity) {
        String id = UUID.randomUUID().toString();
        if (context instanceof FragmentActivity) {
            startActivityForResult(id, (FragmentActivity)context, intent, requestCode, onPreStartActivity);
        } else {
            Intent delegateIt = new Intent(context, ResultDelegateActivity.class);
            delegateIt.putExtra(KEY_ID, id)
                    .putExtra(KEY_REDIRECT_INTENT, intent)
                    .putExtra(KEY_REQUEST_CODE, requestCode);
            if (!(context instanceof Activity)) {
                delegateIt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (onPreStartActivity != null && onPreStartActivity.onPreStart()) {
                return;
            }
            context.startActivity(delegateIt);
        }
        idCallbackMap.put(id, onActivityResult);
    }

    private void startActivityForResult(String id, FragmentActivity fragmentActivity, Intent intent,
                                        int requestCode, OnPreStartActivity onPreStartActivity) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        ResultDelegateFragment fragment = (ResultDelegateFragment)fm.findFragmentByTag(TAG_DELEGATE_FRAGMENT);
        if (fragment == null) {
            final ResultDelegateFragment newFragment = new ResultDelegateFragment();
            fm.beginTransaction().add(newFragment, TAG_DELEGATE_FRAGMENT).commitAllowingStateLoss();
            handler.post(() -> startActivityForResult(id, newFragment, intent, requestCode, onPreStartActivity));
        } else {
            startActivityForResult(id, fragment, intent, requestCode, onPreStartActivity);
        }
    }

    private void startActivityForResult(String id, ResultDelegateFragment fragment, Intent intent,
                                        int requestCode, OnPreStartActivity onPreStartActivity) {
        if (onPreStartActivity != null && onPreStartActivity.onPreStart()) {
            return;
        }
        fragment.setId(id);
        fragment.startActivityForResult(intent, requestCode);
    }

    Intent getRedirectIntent(Intent intent) {
        return intent.getParcelableExtra(KEY_REDIRECT_INTENT);
    }

    int getRequestCode(Intent intent) {
        return intent.getIntExtra(KEY_REQUEST_CODE, 64);
    }

    String getID(Intent intent) {
        return intent.getStringExtra(KEY_ID);
    }

    void onActivityResult(String id, int requestCode, int resultCode, Intent data) {
        idCallbackMap.remove(id).onActivityResult(requestCode, resultCode, data);
    }

}
