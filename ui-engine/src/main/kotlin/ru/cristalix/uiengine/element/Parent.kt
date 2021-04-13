package ru.cristalix.uiengine.element

interface Parent {

    val children: List<AbstractElement>

    fun addChild(vararg elements: AbstractElement)

    fun removeChild(vararg elements: AbstractElement)
}