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
    ) : RectangleView {
        val data = RectangleData(
            size = size,
            scale = scale,
            offset = offset,
            align = align,
            origin = origin,
            color = color,
            texture = texture
        )
        return RectangleView(data)
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
    ) : TextView {
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
        return TextView(data)
    }

    fun item(
        scale: V3 = V3(),
        offset: V3 = V3(),
        align: V3 = V3(),
        origin: V3 = V3(),
        color: Color = Color.TRANSPARENT,
        stack: ItemStack
    ) : ItemView {
        val data = ItemData(
            scale = scale,
            offset = offset,
            align = align,
            origin = origin,
            color = color,
            stack = stack
        )
        return ItemView(data)
    }

}