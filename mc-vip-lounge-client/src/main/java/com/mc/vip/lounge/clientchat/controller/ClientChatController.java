package com.mc.vip.lounge.clientchat.controller;

import com.mc.vip.lounge.clientchat.chats.GroupChat;
import com.mc.vip.lounge.clientchat.gui.ClientGraficalInterface;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientChatController {

    private BufferedReader in;
    private PrintWriter out;
    private ClientGraficalInterface gui;

    private Logger CLIENT_LOG = Logger.getLogger(ClientGraficalInterface.class.getName());

    public ClientChatController(){
        gui = ClientGraficalInterface.getInstance();
    }

    /** Connects to the server then enters the processing loop. */
    public void run() throws IOException {
        Socket socket = null;
        try {
            // Make connection and initialize streams
            String serverAddress = getServerAddress();
            socket = new Socket(serverAddress, 9001);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            GroupChat groupChat = new GroupChat(in,out,gui);
            groupChat.listenGroupChat();

        } catch (Exception e) {
            CLIENT_LOG.log(Level.WARNING, "Problems by running the client: ", e);
        } finally {
            socket.close();
        }
    }

    public JFrame getFrame() {
        return ClientGraficalInterface.getInstance().getFrame();
    }


    /** Prompt for and return the address of the server. */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                gui.getFrame(),
                "Enter IP Address of the Server:",
                "Welcome to the Chatter",
                JOptionPane.QUESTION_MESSAGE);
    }
}
