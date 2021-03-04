package com.github.boybeak.irouter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.github.boybeak.irouter.core.LoaderManager;
import com.github.boybeak.irouter.core.annotation.Extras;
import com.github.boybeak.irouter.core.annotation.Inject;
import com.github.boybeak.irouter.core.annotation.Key;
import com.github.boybeak.irouter.core.annotation.RouteTo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class IRouter {

    private static final String TAG = IRouter.class.getSimpleName();

    private boolean isDebug = false;
    private Class<?> errorActivityClz = null;

    private IRouter(Class<?> errorActivityClz, boolean isDebug) {
        this.errorActivityClz = errorActivityClz;
        this.isDebug = isDebug;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return parseMethod(method, args);
                    }
                });
    }

    private Navigator parseMethod(Method method, Object[] args) {

        if (method.getReturnType() != Navigator.class) {
            throw new IllegalStateException("The method " + method.getName() + " must return Navigator");
        }

        RouteTo routeTo = method.getAnnotation(RouteTo.class);

        String path = routeTo.value();
        Class<? extends Interceptor>[] interceptorClasses = routeTo.interceptors();

        Navigator navigator = NavigatorCache.getInstance().obtain(path);

        if (interceptorClasses != null) {
            List<Interceptor> interceptorList = new ArrayList<>();
            for (Class<? extends Interceptor> clz : interceptorClasses) {
                try {
                    interceptorList.add(clz.newInstance());
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            navigator.addInterceptors(interceptorList);
        }

        if (isDebug) {
            navigator.putExtra(Constants.KEY_PATH, path);
        }

        if (navigator.getTargetClz() == null) {
            if (errorActivityClz == null) {
                throw new IllegalStateException("The target activity not found for " + path + ", and no errorActivity was set.");
            }
            navigator.setTargetClz(errorActivityClz);
            return navigator;
        }

        Annotation[][] annotations = method.getParameterAnnotations();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Annotation[] argAnnotations = annotations[i];
                Key key = getKeyAnnotation(argAnnotations, Key.class);
                Extras extras = getKeyAnnotation(argAnnotations, Extras.class);
                if (key != null && extras != null) {
                    throw new IllegalStateException("Can not set @Key and @Extras to the same parameter in method " + method.getName());
                }
                if (key != null) {
                    String keyStr = key.value();
                    if (TextUtils.isEmpty(keyStr)) {
                        throw new IllegalStateException("Empty key not support in method " + method.getName());
                    }
                    fillArg(navigator, keyStr, arg);
                } else if (extras != null) {
                    if (arg instanceof Bundle) {
                        navigator.putExtras((Bundle)arg);
                    } else if (arg instanceof Intent) {
                        navigator.putExtras((Intent)arg);
                    } else {
                        throw new IllegalStateException("@Extras can only be set to Bundle or Intent value");
                    }
                }
            }
        }

        return navigator;
    }

    @SuppressWarnings("unchecked")
    private <T> T getKeyAnnotation(Annotation[] annotations, Class<T> tClass) {
        T key = null;
        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            if (annotation.annotationType() == tClass) {
                key = (T)annotation;
            }
        }
        return key;
    }

    private void fillArg(Navigator navigator, String key, Object arg) {
        if (int.class.isInstance(arg)) {
            navigator.putExtra(key, (int)arg);
        } else if (arg instanceof Integer) {
            navigator.putExtra(key, (Integer) arg);
        } else if (long.class.isInstance(arg)) {
            navigator.putExtra(key, (long)arg);
        } else if (arg instanceof Long) {
            navigator.putExtra(key, (Long) arg);
        } else if (float.class.isInstance(arg)) {
            navigator.putExtra(key, (float)arg);
        } else if (arg instanceof Float) {
            navigator.putExtra(key, (Float) arg);
        } else if (double.class.isInstance(arg)) {
            navigator.putExtra(key, (double)arg);
        } else if (arg instanceof Double) {
            navigator.putExtra(key, (Double) arg);
        } else if (boolean.class.isInstance(arg)) {
            navigator.putExtra(key, (boolean)arg);
        } else if (arg instanceof Boolean) {
            navigator.putExtra(key, (Boolean) arg);
        } else if (char.class.isInstance(arg)) {
            navigator.putExtra(key, (char)arg);
        } else if (arg instanceof Character) {
            navigator.putExtra(key, (Character) arg);
        } else if (byte.class.isInstance(arg)) {
            navigator.putExtra(key, (byte)arg);
        } else if (arg instanceof Byte) {
            navigator.putExtra(key, (Byte) arg);
        } else if (short.class.isInstance(arg)) {
            navigator.putExtra(key, (short)arg);
        } else if (arg instanceof Short) {
            navigator.putExtra(key, (Short)arg);
        } else if (arg instanceof String) {
            navigator.putExtra(key, (String)arg);
        } else if (arg instanceof CharSequence) {
            navigator.putExtra(key, (CharSequence)arg);
        } else if (arg instanceof Parcelable) {
            navigator.putExtra(key, (Parcelable)arg);
        } else if (arg instanceof ArrayList) {
            ArrayList<?> list = (ArrayList<?>)arg;
            if (list.isEmpty()) {
                navigator.putIntegerArrayListExtra(key, (ArrayList<Integer>) list);
            } else {
                Object obj = list.get(0);
                if (int.class.isInstance(obj) || obj instanceof Integer) {
                    navigator.putIntegerArrayListExtra(key, (ArrayList<Integer>) obj);
                } else if (obj instanceof String) {
                    navigator.putStringArrayListExtra(key, (ArrayList<String>) obj);
                } else if (obj instanceof CharSequence) {
                    navigator.putCharSequenceArrayListExtra(key, (ArrayList<CharSequence>) obj);
                } else if (obj instanceof Parcelable) {
                    navigator.putParcelableArrayListExtra(key, (ArrayList<? extends Parcelable>) obj);
                } else {
                    throw new IllegalStateException("Unsupported data type " + obj.getClass().getName());
                }
            }
        }
    }

    public static class Builder {

        private Class<?> errorActivityClz;
        private boolean isDebug;

        public Builder errorActivity(String path) {
            Class<?> errorClz = LoaderManager.getInstance().get(path);
            if (errorClz == null) {
                throw NoRouteException.create(path);
            }
            return errorActivity(errorClz);
        }

        public Builder errorActivity(Class<?> errorActivityClz) {
            this.errorActivityClz = errorActivityClz;
            return this;
        }

        public Builder isDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public IRouter build() {
            return new IRouter(errorActivityClz, isDebug);
        }

    }

    public static void inject(Object obj, Intent intent) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            final int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                continue;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Inject inject = field.getAnnotation(Inject.class);
            try {
                field.set(obj, getValue(inject.value(), intent));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object getValue(String key, Intent intent) {
        return intent.getExtras().get(key);
    }

}
