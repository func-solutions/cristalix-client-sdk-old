package ru.cristalix.uiengine.element

data class Task(
    val scheduledTo: Long,
    val action: () -> Unit,
    var cancelled: Boolean = false
)