import dev.xdark.clientapi.ClientApi
import dev.xdark.clientapi.entry.ScriptMain
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.Element
import ru.cristalix.uiengine.element.Rectangle
import ru.cristalix.uiengine.element.Text
import ru.cristalix.uiengine.utility.Color
import ru.cristalix.uiengine.utility.V2
import ru.cristalix.uiengine.utility.V3
import ru.cristalix.uiengine.utility.WHITE

class Notifications : ScriptMain {

    override fun load(clientApi: ClientApi) {
        
        UIEngine.initialize(clientApi)

        val children: MutableList<Element> = ArrayList()
        var childY = 3.0

        val timeline = Rectangle(
            size = V2(150.0, 3.0),
            color = Color(1, 2, 3, 1.0)
        );

        childY += 3 // Timeline height
        children.add(timeline)

        childY += 5 // Title & head indent

        val isHeadEnabled = true
        if (isHeadEnabled) {
            val head = Rectangle(
                offset = V3(4.0, childY),
                color = Color(1, 1, 1, 1.0),
                size = V2(1.0, 4.0),
                textureFrom = V2(8.0 / 64.0, 8.0 / 64.0),
                textureSize = V2(8.0 / 64.0, 8.0 / 64.0),
            )

            val hat = Rectangle(
                offset = V3(x = 4.0, y = childY),
                color = WHITE,
                size = V2(x = 10.0, y = 10.0),
                textureFrom = V2(40.0 / 64, 8.0 / 64),
                textureSize = V2(8.0 / 64.0, 8.0 / 64.0),
            )

//            clientApi.skinManager().loadSkin(
//                com.mojang.authlib.minecraft.MinecraftProfileTexture('https://webdata.c7x.dev/textures/skin/' + notification.titleHeadUuid, {}),
//            ProfileTextureType.SKIN,
//            (type: ProfileTextureType, location: ResourceLocation, texture: ProfileTexture) => {
//                head.texture = location;
//                hat.texture = location;
//            }
//            );

            children.add(head)
            children.add(hat)
        }

        val titleText = Text({
            offset: { x: constants.leftIndent + (isHeadEnabled ? constants.headSize + constants.leftIndent : 0), y: childY },
            text: constants.titleColor + notification.title,
            scale: { x: 1.1, y: 1.1 }
        });
        children.push(titleText);
        childY += 11; // Title height

        let lines = notification.text;
        if (lines.length > 0) {
            childY += 3;
            for (let line of lines) {
                let text = gui.text({
                    offset: { x: constants.leftIndent, y: childY },
                    text: constants.textColor + line,
                    color: { r: 255 / 255, g: 255 / 255, b: 255 / 255, a: 0.5 }
                });
                children.push(text);
                childY += 10; // Text height
            }
        }

        let buttonX = constants.leftIndent;
        let buttonsViews: model.ButtonView[] = [];
        if (notification.buttons.length > 0) {
            childY += 8;
            for (let button of notification.buttons) {
                let label = gui.text({
                    text: button.label,
                    offset: { x: 5, y: 3 }
                });

                let buttonContainer = gui.rect({
                    offset: { x: buttonX, y: childY },
                    size: { x: 5 + fontRenderer.getStringWidth(button.label) + 5, y: 15 },
                    color: button.color ? button.color : notification.colorScheme.secondary,
                    children: [label],
                    onHover: (element, hovered) => element.setProperty(properties.colorA, hovered ? 0.6 : 1),
                    onClick: (element, down, key) => {
                    element.setProperty(properties.colorA, down ? 0.4 : 1);
                    let actions = button.actions;
                    if (!down) {
                        buttonView.key = undefined;
                        hide(view);
                        if (actions) {
                            for (let action of actions) {
                                action.do(key);
                            }
                        }
                    }
                }
                });

                buttonX += buttonContainer.properties[properties.sizeX];
                buttonX += constants.leftIndent;

                children.push(buttonContainer);
                let buttonView: model.ButtonView = {
                        button: button,
                        container: buttonContainer,
                        label: label
                };
                buttonsViews.push(buttonView);
            }
            childY += 15; // Buttons height
        }

        let lastNotificationY = 0;
        for (let item of values(notifications)) {
            let y = item.container.properties[properties.offsetY];
            let height = item.container.properties[properties.sizeY];
            if (y > lastNotificationY) {
                lastNotificationY = y + height;
            }
        }

        childY += 5;
        let notificationContainer = gui.rect({
            offset: {
            x: getOutscreenPosition(),
            y: lastNotificationY ? lastNotificationY + constants.notificationIndent : constants.stackIndent
        },
            size: { x: constants.notificationWidth, y: childY },
            color: notification.colorScheme.primary,
            children: children
        });

        let view = {
                visible: false,
                notification: notification,
                container: notificationContainer,
                timeline: timeline,
                buttons: buttonsViews,
                timerStarted: false
        };
        notifications[notification.id] = view;

        gui.addToOverlay(notificationContainer);

        show(view);

    }

    override fun unload() {}
}