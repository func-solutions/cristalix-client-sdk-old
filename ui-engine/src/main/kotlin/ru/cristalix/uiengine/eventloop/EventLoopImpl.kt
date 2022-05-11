package ru.cristalix.uiengine.eventloop

import kotlin.properties.Delegates

class EventLoopImpl: EventLoop {

    companion object {
        // ахАХХАХХАХАХА неважно зачем это допустим прогард момент?
        fun EventLoopImpl.init() {
            runningTasks = arrayListOf()
            inEventLoop = false
            runningAnimations = arrayListOf()
        }
    }

    private lateinit var runningTasks: MutableList<Task>
    private var eventLoopBuffer: MutableList<Task>? = null
    private var inEventLoop by Delegates.notNull<Boolean>()
    override lateinit var runningAnimations: MutableList<Animation>
    override var animationContext: AnimationContext? = null

    init {
        init()
    }

    override fun schedule(delaySeconds: Double, action: () -> Unit): Task {
        val task = Task(System.currentTimeMillis() + (delaySeconds * 1000).toLong(), action)
        if (inEventLoop) {
            if (eventLoopBuffer == null) eventLoopBuffer = ArrayList(1)
            eventLoopBuffer!!.add(task)
        } else {
            runningTasks.add(task)
        }
        return task
    }

    override fun update() {
        val time = System.currentTimeMillis()

        val runningTasks = runningTasks
        if (runningTasks.isNotEmpty()) {
            inEventLoop = true
            with(runningTasks.iterator()) {
                while (hasNext()) {
                    val task = next()
                    if (task.cancelled) {
                        remove()
                        continue
                    }
                    if (time >= task.scheduledTo) {
                        remove()
                        try {
                            task.action()
                        } catch (ex: Exception) {
                            RuntimeException("Error while executing task", ex).printStackTrace()
                        }
                    }
                }
            }
            inEventLoop = false
            eventLoopBuffer?.apply { runningTasks.addAll(this) }
            eventLoopBuffer = null
        }

        val runningAnimations = runningAnimations
        if (runningAnimations.isNotEmpty()) {
            with(runningAnimations.iterator()) {
                while (hasNext()) {
                    if (!next().update(time)) remove()
                }
            }
        }
    }
}