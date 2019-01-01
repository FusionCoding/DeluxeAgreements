package de.fusion.deluxeagreements.sql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractConnection {

    private final String name;

    public AbstractConnection(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void init();

    public abstract void shutdown() throws Exception;

    public abstract Connection getConnection() throws SQLException;

}
