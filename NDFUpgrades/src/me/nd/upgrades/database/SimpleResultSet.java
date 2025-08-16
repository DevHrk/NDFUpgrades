package me.nd.upgrades.database;

import java.sql.*;

public final class SimpleResultSet implements AutoCloseable
{
    private final ResultSet resultSet;
    
    public static SimpleResultSet of(final ResultSet resultSet) {
        return new SimpleResultSet(resultSet);
    }
    
	public <T> T get(final String column) {
        try {
            if (this.resultSet.isBeforeFirst()) {
                throw new UnsupportedOperationException("ResultSet hasn't any result, use next() to search first result!");
            }
            return (T)this.resultSet.getObject(column);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("\"" + column + "\" no has element");
        }
    }
    
    public boolean next() {
        try {
            return this.resultSet.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void close() throws Exception {
        this.resultSet.close();
    }
    
    public SimpleResultSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
