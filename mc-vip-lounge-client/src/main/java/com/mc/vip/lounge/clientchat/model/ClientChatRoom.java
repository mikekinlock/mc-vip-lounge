package com.mc.vip.lounge.clientchat.model;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientChatRoom {

    private static final String NO_NAME = "not_available";

    private boolean isSelected;
    private String id;
    private String name;
    private String conversation;

    public ClientChatRoom(@NotNull final String... users) {

        this.isSelected = true;
        this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
        this.name = NO_NAME;
        this.conversation = "";

    }

    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void addUsers(String... users) {
        this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
    }

    public String getMessages() {
        return this.conversation;
    }

    public void addMessage(final String currentMessage) {
        this.conversation = this.conversation + currentMessage;
    }

    public String getId() {
        return id;
    }

    public boolean isSelected() {
        return this.isSelected;
    }
}
