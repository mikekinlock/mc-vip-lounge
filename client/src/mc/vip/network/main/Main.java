package mc.vip.network.main;

import javax.swing.*;

import mc.vip.network.client.Client;

public class Main {

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
