package me.nd.upgrades.database;

public interface SQLResultAdapter<T>
{
    T adaptResult(final SimpleResultSet p0);
}
