package com.mc.vip.lounge.clientchat.model;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

public class ClientChatRoom {

    private boolean isSelected;
    private String id;
    private String conversation;

    public ClientChatRoom(@NotNull final String... users) {

        this.isSelected = true;
        this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
        this.conversation = "";

    }

    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;
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
