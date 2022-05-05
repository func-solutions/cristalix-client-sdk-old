@file:Suppress("NOTHING_TO_INLINE")

package ru.cristalix.clientapi

import dev.xdark.clientapi.event.Event
import dev.xdark.clientapi.event.EventBus
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.network.PluginMessage
import io.netty.buffer.ByteBuf

context(JavaMod)
inline fun <T : Event> EventBus<T>.register(
    listener: Listener = this@JavaMod.listener,
    priority: Int = 1,
    noinline handler: T.() -> Unit
) {
    register(listener, handler, priority)
}

context(JavaMod)
inline fun <reified T : Event> registerHandler(
    listener: Listener = this@JavaMod.listener,
    priority: Int = 1,
    noinline handler: T.() -> Unit
) {
    Event.bus(T::class.java).register(listener, handler, priority)
}

abstract class KotlinMod : JavaMod() {
    inline fun <reified T : Event> registerHandler(priority: Int = 1, noinline handler: T.() -> Unit) {
        registerHandler(listener, priority, handler)
    }

    inline fun registerChannel(channel: String, priority: Int = 1, crossinline handler: ByteBuf.() -> Unit) {
        registerHandler<PluginMessage>(priority) {
            if (this.channel == channel) {
                handler(data)
            }
        }
    }
}
