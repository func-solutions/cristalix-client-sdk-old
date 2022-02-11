package ru.cristalix.clientapi;

import dev.xdark.clientapi.ClientApi;
import dev.xdark.clientapi.entry.ModMain;
import dev.xdark.clientapi.event.Listener;
import dev.xdark.clientapi.event.network.PluginMessage;
import dev.xdark.clientapi.resource.ResourceLocation;
import dev.xdark.clientapi.texture.RenderEngine;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JavaMod implements ModMain {

    public static ClientApi clientApi;

    public List<Runnable> onDisable = new ArrayList<>();
    public Listener listener;

    /**
     * Bundler substitutes this check with a constant value.
     * This is required for client mods to work properly.
     */
    public static boolean isClientMod() {
        return Boolean.getBoolean("ru.cristalix.uiengine.no3dInit");
    }

    @Override
    public final void load(ClientApi clientApi) {

        JavaMod.clientApi = clientApi;

        listener = clientApi.eventBus().createListener();

        onDisable.add(new Runnable() {
            @Override
            public void run() {
                clientApi.eventBus().unregisterAll(listener);
            }
        });

        if (!isClientMod()) {
            String modClass = this.getClass().getName();

            clientApi.eventBus().register(listener, PluginMessage.class, new Consumer<PluginMessage>() {
                @Override
                public void accept(PluginMessage pluginMessage) {
                    if (pluginMessage.getChannel().equals("sdkreload")) {
                        ByteBuf data = pluginMessage.getData();
                        String clazz = NetUtil.readUtf8(data);
                        if (!clazz.equals(modClass)) {
                            data.resetReaderIndex();
                            return;
                        }

                        ByteBuf buffer = Unpooled.buffer();
                        NetUtil.writeUtf8(clazz, buffer);
                        clientApi.clientConnection().sendPayload("sdkconfirm", buffer);
                        unload();
                    }
                }
            }, 0);
        }

        onEnable();

    }

    public void onEnable() { }

    @Override
    public final void unload() {
        for (Runnable runnable : onDisable)
            runnable.run();

        onDisable.clear();
    }

    public static ResourceLocation loadTextureFromJar(ClientApi clientApi, String namespace, String address, String path) {
        try {
            if (!path.startsWith("/")) path = "/" + path;
            InputStream stream = JavaMod.class.getResourceAsStream(path);
            if (stream == null) {
                throw new IllegalArgumentException("No resource with path " + path + " found");
            }
            BufferedImage image = ImageIO.read(stream);

            ResourceLocation location = ResourceLocation.of(namespace, address);
            RenderEngine renderEngine = clientApi.renderEngine();
            renderEngine.deleteTexture(location);
            renderEngine.loadTexture(location, renderEngine.newImageTexture(image, false, false));
            return location;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResourceLocation loadTextureFromJar(String namespace, String address, String path) {
        return loadTextureFromJar(clientApi, namespace, address, path);
    }

}
