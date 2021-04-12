import dev.xdark.clientapi.event.network.PluginMessage
import io.netty.buffer.ByteBuf
import ru.cristalix.clientapi.JavaMod

open class KotlinMod : JavaMod() {

    inline fun <reified T> registerHandler(priority: Int = 1, noinline handler: T.() -> Unit) {
        clientApi.eventBus().register(listener, T::class.java, handler, priority)
    }

    fun registerMessage(channel: String, priority: Int = 1, handler: ByteBuf.() -> Unit) {
        registerHandler<PluginMessage>(priority) {
            if (this.channel == channel) {
                handler(data)
            }
        }
    }

}