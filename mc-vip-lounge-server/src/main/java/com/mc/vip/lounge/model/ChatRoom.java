package com.mc.vip.lounge.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class ChatRoom {

    private long id;
    private List <ChatUsers> users;
    private List <String> chatRecord;
    private boolean isActiv;

    public ChatRoom(@NotNull final long id, @NotNull final ArrayList users){
        if(users.size() > 0){
            this.id = id;
            this.users = users;
            this.chatRecord = new ArrayList<>();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
