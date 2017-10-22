package com.mc.vip.lounge.clientchat.db.user;

import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

import java.util.List;
import java.util.Optional;

public interface ClientChatRoomsList {

    List<ClientChatRoom> getAllClientChatRooms();
    void nonSelected();
    Optional<ClientChatRoom> getSelectedChatRoom();
}
