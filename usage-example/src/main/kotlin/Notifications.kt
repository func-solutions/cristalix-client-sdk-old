import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.entry.ScriptMain
import ru.cristalix.uiengine.UIEngine

class Notifications : ScriptMain {

    override fun load(clientApi: ClientApi) {
        
        UIEngine.initialize(clientApi)

    }

    override fun unload() {}
}