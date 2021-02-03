package ru.cristalix.uiengine.example;

import dev.xdark.clientapi.ClientApi;
import dev.xdark.clientapi.entry.ModMain;
import ru.cristalix.uiengine.UIEngine;
import ru.cristalix.uiengine.element.Rectangle
import ru.cristalix.uiengine.element.Text
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3

class ExampleMod : ModMain {

    override fun load(clientApi: ClientApi) {
        UIEngine.initialize(clientApi)
        UIEngine.overlayContext.addChild(
            Rectangle(
                size = V2(100.0, 100.0),
                color = Color(alpha = 0.5, red = 255, green = 100, blue = 90),
                children = listOf(
                    Text(
                        content = "Cristalix UI Engine v3",
                        align = V3(0.5, 0.5),
                        origin = V3(0.5, 0.5)
                    )
                )
            )
        )
    }

    override fun unload() {
        UIEngine.uninitialize()
    }
}
