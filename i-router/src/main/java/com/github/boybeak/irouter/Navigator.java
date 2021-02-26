package com.github.boybeak.irouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Navigator {

    private static final String TAG = Navigator.class.getSimpleName();

    private String path = null;

    private Intent intent = new Intent();
    private Class<?> targetClz;
    private final List<Interceptor> interceptors = new ArrayList<>();

    private boolean isDirty = false, isRecycled = false;

    Navigator(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    Navigator setTargetClz(Class<?> targetClz) {
        this.targetClz = targetClz;
        return this;
    }

    void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public Class<?> getTargetClz() {
        return targetClz;
    }

    public void startActivity(Context context) {
        if (isRecycled()) {
            throw new IllegalStateException("Do not use a recycled navigator");
        }
        intent.setClass(context, targetClz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        new ActivityStarter().startActivity(context, intent, new ActivityStarter.Callback() {
            @Override
            public boolean onPreStart() {
                return actionInterceptors(context);
            }

            @Override
            public void onPostStart() {
                recycle();
            }
        });
    }

    public void startActivityForResult(Context context, int requestCode, Navigator.OnActivityResult onActivityResult) {
        if (isRecycled()) {
            throw new IllegalStateException("Do not use a recycled navigator");
        }
        intent.setClass(context, targetClz);
        ResultManager.getInstance().startActivityForResult(context, intent, requestCode,
                onActivityResult, this);
    }

    boolean actionInterceptors(Context context) {
        for (Interceptor interceptor : interceptors) {
            if (interceptor.intercept(context, path, intent)) {
                return true;
            }
        }
        return false;
    }

    public Navigator addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public Navigator addInterceptors(Collection<Interceptor> interceptors) {
        this.interceptors.addAll(interceptors);
        return this;
    }

    public Navigator addCategory(String category) {
        intent.addCategory(category);
        return this;
    }
    public Navigator addFlags(int flags) {
        intent.addFlags(flags);
        return this;
    }
    public Navigator putExtra(String key, boolean value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, char value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, byte value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, short value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, long value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, float value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, double value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, CharSequence value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putExtra(String key, Parcelable[] value) {
        intent.putExtra(key, value);
        return this;
    }
    public Navigator putIntegerArrayListExtra(String key, ArrayList<Integer> value) {
        intent.putIntegerArrayListExtra(key, value);
        return this;
    }
    public Navigator putStringArrayListExtra(String key, ArrayList<String> value) {
        intent.putStringArrayListExtra(key, value);
        return this;
    }
    public Navigator putCharSequenceArrayListExtra(String key, ArrayList<CharSequence> value) {
        intent.putCharSequenceArrayListExtra(key, value);
        return this;
    }
    public Navigator putParcelableArrayListExtra(String key, ArrayList<? extends Parcelable> value) {
        intent.putParcelableArrayListExtra(key, value);
        return this;
    }

    public Navigator putExtras(Bundle extras) {
        intent.putExtras(extras);
        return this;
    }
    public Navigator putExtras(Intent it) {
        intent.putExtras(it);
        return this;
    }

    void recycle() {
        interceptors.clear();
        intent = new Intent();
        if (isDirty) {
            path = null;
            targetClz = null;
            isDirty = false;
        } else {
            NavigatorCache.getInstance().put(this);
        }
        isRecycled = true;
    }

    boolean isRecycled() {
        return isRecycled;
    }

    void reset() {
        isRecycled = false;
    }

    /*boolean realStartActivity(Context context) {
        if (actionInterceptors(context)) {
            recycle();
            return false;
        }
        context.startActivity(intent);
        recycle();
        return true;
    }

    boolean realStartActivityForResult(Fragment fragment, int requestCode) {
        if (actionInterceptors(fragment.requireContext())) {
            recycle();
            return false;
        }
        fragment.startActivityForResult(intent, requestCode);
        recycle();
        return true;
    }*/

    public interface OnActivityResult {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
