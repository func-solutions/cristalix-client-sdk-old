package ru.cristalix.uiengine.eventloop

data class Task(
    val scheduledTo: Long,
    val action: () -> Unit,
    var cancelled: Boolean = false
)