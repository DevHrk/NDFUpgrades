package me.nd.upgrades.database;

import java.sql.*;

public final class SimpleStatement implements AutoCloseable
{
    private final PreparedStatement preparedStatement;
    
    public static SimpleStatement of(final PreparedStatement preparedStatement) {
        return new SimpleStatement(preparedStatement);
    }
    
    public void set(final int parameterIndex, final Object value) {
        try {
            this.preparedStatement.setObject(parameterIndex, value);
        }
        catch (SQLException t) {
            t.printStackTrace();
        }
    }
    
    public void executeUpdate() {
        try {
            this.preparedStatement.executeUpdate();
        }
        catch (SQLException t) {
            t.printStackTrace();
        }
    }
    
    public SimpleResultSet executeQuery() {
        try {
            return new SimpleResultSet(this.preparedStatement.executeQuery());
        }
        catch (SQLException t) {
            t.printStackTrace();
            throw new NullPointerException("ResultSet can't be null");
        }
    }
    
    @Override
    public void close() throws Exception {
        this.preparedStatement.close();
    }
    
    public SimpleStatement(final PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
}
