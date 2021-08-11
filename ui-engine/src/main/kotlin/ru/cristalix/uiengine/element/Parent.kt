package ru.cristalix.uiengine.element

interface Parent {

    val children: List<AbstractElement>

    fun addChild(element: AbstractElement)

    fun removeChild(element: AbstractElement)

    fun addChild(vararg elements: AbstractElement)

    fun removeChild(vararg elements: AbstractElement)

    operator fun <T : AbstractElement> T.unaryPlus(): T {
        this@Parent.addChild(this)
        return this
    }

    operator fun <T: AbstractElement> plus(element: T): T {
        this@Parent.addChild(element)
        return element
    }

}