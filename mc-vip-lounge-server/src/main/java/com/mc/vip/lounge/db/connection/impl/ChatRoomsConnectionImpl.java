package com.mc.vip.lounge.db.connection.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mc.vip.lounge.db.connection.ChatRoomsConnection;
import com.mc.vip.lounge.model.ChatRoom;

public class ChatRoomsConnectionImpl implements ChatRoomsConnection{

    private static List<ChatRoom> chatRooms;
    private static ChatRoomsConnectionImpl instance;

    private ChatRoomsConnectionImpl(){
        chatRooms = new CopyOnWriteArrayList<>();
    }

    public static ChatRoomsConnection getInstance(){
        if (instance == null){
            instance = new ChatRoomsConnectionImpl();
        }
        return instance;
    }

    @Override
    public List<ChatRoom> getAllChatRooms() {
        if (chatRooms == null){
            chatRooms = new CopyOnWriteArrayList<>();
        }
        return chatRooms;
    }

    @Override
    public Optional<ChatRoom> getChatRoomById(String id) {
        return chatRooms.stream()
                .filter(room-> room.getId().equals(id))
                .findFirst();
    }
}
