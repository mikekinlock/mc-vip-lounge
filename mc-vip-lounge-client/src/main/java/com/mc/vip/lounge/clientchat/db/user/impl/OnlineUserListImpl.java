package com.mc.vip.lounge.clientchat.db.user.impl;

import javax.swing.*;

import com.mc.vip.lounge.clientchat.db.user.OnlineUserList;
import com.mc.vip.lounge.clientchat.model.CurrentClient;

public class OnlineUserListImpl implements OnlineUserList {

    private static final DefaultListModel<String> onlineList = new DefaultListModel<>();

    @Override
    public void updateUserList(final String[] users) {
        onlineList.removeAllElements();
        int index = 0;
        for (String user : users) {
            if (!user.equals(CurrentClient.getName())) {
                onlineList.add(index, user);
            }
        }
    }

    @Override
    public DefaultListModel<String> getUserList() {
        return onlineList;
    }
}
