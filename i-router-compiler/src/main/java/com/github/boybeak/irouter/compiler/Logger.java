package com.github.boybeak.irouter.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public final class Logger {
    private Messager messager;
    public Logger(Messager messager) {
        this.messager = messager;
    }
    public void v (CharSequence text) {
        messager.printMessage(Diagnostic.Kind.NOTE, "\n" + text + "\n");
    }
    public void e (CharSequence error) {
        messager.printMessage(Diagnostic.Kind.ERROR, "\n" + error + "\n");
    }
}