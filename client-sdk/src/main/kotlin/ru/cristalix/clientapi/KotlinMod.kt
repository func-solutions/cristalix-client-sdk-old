package ru.cristalix.clientapi

import dev.xdark.clientapi.event.network.PluginMessage
import io.netty.buffer.ByteBuf

open class KotlinMod : JavaMod() {

    inline fun <reified T> registerHandler(priority: Int = 1, noinline handler: T.() -> Unit) {
        clientApi.eventBus().register(listener, T::class.java, handler, priority)
    }

    fun registerChannel(channel: String, priority: Int = 1, handler: ByteBuf.() -> Unit) {
        registerHandler<PluginMessage>(priority) {
            if (this.channel == channel) {
                handler(data)
            }
        }
    }

}