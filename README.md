# IRouter ![Version](https://img.shields.io/badge/version-0.06-blue)
A new router. [Source code ananylize](https://boybeak.github.io/%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90%E7%B3%BB%E5%88%97/%E6%89%8B%E6%92%B8%E4%B8%80%E4%B8%AA%E8%B7%AF%E7%94%B1%E6%A1%86%E6%9E%B6IRouter.html).

# Install

```groovy
// project's build.gradle
buildscript {
    ext {
        i_router_version = '0.0.4'
    }
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.github.boybeak:i-router-register:$i_router_version"
    }
}
```

```groovy
// application's build.gradle
plugins {
  id 'kotlin-kapt'
  id 'i-router-register'
}

android {
  defaultConfig {
    javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ROUTER_MODULE_NAME : project.getName()]
            }
        }
  }
}

dependencies {
  implementation 'androidx.fragment:fragment:1.2.5'
  implementation "com.github.boybeak:i-router:$i_router_version"
  kapt "com.github.boybeak:i-router-compiler:$i_router_version"
}
```

```groovy
// other module's build.gradle
plugins {
  id 'kotlin-kapt'
}

android {
  defaultConfig {
    javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ROUTER_MODULE_NAME : project.getName()]
            }
        }
  }
}

dependencies {
  implementation 'androidx.fragment:fragment:1.2.5'
  implementation "com.github.boybeak:i-router:$i_router_version"
  kapt "com.github.boybeak:i-router-compiler:$i_router_version"
}
```



# Usage

Like **ARouter**, you must add  a **@RoutePath** annotation for the target activity.

```kotlin
@RoutePath("topic/detail")
class TopicActivity : AppCompatActivity() {

    @Inject("topic")
    private var topic: Topic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        IRouter.inject(this, intent)

        findViewById<AppCompatTextView>(R.id.titleTV).text = topic!!.content

    }
}
```

The values passed by intent, can be set a value by **@Inject** and `IRouter.inject(this, intent)`.



Like **Retrofit**, you must create an interface to declare "api".

```kotlin
interface IRouterService {
    @RouteTo("topic/detail")
    fun topicDetail(@Key("topic") topic: Topic): Navigator
}
```

Then, create a global `IRouterService` instance.

```kotlin
val iRouter = IRouter.Builder()
    .isDebug(BuildConfig.DEBUG)
    .errorActivity(ErrorActivity::class.java)
    .build()
    .create(IRouterService::class.java)
```

Finally, use the `iRouter`.

```kotlin
iRouter.topicDetail(topic).startActivity(this@MainActivity)

// OR

iRouter.topicDetail(topic).startActivityForResult(this, 100) { requestCode, resultCode, data ->
}
```