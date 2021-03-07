

# Начало работы

Клиент Cristalix поддерживает прогрузку модов двумя способами: с клиента и с сервера

## Компиляция модов

В клиенте Cristalix существуют некоторые ограничения относительно функций джавы, которые вы можете использовать.  
Клиент не поддерживает вызовы `invokedynamic` - лямбда-выражения использовать нельзя.  
Клиент не поддерживает никакие виды рефлексии, в том число использования методов в классе `Class`.

Для обхода этих ограничений можно использовать bundler-plugin. 
Он позволяет конвертировать любые моды в поддерживаемый формат, 
а также удаляет все известные виды рефлексии из классов котлина.

Бандлер поставляется как плагин для Gradle, для подробной информации о том, как работает его поключеие, 
смотрите в build.gradle данного проекта.

Ниже приведён минимальный build.gradle для разработки модов на котлине с использованием UI engine
 
```groovy
 buildscript {
     repositories {
         google()
         jcenter()
         maven {
             url "https://repo.implario.dev/public"
         }
     }
     dependencies {
         classpath("ru.cristalix:bundler:2.1.4")
     }
 }
 
 plugins {
     id 'org.jetbrains.kotlin.jvm' version '1.4.30'
 }
 
 repositories {
     mavenCentral()
     mavenLocal()
     maven {
         url 'https://repo.implario.dev/public'
     }
 }
 
 apply plugin: 'ru.cristalix.uiengine.bundler'
 apply plugin: 'kotlin'
 
 tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
     kotlinOptions {
         jvmTarget = "1.8"
     }
 }

 sourceCompatibility = '1.8'
 targetCompatibility = '1.8'

 tasks.withType(JavaCompile) {
     options.encoding = "UTF-8"
 }


 dependencies {

     implementation group: 'org.jetbrains.kotlin', name: 'kotlin-stdlib', version: '1.4.30'

     compileOnly 'dev.xdark:clientapi:1.0.6'
     compileOnly 'ru.cristalix:client-api-libs:all'
     
     // Актуальную версию см. на github.com/delfikpro/cristalix-ui-engine
     implementation 'ru.cristalix:uiengine:3.5.3' 

 }

 jar {
     from configurations.runtimeClasspath.collect { it.directory ? it : zipTree(it) }
 }

```

После подключения плагина в проекте появится новый таск с названием `bundle`.  
При его выполнении в папке `build` будет генерироваться сжатый сконвертированный мод 
с названием в формате `***-bundle.jar`  
Этот артифакт можно загружать на Cristalix.

## Отправка модов с сервера
Если у вас есть доступ к серверному коду Cristalix, то вы можете использовать CoreApi, 
чтобы отправлять моды игрокам, находящимся на вашем сервере

## Глобальные клиентские моды
Моды можно помещать в папку `.cristalix/updates/Minigames/mods`  
Клиент прогружает все моды из этой папки при запуске.

