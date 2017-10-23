package com.mc.vip.lounge.clientchat.model;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientChatRoom {

    private String[] users;
    private boolean isSelected;
    private String id;
    private JTextArea messageArea = new JTextArea(8, 40);

    public ClientChatRoom(@NotNull final String... users) {
        if (users.length > 0) {
            this.users = users;
            this.isSelected = true;
            this.id = Arrays.stream(this.users).sorted().collect(Collectors.joining(","));
            messageArea.setEditable(false);

        }
    }
    public void setSelected(final boolean isSelected){
        this.isSelected = isSelected;
    }

    public String[] getUsersAsArray() {
        return this.users;
    }

    public JTextArea getMessageArea() {
        return this.messageArea;
    }

    public String getId() {
        return id;
    }

    public boolean isSelected(){
        return this.isSelected;
    }
}
