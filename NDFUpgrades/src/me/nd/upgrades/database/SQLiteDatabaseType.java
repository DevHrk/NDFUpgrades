package me.nd.upgrades.database;

import java.io.*;

public final class SQLiteDatabaseType extends FileDatabaseType
{
    public SQLiteDatabaseType(final File file) {
        super("org.sqlite.JDBC", "jdbc:sqlite:" + file, file);
        FileUtils.createFileIfNotExists(file);
    }
    
    public static SQLiteDatabaseTypeBuilder builder() {
        return new SQLiteDatabaseTypeBuilder();
    }
    
    public static class SQLiteDatabaseTypeBuilder
    {
        private File file;
        
        SQLiteDatabaseTypeBuilder() {
        }
        
        public SQLiteDatabaseTypeBuilder file(final File file) {
            this.file = file;
            return this;
        }
        
        public SQLiteDatabaseType build() {
            return new SQLiteDatabaseType(this.file);
        }
        
        @Override
        public String toString() {
            return "SQLiteDatabaseType.SQLiteDatabaseTypeBuilder(file=" + this.file + ")";
        }
    }
}
