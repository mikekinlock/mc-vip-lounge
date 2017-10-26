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
    private String chatRecord;
    private boolean isActiv;

    public ChatRoom(@NotNull final String... users) {
        if (users.length > 0) {
            this.id = id;
            createUsersAndID(users);
            this.chatRecord = "";
        }
    }

    private void createUsersAndID(@NotNull String[] users) {
        List<ChatUsers> onlineUsers = UserConnectionFactory.getInstance().getAllUsers();
        for (String userName : users) {
            onlineUsers.stream()
                    .filter(user -> user.getUsername().equals(userName))
                    .forEach(user -> this.users.add(user));
        }
        this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
    }

    public boolean containsUser(String name) {
        return this.users.stream()
                .anyMatch(user -> user.getUsername().equals(name));
    }

    public void updateUser(String... users) {
        createUsersAndID(users);
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

    public String getChatRecord() {
        return chatRecord;
    }

    public void addMessage(String message) {
        chatRecord = chatRecord + message;
    }

    public boolean isActiv() {
        return isActiv;
    }

    public void setActiv(boolean activ) {
        isActiv = activ;
    }
}
