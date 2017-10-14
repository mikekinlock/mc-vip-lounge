package com.mc.vip.lounge.clientchat.db.user.impl;

import com.mc.vip.lounge.clientchat.db.user.OnlineUserList;

import javax.swing.DefaultListModel;

public class OnlineUserListImpl implements OnlineUserList{

    private static final DefaultListModel<String> onlineList = new DefaultListModel<>();

    private OnlineUserListImpl(){

    }

    public static OnlineUserList getUserListInstance(){
        return new OnlineUserListImpl();
    }

    @Override
    public void updateUserList(String[] users) {
        onlineList.removeAllElements();
        for(String user : users){
            onlineList.addElement(user);
        }
    }

    @Override
    public DefaultListModel<String> getUserList() {
        return onlineList;
    }
}
