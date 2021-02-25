package com.github.boybeak.irouter.core;

public interface Loader {
    String getHeader();
    Class<?> getTargetClass(String key);
}
