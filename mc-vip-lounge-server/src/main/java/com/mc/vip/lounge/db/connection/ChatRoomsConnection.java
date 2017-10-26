package com.mc.vip.lounge.db.connection;

import java.util.Optional;

import com.mc.vip.lounge.model.ChatRoom;

public interface ChatRoomsConnection {

    Optional<ChatRoom> getChatRoomById(final String id);
}
