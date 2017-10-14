package com.mc.vip.lounge.clientchat.db.user.impl;

import com.mc.vip.lounge.clientchat.db.user.OnlineUserList;
import com.mc.vip.lounge.clientchat.model.CurrentClient;

import javax.swing.DefaultListModel;

public class OnlineUserListImpl implements OnlineUserList{

    private static final DefaultListModel<String> onlineList = new DefaultListModel<>();

    private OnlineUserListImpl(){ }

    public static OnlineUserList getUserListInstance(){
        return new OnlineUserListImpl();
    }

    @Override
    public void updateUserList(String[] users) {
        onlineList.removeAllElements();
        for(String user : users){
            if (!user.equals(CurrentClient.getName())){
                onlineList.addElement(user);
            }
        }
    }

    @Override
    public DefaultListModel<String> getUserList() {
        return onlineList;
    }
}
