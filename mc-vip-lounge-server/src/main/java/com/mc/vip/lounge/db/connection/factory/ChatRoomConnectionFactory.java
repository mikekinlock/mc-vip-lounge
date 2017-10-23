package com.mc.vip.lounge.db.connection.factory;

import com.mc.vip.lounge.db.connection.ChatRoomsConnection;
import com.mc.vip.lounge.db.connection.impl.ChatRoomsConnectionImpl;

public class ChatRoomConnectionFactory {
    private ChatRoomConnectionFactory(){}

    public static ChatRoomsConnection getInstance(){
        return ChatRoomsConnectionImpl.getInstance();
    }
}
