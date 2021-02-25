package com.github.boybeak.irouter.register;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class IRouterRegister implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project project) {
        boolean isApp = project.getPlugins().hasPlugin(AppPlugin.class);
        if (isApp) {
            AppExtension android = project.getExtensions().getByType(AppExtension.class);

            com.github.boybeak.irouter.register.RegisterTransform transform = new RegisterTransform(project);

            android.registerTransform(transform, Collections.EMPTY_LIST);
        }
    }
}