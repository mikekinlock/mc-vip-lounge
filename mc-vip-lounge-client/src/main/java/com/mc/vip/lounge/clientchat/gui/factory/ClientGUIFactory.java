package com.mc.vip.lounge.clientchat.gui.factory;

import com.mc.vip.lounge.clientchat.gui.ClientGUI;
import com.mc.vip.lounge.clientchat.gui.impl.ClientGUIImpl;

public class ClientGUIFactory {

    private static ClientGUI instance;

    private ClientGUIFactory() {
    }

    public static ClientGUI getInstance() {
        if (instance == null) {
            instance = new ClientGUIImpl();
        }
        return instance;
    }

}
