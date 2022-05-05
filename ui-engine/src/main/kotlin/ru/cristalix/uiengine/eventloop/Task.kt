package ru.cristalix.uiengine.eventloop

class Task(
    val scheduledTo: Long,
    val action: () -> Unit,
    var cancelled: Boolean = false
)
