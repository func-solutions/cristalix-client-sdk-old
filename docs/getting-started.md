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

plugins {
    id 'org.jetbrains.kotlin.jvm' version '1.4.30'
    id 'ru.cristalix.bundler' version '3.0.0'
}

repositories {
    maven {
        url 'https://repo.implario.dev/public'
    }
}

dependencies {

    implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.4.30'

    compileOnly 'ru.cristalix:client-api:latest-SNAPSHOT'

    implementation 'ru.cristalix:client-sdk:5.0.4'
    implementation 'ru.cristalix:uiengine:3.7.5'

}

jar.from configurations.runtimeClasspath.collect(project.&zipTree)

compileKotlin['kotlinOptions'].jvmTarget = "1.8"

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

