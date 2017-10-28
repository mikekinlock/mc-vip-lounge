package com.mc.vip.lounge.clientchat.model;

public class CurrentClient {
    private static String currentClientName;

    public static void setName(final String name) {
        currentClientName = name;
    }

    public static String getName() {
        return currentClientName;
    }
}
