package me.nd.upgrades.database;

public abstract class SQLDatabaseType
{
    private final String driverClassName;
    private final String jdbcUrl;
    
    public abstract SQLConnector connect();
    
    public String getDriverClassName() {
        return this.driverClassName;
    }
    
    public String getJdbcUrl() {
        return this.jdbcUrl;
    }
    
    public SQLDatabaseType(final String driverClassName, final String jdbcUrl) {
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
    }
}
