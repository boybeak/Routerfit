package com.github.boybeak.irouter.register;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class RegisterTransform extends Transform {

    private static final String TAG = RegisterTransform.class.getSimpleName();

    private Project project;
    private Logger logger;
    private com.github.boybeak.irouter.register.Scanner scanner;

    public RegisterTransform(Project project) {
        this.project = project;
        logger = project.getLogger();
        scanner = Scanner.getInstance(logger);
    }

    @Override
    public String getName() {
        return "RegisterTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        logger.lifecycle("transform running " + new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date()));

        scanner.scan(transformInvocation, (loaderManagerJar, loaderManagerEntryName, loaders) -> {
            Asm.getInstance().generateCode(loaderManagerJar, loaderManagerEntryName, loaders);
        });
    }

}
