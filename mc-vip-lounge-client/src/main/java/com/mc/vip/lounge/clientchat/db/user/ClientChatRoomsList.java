package com.mc.vip.lounge.clientchat.db.user;

import java.util.List;
import java.util.Optional;

import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

public interface ClientChatRoomsList {

    List<ClientChatRoom> getAllClientChatRooms();
    Optional<ClientChatRoom> getSelectedChatRoom();
    Optional<ClientChatRoom> getRoomById(String id);
    void nonSelected();
}
