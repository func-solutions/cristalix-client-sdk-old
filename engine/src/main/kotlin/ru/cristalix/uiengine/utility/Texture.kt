package ru.cristalix.uiengine.utility

open class Texture(
    resource: String,
    start: V3,
    size: V3
)

class EmptyTexture() : Texture("", V3(), V3())