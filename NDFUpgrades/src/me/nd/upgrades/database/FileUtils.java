package me.nd.upgrades.database;

import java.io.*;

public final class FileUtils
{
    public static void createFileIfNotExists(final File file) {
        try {
            final File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private FileUtils() {
    }
}
