package com.mc.vip.lounge.clientchat.model;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientChatRoom {

    private String[] users;
    private boolean isSelected;
    private String id;

    public ClientChatRoom(@NotNull final String... users) {
        if (users.length > 0) {
            this.users = users;
            this.isSelected = true;
            this.id = Arrays.stream(this.users).sorted().collect(Collectors.joining());
        }
    }

    public String[] getUsers() {
        return this.users;
    }

    public String getId() {
        return id;
    }

    public void setId (final String id){
        this.id = id;
    }

    public void setSelected(final boolean isSelected){
        this.isSelected = isSelected;
    }

    public boolean isSelected(){
        return this.isSelected;
    }
}
