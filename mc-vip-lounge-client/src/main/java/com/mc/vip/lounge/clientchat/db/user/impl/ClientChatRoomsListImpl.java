package com.mc.vip.lounge.clientchat.db.user.impl;

import com.mc.vip.lounge.clientchat.db.user.ClientChatRoomsList;
import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

import javax.swing.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientChatRoomsListImpl implements ClientChatRoomsList{

    private static List<ClientChatRoom> chatRooms;
    private static ClientChatRoomsList instance;

    private ClientChatRoomsListImpl(){
         chatRooms = new CopyOnWriteArrayList<>()   ;
    }

    public static ClientChatRoomsList getInstance(){
        if(instance == null){
            instance = new ClientChatRoomsListImpl();
        }
        return instance;
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
    public Optional<ClientChatRoom> getRoomById(String id) {
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
