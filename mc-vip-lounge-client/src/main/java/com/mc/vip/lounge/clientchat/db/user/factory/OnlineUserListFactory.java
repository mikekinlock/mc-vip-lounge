package com.mc.vip.lounge.clientchat.db.user.factory;

import com.mc.vip.lounge.clientchat.db.user.OnlineUserList;
import com.mc.vip.lounge.clientchat.db.user.impl.OnlineUserListImpl;

public class OnlineUserListFactory{

    private static OnlineUserList userList;

    public static OnlineUserList getInstance(){
        if (userList == null){
            userList = OnlineUserListImpl.getUserListInstance();
        }
        return userList;
    }
}
