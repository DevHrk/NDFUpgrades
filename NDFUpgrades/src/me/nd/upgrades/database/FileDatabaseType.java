package me.nd.upgrades.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;


public abstract class FileDatabaseType extends SQLDatabaseType {
    private final File file;

    public FileDatabaseType(String driverClassName, String jdbcUrl, File file) {
        super(driverClassName, jdbcUrl);
        this.file = file;
    }

    @Override
    public SQLConnector connect() {
        try {
            Class.forName(this.getDriverClassName());
            final Connection connection = DriverManager.getConnection(this.getJdbcUrl());
            return new SQLConnector(this){

                @Override
                public void consumeConnection(Consumer<Connection> consumer) {
                    consumer.accept(connection);
                }
            };
        }
        catch (ClassNotFoundException | SQLException t) {
            t.printStackTrace();
            throw new NullPointerException("connection can't be null");
        }
    }

    public File getFile() {
        return this.file;
    }
}
