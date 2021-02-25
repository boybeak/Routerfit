package com.github.boybeak.irouter.register;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Scanner {

    private Logger logger;

    private static Scanner sScanner = null;
    public static Scanner getInstance(Logger logger) {
        if (sScanner == null) {
            synchronized(Scanner.class) {
                if (sScanner == null) {
                    sScanner = new Scanner(logger);
                }
            }
        }
        return sScanner;
    }

    private List<String> loaderClzList = new ArrayList<>();
    private String loaderManagerEntryName = null;
    private File loaderManagerJar = null;

    private Scanner(Logger logger) {
        this.logger = logger;
    }

    public void scan(TransformInvocation transformInvocation, OnScanFinish onScanFinish) {
        for (TransformInput input : transformInvocation.getInputs()) {
            for (JarInput jarInput : input.getJarInputs()) {
                String destName = jarInput.getName();
                // rename jar files
                String hexName = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4);
                }

                File src = jarInput.getFile();
                File dest = transformInvocation.getOutputProvider()
                        .getContentLocation(destName + "_" + hexName, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);

                if (Scanner.shouldProcessPreDexJar(src.getAbsolutePath())) {
                    scanJar(src, dest);
                }

                try {
                    FileUtils.copyFile(src, dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                logger.lifecycle("directoryInput=" + directoryInput.getFile().getAbsolutePath());
                File expectDir = new File(directoryInput.getFile(), com.github.boybeak.irouter.register.Constants.LOADER_PACKAGE_PATH);
                if (expectDir.exists()) {
                    File[] loaders = expectDir.listFiles();
                    if (loaders == null) {
                        continue;
                    }
                    for (File loader : loaders) {
                        if (loader.isFile()) {
                            loaderClzList.add(formatToLoaderClass(loader.getAbsolutePath()));
                        }
                    }
                }

                File dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                try {
                    FileUtils.copyDirectory(directoryInput.getFile(), dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        onScanFinish.onScanFinish(loaderManagerJar, loaderManagerEntryName, loaderClzList);
    }

    private void scanJar(File jarFile, File destFile) {
        if (jarFile == null) {
            return;
        }
        try {
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.startsWith(com.github.boybeak.irouter.register.Constants.LOADER_PACKAGE_PATH)) {
                    loaderClzList.add(formatToLoaderClass(entryName));
                } else if (entryName.endsWith(com.github.boybeak.irouter.register.Constants.LOADER_MANAGER_PATH)) {
                    loaderManagerEntryName = entryName;
                    loaderManagerJar = destFile;
                }
            }
            jar.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void scanClass(File file) {
        logger.lifecycle("scanClass file=" + formatToLoaderClass(file.getAbsolutePath()));
        try {
            scanClass(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private String formatToLoaderClass(String name) {
        return com.github.boybeak.irouter.register.Constants.LOADER_PACKAGE_PATH + name.substring(name.lastIndexOf('/')).replaceAll(".class", "");
    }

    static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository");
    }

    static boolean shouldProcessClass(String entryName) {
        return entryName != null && entryName.startsWith(Constants.LOADER_PACKAGE_PATH);
    }

    interface OnScanFinish {
        void onScanFinish(File loaderManagerJar, String loaderManagerEntryName, List<String> loaders);
    }
}
