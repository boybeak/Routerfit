package com.github.boybeak.irouter;

public interface OnPreStartActivity {
    /**
     * @return true if you want to prevent start activity
     */
    boolean onPreStart();
}
