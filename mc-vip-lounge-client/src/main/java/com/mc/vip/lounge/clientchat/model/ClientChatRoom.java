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
    private JTextArea messageArea = new JTextArea(8, 40);

    public ClientChatRoom(@NotNull final String... users) {
        if (users.length > 0) {
            this.isSelected = true;
            this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
            messageArea.setEditable(false);
            this.name = NO_NAME;
        }
    }
    public void setSelected(final boolean isSelected){
        this.isSelected = isSelected;
    }

    public void addUsers(String ... users){
        this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
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

    public void setName (final String name){
        this.name = name;
    }
    public String getName (){
        return this.name;
    }
}
