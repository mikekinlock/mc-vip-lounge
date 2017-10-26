package com.mc.vip.lounge.clientchat.gui;

import com.mc.vip.lounge.clientchat.model.ClientChatRoom;

import javax.swing.JFrame;
import javax.swing.JTextField;

public interface ClientGUI {

    String getUserName();
    JFrame getFrame();
    JTextField getTextField();
    void setTextAreaText(String textArea);
    void addChatRoomToChatList(String identifier, ClientChatRoom newChatRoom);

}
