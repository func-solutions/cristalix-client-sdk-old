package ru.cristalix.clientapi

import dev.xdark.clientapi.event.Event
import dev.xdark.clientapi.event.EventBus
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.network.PluginMessage
import io.netty.buffer.ByteBuf
import ru.cristalix.clientapi.KotlinModHolder.mod

fun <T : Event> EventBus<T>.register(
    listener: Listener = mod.listener,
    priority: Int = 1,
    handler: T.() -> Unit,
) {
    register(listener, handler, priority)
}

inline fun <reified T : Event> registerHandler(
    listener: Listener = mod.listener,
    priority: Int = 1,
    noinline handler: T.() -> Unit,
) {
    Event.bus(T::class.java).register(listener, handler, priority)
}

@Suppress("LeakingThis")
abstract class KotlinMod : JavaMod() {
    init {
        mod = this
    }

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
