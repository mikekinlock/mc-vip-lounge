package com.mc.vip.lounge.db.connection.factory;

import com.mc.vip.lounge.db.connection.UsersConnection;
import com.mc.vip.lounge.db.connection.impl.UsersConnectionImpl;

public class UserConnectionFactory {

    private static UsersConnectionImpl instance;

    private UserConnectionFactory() {
    }

    public static UsersConnection getInstance() {
        if (instance == null) {
            instance = new UsersConnectionImpl();
        }
        return instance;
    }

}
