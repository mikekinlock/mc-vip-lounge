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

    public ChatRoom(@NotNull final String... users) {
        if (users.length > 0) {
            createUsersAndID(users);
            this.chatRecord = "";
        }
    }

    public boolean containsUser(final String name) {
        return this.users.stream()
                .anyMatch(user -> user.getUsername().equals(name));
    }

    public void updateUser(final String... users) {
        createUsersAndID(users);
    }

    public String getId() {
        return id;
    }

    public List<ChatUsers> getUsers() {
        return users;
    }

    public String getChatRecord() {
        return chatRecord;
    }

    public void addMessage(final String message) {
        chatRecord = chatRecord + message;
    }

    private void createUsersAndID(@NotNull final String[] users) {
        List<ChatUsers> onlineUsers = UserConnectionFactory.getInstance().getAllUsers();
        for (String userName : users) {
            onlineUsers.stream()
                    .filter(user -> user.getUsername().equals(userName))
                    .forEach(user -> this.users.add(user));
        }
        this.id = Arrays.stream(users).sorted().collect(Collectors.joining(","));
    }
}
