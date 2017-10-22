package com.mc.vip.lounge.clientchat.db.user.factory;

import com.mc.vip.lounge.clientchat.db.user.ClientChatRoomsList;
import com.mc.vip.lounge.clientchat.db.user.impl.ClientChatRoomsListImpl;

public class ClientChatRoomsListFactory {

    private ClientChatRoomsListFactory(){};

    public static ClientChatRoomsList getInstance(){
        return ClientChatRoomsListImpl.getInstance();
    }

}
