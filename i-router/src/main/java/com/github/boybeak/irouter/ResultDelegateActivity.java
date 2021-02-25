package com.github.boybeak.irouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class ResultDelegateActivity extends Activity {

    private String id = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent redirectIntent = ResultManager.getInstance().getRedirectIntent(getIntent());
        int requestCode = ResultManager.getInstance().getRequestCode(getIntent());
        id = ResultManager.getInstance().getID(getIntent());
        startActivityForResult(redirectIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ResultManager.getInstance().onActivityResult(id, requestCode, resultCode, data);
        finish();
    }
}
