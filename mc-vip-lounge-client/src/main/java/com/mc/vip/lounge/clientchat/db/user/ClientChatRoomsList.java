package com.mc.vip.lounge.clientchat.db.user;

import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface ClientChatRoomsList {

    List<ClientChatRoom> getAllClientChatRooms();
    Optional<ClientChatRoom> getSelectedChatRoom();
    Optional<ClientChatRoom> getRoomById(String id);
    void nonSelected();
}
