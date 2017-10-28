package com.mc.vip.lounge.clientchat.db.user.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mc.vip.lounge.clientchat.db.user.ClientChatRoomsList;
import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

public class ClientChatRoomsListImpl implements ClientChatRoomsList {

    private static List<ClientChatRoom> chatRooms;

    public ClientChatRoomsListImpl() {
        chatRooms = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<ClientChatRoom> getAllClientChatRooms() {
        return chatRooms;
    }

    @Override
    public Optional<ClientChatRoom> getSelectedChatRoom() {
        return chatRooms.stream()
                .filter(room -> room.isSelected())
                .findFirst();
    }

    @Override
    public Optional<ClientChatRoom> getRoomById(final String id) {
        return chatRooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }

    @Override
    public void nonSelected() {
        chatRooms.stream()
                .forEach(room -> room.setSelected(false));
    }
}
