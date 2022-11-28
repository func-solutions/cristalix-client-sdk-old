package ru.cristalix.uiengine.utility

import ru.cristalix.uiengine.element.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun rectangle(setup: RectangleElement.() -> Unit): RectangleElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return RectangleElement().also(setup)
}

inline fun text(setup: TextElement.() -> Unit): TextElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return TextElement().also(setup)
}

inline fun item(setup: ItemElement.() -> Unit): ItemElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return ItemElement().also(setup)
}

inline fun input(gui: ContextGui, setup: InputElement.() -> Unit): InputElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return InputElement(gui).also(setup)
}

inline fun cube(setup: CuboidElement.() -> Unit): CuboidElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return CuboidElement().also(setup)
}

inline fun sphere(setup: SphereElement.() -> Unit): SphereElement {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return SphereElement().also(setup)
}

inline fun carved(setup: CarvedRectangle.() -> Unit): CarvedRectangle {
    contract {
        callsInPlace(setup, InvocationKind.EXACTLY_ONCE)
    }
    return CarvedRectangle().also(setup)
}
