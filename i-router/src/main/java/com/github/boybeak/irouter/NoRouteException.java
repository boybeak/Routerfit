package com.github.boybeak.irouter;

public class NoRouteException extends RuntimeException {

    public static NoRouteException create(String path) {
        return new NoRouteException("No route target class found for path " + path);
    }

    private NoRouteException(String message) {
        super(message);
    }
}
