package me.nd.upgrades.database;

import java.util.*;

public final class SQLResultAdapterProvider
{
    private static final SQLResultAdapterProvider instance = new SQLResultAdapterProvider();
	private final Map<Class, SQLResultAdapter> adapterMap = new LinkedHashMap<Class, SQLResultAdapter>();

	public <T extends SQLResultAdapter> T getAdapter(Class<T> clazz) {
        return (T)this.adapterMap.computeIfAbsent(clazz, k -> {
            try {
                return (SQLResultAdapter)k.newInstance();
            }
            catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                throw new NullPointerException("adapter can't be null");
            }
        });
    }

    public static SQLResultAdapterProvider getInstance() {
        return instance;
    }
}
