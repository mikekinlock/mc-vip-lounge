package com.mc.vip.lounge.db.connection;

import com.mc.vip.lounge.model.ChatUsers;

import java.util.List;

public interface UsersConnection {
    List<ChatUsers> getAllUsers();
}
