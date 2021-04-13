import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.AbstractElement
import ru.cristalix.uiengine.element.child
import ru.cristalix.uiengine.utility.*

class TestMod: KotlinMod() {


    var AbstractElement.scale: Int
        get() = 0
        set(value) {
            scale = V(value, value, value)
        }

    override fun onEnable() {

        UIEngine.initialize(this)


        val rect = rectangle {

            mask = true

            size `` 20

            alignOrigin = CENTER


            add child circle {
                size = V(10, 10)
                color = Color("#ae55ff")
            }
        }

        UIEngine.overlayContext.addChild(rect)

    }

}