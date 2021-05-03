package ru.cristalix.clientapi;

import dev.xdark.clientapi.ClientApi;
import dev.xdark.clientapi.entry.ModMain;
import dev.xdark.clientapi.event.Listener;
import dev.xdark.clientapi.event.network.PluginMessage;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JavaMod implements ModMain {

    public static ClientApi clientApi;

    public List<Runnable> onDisable = new ArrayList<>();
    public Listener listener;

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

        onEnable();

    }

    public void onEnable() { }

    @Override
    public final void unload() {
        for (Runnable runnable : onDisable)
            runnable.run();

        onDisable.clear();
    }

}
