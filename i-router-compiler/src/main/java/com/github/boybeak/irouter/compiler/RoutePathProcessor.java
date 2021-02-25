package com.github.boybeak.irouter.compiler;

import com.github.boybeak.irouter.core.BaseLoader;
import com.github.boybeak.irouter.core.annotation.RoutePath;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class RoutePathProcessor extends AbstractProcessor {

    private Pattern pathPtn = Pattern.compile("\\w+/\\w+");

    private Logger logger;
    private Types typeUtils;
    private Elements elementUtils;
    private TypeElement activityType;
    private String moduleName;

    private Filer outputFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        String mn = processingEnvironment.getOptions().get(Constants.MODULE_NAME);
        if (mn == null || mn.isEmpty()) {
            String msg = "javaCompileOptions {\n" +
                    "            annotationProcessorOptions {\n" +
                    "                arguments = [ROUTER_MODULE_NAME : project.getName()]\n" +
                    "            }\n" +
                    "        }";
            throw new IllegalStateException("You must set a ROUTER_MODULE_NAME in android{defaultConfig{}} block:" + msg);
        }
        moduleName = generateValidModuleName(mn);
        checkModuleName(moduleName);

        logger = new Logger(processingEnvironment.getMessager());

        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        activityType = elementUtils.getTypeElement("android.app.Activity");

        outputFiler = processingEnvironment.getFiler();

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(RoutePath.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) {
            return false;
        }
        logger.v(moduleName + " PathCache.class=" + PathCache.getInstance().getClass().hashCode() + " tid=" + Thread.currentThread().getName());

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(RoutePath.class);

        List<? extends TypeElement> typeElements = new ArrayList<>(ElementFilter.typesIn(elements));
        processTypes(typeElements);

        return true;
    }

    private void processTypes(List<? extends TypeElement> typeElements) {
        Map<String, TypeElement> tempCache = new HashMap<>();
        for (TypeElement type : typeElements) {
            if (!type.getModifiers().contains(Modifier.ABSTRACT) && typeUtils.isSubtype(type.asType(), activityType.asType())) {
                RoutePath routePath = type.getAnnotation(RoutePath.class);
                String path = routePath.value();
                checkPath(path);
                if (tempCache.containsKey(path)) {
                    throw new IllegalStateException("Conflict path " + path + " for " + type + " and " + tempCache.get(path));
                }
                tempCache.put(path, type);

                PathCache.getInstance().put(path, processingEnv.getElementUtils().getBinaryName(type));
            }
        }
        parsePathCache();
    }

    private void parsePathCache() {
        for (String key : PathCache.getInstance().keySet()) {
            PathCache.Tail tail = PathCache.getInstance().get(key);
            generateLoaderClass(key, tail);
        }
    }

    private void generateLoaderClass(String header, PathCache.Tail tail) {

        MethodSpec getHeaderMS = MethodSpec.methodBuilder("getHeader")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode("return $S;\n", header)
                .build();

        MethodSpec.Builder loadIntoMapMB = MethodSpec.methodBuilder("loadIntoMap")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        for (String tk : tail.keySet()) {
            loadIntoMapMB.addCode("load($S, $L.class);\n", tk, tail.get(tk));
        }

        TypeSpec loaderType = TypeSpec.classBuilder(moduleName + "$" + formatHeader(header) + "$Loader")
                .addModifiers(Modifier.PUBLIC)
                .superclass(BaseLoader.class)
                .addMethod(getHeaderMS)
                .addMethod(loadIntoMapMB.build())
                .build();
        JavaFile loaderFile = JavaFile.builder(
                "com.github.boybeak.irouter.loader",
                loaderType
        ).build();
        try {
//            JavacFiler javacFiler = (JavacFiler)outputFiler;
            loaderFile.writeTo(outputFiler);
//            outputFiler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkModuleName(String moduleName) {
        Pattern pattern = Pattern.compile("[\\w&&\\D]\\w*");
        if (!pattern.matcher(moduleName).matches()) {
            throw new IllegalStateException("Illegal module name " + moduleName);
        }
    }

    private void checkPath(String path) {
        if (!pathPtn.matcher(path).matches()) {
            throw new IllegalStateException("Illegal path, must contains only one /");
        }
    }

    private String generateValidModuleName(String moduleName) {
        StringBuilder moduleSB = new StringBuilder(moduleName);
        char first = moduleSB.charAt(0);
        if (first >= '0' && first <= '9') {
            moduleSB.insert(0, '_');
        }
        for (int i = 0; i < moduleSB.length(); i++) {
            char c = moduleSB.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                break;
            } else if (c >= 'a' && c <= 'z') {
                moduleSB.setCharAt(i, (char)(c - 32));
                break;
            }
        }
        return moduleSB.toString().replaceAll("\\W", "_");
    }

    private String formatHeader(String header) {
        StringBuilder builder = new StringBuilder(header);
        for (int i = 0; i < builder.length(); i++) {
            char c = builder.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                break;
            } else if (c >= 'a' && c <= 'z') {
                builder.setCharAt(i, (char)(c - 32));
                break;
            }
        }
        return builder.toString();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void println(Object object) {
        System.out.println(object);
    }

}
