package com.mc.vip.lounge.clientchat.db.user;

import javax.swing.*;

public interface OnlineUserList {

    void updateUserList(final String[] users);

    DefaultListModel<String> getUserList();

}
