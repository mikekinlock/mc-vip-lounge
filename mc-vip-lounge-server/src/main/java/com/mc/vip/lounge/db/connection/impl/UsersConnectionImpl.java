package com.mc.vip.lounge.db.connection.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mc.vip.lounge.db.connection.UsersConnection;
import com.mc.vip.lounge.model.ChatUsers;

public class UsersConnectionImpl implements UsersConnection {

    private static List<ChatUsers> users;

    public UsersConnectionImpl() {
        users = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<ChatUsers> getAllUsers() {
        return users;
    }
}
