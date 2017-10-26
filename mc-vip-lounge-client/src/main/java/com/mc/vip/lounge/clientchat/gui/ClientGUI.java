package com.mc.vip.lounge.clientchat.gui;

import javax.swing.*;

import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

public interface ClientGUI {

    String getUserName();

    JFrame getFrame();

    JTextField getTextField();

    void setTextAreaText(String textArea);

    void addChatRoomToChatList(String identifier, ClientChatRoom newChatRoom);

}
