package com.mc.vip.lounge.model;

public class ChatUsers {

    private String username;
    private boolean isOnline;
    private long id;

    public ChatUsers(final String username, final boolean isOnline){
        this.username = username;
        this.isOnline = isOnline;
    }

    public String getUsername(){
        return this.username;
    }

    public int compareTo(final ChatUsers compare){
        String firstUser = this.username;

        Comparable usersCompare = (Comparable<ChatUsers>) o -> {
            return firstUser.compareTo(o.getUsername());
        };

        return usersCompare.compareTo(compare);
    }
}
