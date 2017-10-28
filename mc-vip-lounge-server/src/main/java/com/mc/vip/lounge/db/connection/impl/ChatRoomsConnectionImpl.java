package com.mc.vip.lounge.db.connection.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mc.vip.lounge.db.connection.ChatRoomsConnection;
import com.mc.vip.lounge.model.ChatRoom;

public class ChatRoomsConnectionImpl implements ChatRoomsConnection {

    private static List<ChatRoom> chatRooms;

    public ChatRoomsConnectionImpl() {
        chatRooms = new CopyOnWriteArrayList<>();
    }

    @Override
    public Optional<ChatRoom> getChatRoomById(final String id) {
        return chatRooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }
}
