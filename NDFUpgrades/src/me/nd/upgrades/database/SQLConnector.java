package me.nd.upgrades.database;

import java.sql.Connection;
import java.util.function.Consumer;


public abstract class SQLConnector
{
    private final SQLDatabaseType databaseType;

    public abstract void consumeConnection(Consumer<Connection> var1);

    public SQLDatabaseType getDatabaseType() {
        return this.databaseType;
    }

    public SQLConnector(SQLDatabaseType databaseType) {
        this.databaseType = databaseType;
    }
}

