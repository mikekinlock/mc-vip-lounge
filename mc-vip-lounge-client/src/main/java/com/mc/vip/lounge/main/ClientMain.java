package com.mc.vip.lounge.main;

import javax.swing.*;

import com.mc.vip.lounge.clientchat.chats.GroupChat;
import com.mc.vip.lounge.clientchat.controller.ClientChatController;

public class ClientMain {

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        ClientChatController groupChat = new ClientChatController();
        groupChat.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        groupChat.getFrame().setVisible(true);
        groupChat.run();
    }
}
