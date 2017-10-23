package com.mc.vip.lounge.db.connection.impl;

import com.mc.vip.lounge.db.connection.UsersConnection;
import com.mc.vip.lounge.model.ChatUsers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UsersConnectionImpl implements UsersConnection{

    private static List<ChatUsers> users;
    private static UsersConnectionImpl instance;

    private UsersConnectionImpl(){ }

    public static UsersConnection getInstance(){
        if (instance == null){
            instance = new UsersConnectionImpl();
        }
        return instance;
    }

    @Override
    public List<ChatUsers> getAllUsers() {
        if (users == null){
            users = new CopyOnWriteArrayList<>();
        }
        return users;
    }
}
