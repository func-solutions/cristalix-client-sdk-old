package ru.cristalix.clientapi

import dev.xdark.clientapi.event.Event
import dev.xdark.clientapi.event.Listener
import dev.xdark.clientapi.event.network.PluginMessage
import io.netty.buffer.ByteBuf

inline fun <reified T : Event> registerHandler(
    listener: Listener,
    priority: Int = 1,
    noinline handler: T.() -> Unit
) {
    Event.bus(T::class.java).register(listener, handler, priority)
}


open class KotlinMod : JavaMod() {

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