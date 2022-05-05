@file:Suppress("NOTHING_TO_INLINE")

package ru.cristalix.clientapi

import dev.xdark.clientapi.event.Event
import dev.xdark.clientapi.event.EventBus
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.network.PluginMessage
import io.netty.buffer.ByteBuf

context(JavaMod)
fun <T : Event> EventBus<T>.register(
    listener: Listener = this@JavaMod.listener,
    priority: Int = 1,
    handler: T.() -> Unit
) {
    register(listener, handler, priority)
}

context(JavaMod)
@Suppress("FunctionName")
fun <T : Event> _registerHandler(
    clazz: Class<T>,
    listener: Listener = this@JavaMod.listener,
    priority: Int = 1,
    handler: T.() -> Unit
) {
    Event.bus(clazz).register(listener, handler, priority)
}

context(JavaMod)
inline fun <reified T : Event> registerHandler(
    listener: Listener = this@JavaMod.listener,
    priority: Int = 1,
    noinline handler: T.() -> Unit
): Unit = _registerHandler(T::class.java, listener, priority, handler)

abstract class KotlinMod : JavaMod() {
    inline fun <reified T : Event> registerHandler(priority: Int = 1, noinline handler: T.() -> Unit) {
        registerHandler(listener, priority, handler)
    }

    fun registerChannel(channel: String, priority: Int = 1, handler: ByteBuf.() -> Unit) {
        registerHandler<PluginMessage>(priority) {
            if (this.channel == channel) {
                handler(data)
            }
        }
    }
}
