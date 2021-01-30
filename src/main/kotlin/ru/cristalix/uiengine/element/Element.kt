package ru.cristalix.uiengine.element

import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.Easing
import ru.cristalix.uiengine.utility.V3

interface Element {

    var scale: V3
    var offset: V3
    var align: V3
    var origin: V3
    var color: Color
    //var rotation: V3

}

open class ElementData(
    override var scale: V3,
    override var offset: V3,
    override var align: V3,
    override var origin: V3,
    override var color: Color
) : Element

open class ElementView<V : ElementView<V>>(
    private val element: Element
) : Element {

    override var scale: V3 = element.scale
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            element.scale = value
            field = value
            //updates.add(something)
        }
    override var offset: V3 = element.offset
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            element.offset = value
            field = value
            //updates.add(something)
        }
    override var align: V3 = element.align
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            element.align = value
            field = value
            //updates.add(something)
        }
    override var origin: V3 = element.origin
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            element.origin = value
            field = value
            //updates.add(something)
        }
    override var color: Color = element.color
        get() {
            //updates.add(something)
            return field
        }
        set(value) {
            element.color = value
            field = value
            //updates.add(something)
        }

    fun animate(
        duration: Number,
        easing: Easing = Easing.NONE,
        action: V.() -> Unit
    ) : V {
        // do something
        return getThis()
    }

    fun update(action: V.() -> Unit): V {
        TODO("Not yet implemented")
        return getThis()
    }

    infix fun then(action: () -> Unit): V {
        TODO("Not yet implemented")
        return getThis()
    }

    fun then(delay: Number, action: () -> Unit): V {
        TODO("Not yet implemented")
        return getThis()
    }

    @Suppress("UNCHECKED_CAST")
    fun getThis() = this as V

}