package com.mc.vip.lounge.db.connection;

import java.util.List;
import java.util.Optional;

import com.mc.vip.lounge.model.ChatRoom;
import com.mc.vip.lounge.model.ChatUsers;

public interface ChatRoomsConnection {
    List<ChatRoom> getAllChatRooms();
    Optional<ChatRoom> getChatRoomById(final String id);
}
