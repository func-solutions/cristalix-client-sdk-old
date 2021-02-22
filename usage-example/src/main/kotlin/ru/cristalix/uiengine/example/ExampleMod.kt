package ru.cristalix.uiengine.example

import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.entry.ModMain
import dev.xdark.clientapi.event.input.KeyPress
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.RectangleElement
import ru.cristalix.uiengine.element.animate
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.V3
import ru.cristalix.uiengine.utility.onLeftClick

class ExampleMod : ModMain {

    override fun load(clientApi: ClientApi) {
        UIEngine.initialize(clientApi)
        val rect = RectangleElement {

            onLeftClick = {  }

            origin = V3(0.5, 0.5)
            align = V3(0.5, 0.5)
            size = V3(240.0, 240.0)
            color = Color(alpha = 0.7, red = 0, green = 0, blue = 0)
        }

        UIEngine.overlayContext.addChild(rect)

        UIEngine.registerHandler(KeyPress::class.java, {
            println("${this.key} pressed")
            rect.animate(0.5) {
                offset.x = 100.0 + Math.random() * 100.0
                offset.y = 100.0 + Math.random() * 100.0
            }
        })

    }

    override fun unload() {
        UIEngine.uninitialize()
    }
}
