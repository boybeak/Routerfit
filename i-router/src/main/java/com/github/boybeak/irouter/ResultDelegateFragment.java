package com.github.boybeak.irouter;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultDelegateFragment extends Fragment {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ResultManager.getInstance().onActivityResult(id, requestCode, resultCode, data);
    }
}
