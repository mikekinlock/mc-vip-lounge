package com.mc.vip.lounge.main;

import javax.swing.*;

import com.mc.vip.lounge.network.Client;

public class ClientMain {

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.getFrame().setVisible(true);
        client.run();
    }
}
