package com.mc.vip.lounge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.mc.vip.lounge.db.connection.factory.UserConnectionFactory;

public class ChatRoom {

    private String id;
    private List<ChatUsers> users = new ArrayList<>();
    private List<String> chatRecord;
    private boolean isActiv;

    public ChatRoom(@NotNull final String... users) {
        if (users.length > 0) {
            this.id = id;
            List<ChatUsers> onlineUsers = UserConnectionFactory.getInstance().getAllUsers();
            for (String userName : users) {
                onlineUsers.stream()
                        .filter(user -> user.getUsername().equals(userName))
                        .forEach(user -> this.users.add(user));
            }
            this.chatRecord = new ArrayList<>();
            this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
        }
    }

    public String getId() {
        return id;
    }

    public List<ChatUsers> getUsers() {
        return users;
    }

    public void setUsers(List<ChatUsers> users) {
        this.users = users;
    }

    public List<String> getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(List<String> chatRecord) {
        this.chatRecord = chatRecord;
    }

    public boolean isActiv() {
        return isActiv;
    }

    public void setActiv(boolean activ) {
        isActiv = activ;
    }
}
