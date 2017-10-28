package com.mc.vip.lounge.main;

import java.net.ServerSocket;

import com.mc.vip.lounge.server.Server;

public class ServerMain {

    /** The application main method, which just listens on a port and spawns handler threads. */
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(Server.PORT);
        boolean serverRunning = true;
        try {
            while (serverRunning) {
                Server.Handler handler = new Server.Handler(listener.accept());
                handler.start();
                if (handler.isClosed()) {
                    serverRunning = false;
                }

            }
        } finally {
            listener.close();
        }
    }
}
