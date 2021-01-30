package ru.cristalix.uiengine

import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.item.ItemStack
import ru.cristalix.uiengine.element.*
import ru.cristalix.uiengine.utility.*

object UIEngine {

    private lateinit var clientApi: ClientApi

    fun initialize(clientApi: ClientApi) {
        this.clientApi = clientApi
    }

    fun rectangle(
        size: V3 = V3(),
        scale: V3 = V3(),
        offset: V3 = V3(),
        align: V3 = V3(),
        origin: V3 = V3(),
        color: Color = Color.TRANSPARENT,
        texture: Texture = EmptyTexture()
    ) : Rectangle {
        val data = RectangleData(
            size = size,
            scale = scale,
            offset = offset,
            align = align,
            origin = origin,
            color = color,
            texture = texture
        )
        return Rectangle(data)
    }

    fun text(
        scale: V3 = V3(),
        offset: V3 = V3(),
        align: V3 = V3(),
        origin: V3 = V3(),
        color: Color = Color.TRANSPARENT,
        label: String = "",
        autoFit: Boolean = false,
        shadow: Boolean = false
    ) : Text {
        val data = TextData(
            scale = scale,
            offset = offset,
            align = align,
            origin = origin,
            color = color,
            label = label,
            autoFit = autoFit,
            shadow = shadow
        )
        return Text(data)
    }

    fun item(
        scale: V3 = V3(),
        offset: V3 = V3(),
        align: V3 = V3(),
        origin: V3 = V3(),
        color: Color = Color.TRANSPARENT,
        stack: ItemStack
    ) : Item {
        val data = ItemData(
            scale = scale,
            offset = offset,
            align = align,
            origin = origin,
            color = color,
            stack = stack
        )
        return Item(data)
    }

}