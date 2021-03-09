# Анимации

UI engine предоставляет достаточно простой API для анимирования свойств объектов.

Вы можете изменять свойства элементов без анимации, просто меняя любые компоненты в них:
```kotlin
 element.align.x = 0.5
 element.color.alpha = 1.0
 element.offset.y = -300.0
```

Также вы можете изменять свойства целиком:
```kotlin
 element.align = Relative.CENTER
 element.color = BLACK
 element.offset = V3(100.0, -250.0)
```

Чтобы изменять свойства с течением времени, достаточно обернуть все эти вызовы
в метод `animate(durationInSeconds) {...}`:

```kotlin
element.animate(0.5) {
 element.align = Relative.CENTER
 element.color = BLACK
 element.offset = V3(100.0, -250.0)
}
```

Анимации поддерживают так называемые [easings](https://easings.net),
UI engine предоставляет большое количество встроенных easing-функций в объекте `Easings`

```kotlin
element.animate(1.0, Easings.BACK_OUT) { offset.x = 300.0 }
```

Анимации можно выстраивать друг за другом, используя синтаксис, похожий на `thenAccept()`
в `CompletableFuture`:

```kotlin
element.animate(1.0) {
    offset.x = 100.0
} then(2.0) {
    offset.x = 200.0
} then(3.0) then() {
    offset.x = 0.0
}
```

В вышеприведённом примере элемент сперва переместится на x = 100 за секунду, 
затем на x = 200 за две секунды, затем подождёт три секунды, и мгновенно окажется на x = 0.0

