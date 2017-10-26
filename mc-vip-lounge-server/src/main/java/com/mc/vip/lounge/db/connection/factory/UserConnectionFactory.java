package com.mc.vip.lounge.db.connection.factory;

import com.mc.vip.lounge.db.connection.UsersConnection;
import com.mc.vip.lounge.db.connection.impl.UsersConnectionImpl;

public class UserConnectionFactory {
    private UserConnectionFactory() {
    }

    public static UsersConnection getInstance() {
        return UsersConnectionImpl.getInstance();
    }
}
