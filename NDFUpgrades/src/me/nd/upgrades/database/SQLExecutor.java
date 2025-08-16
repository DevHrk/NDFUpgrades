package me.nd.upgrades.database;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SQLExecutor
{
    private final SQLConnector sqlConnector;

    public void updateQuery(String query, Consumer<SimpleStatement> consumer) {
        this.sqlConnector.consumeConnection(connection -> {
            try (SimpleStatement statement = SimpleStatement.of(connection.prepareStatement(query));){
                consumer.accept(statement);
                statement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateQuery(String query) {
        this.updateQuery(query, preparedStatement -> {});
    }

    public <T> T resultQuery(String query, Consumer<SimpleStatement> consumer, Function<SimpleResultSet, T> function) {
        AtomicReference<T> value = new AtomicReference<>();
        this.sqlConnector.consumeConnection(connection -> {
              try (SimpleStatement statement = SimpleStatement.of(connection.prepareStatement(query))) {
                consumer.accept(statement);
                try (SimpleResultSet resultSet = statement.executeQuery()) {
                  value.set(function.apply(resultSet));
                } catch (Exception e) {
                  e.printStackTrace();
                } 
              } catch (Exception e) {
                e.printStackTrace();
              } 
            });
        return value.get();
      }

    public <T> T resultOneQuery(String query, Consumer<SimpleStatement> consumer, Class<? extends SQLResultAdapter<T>> resultAdapterClass) {
        return (T)this.resultQuery(query, consumer, resultSet -> {
            if (resultSet.next()) {
                SQLResultAdapterProvider adapterProvider = SQLResultAdapterProvider.getInstance();
                Object adapter = adapterProvider.getAdapter(resultAdapterClass);
                return ((SQLResultAdapter<T>) adapter).adaptResult((SimpleResultSet)resultSet);
            }
            return null;
        });
    }

	public <T> Set<T> resultManyQuery(String query, Consumer<SimpleStatement> consumer, Class<? extends SQLResultAdapter<T>> resultAdapterClass) {
        return this.resultQuery(query, consumer, resultSet -> {
            SQLResultAdapterProvider adapterProvider = SQLResultAdapterProvider.getInstance();
            Object adapter = adapterProvider.getAdapter(resultAdapterClass);
            LinkedHashSet elements = new LinkedHashSet();
            while (resultSet.next()) {
                elements.add(((SQLResultAdapter<T>) adapter).adaptResult((SimpleResultSet)resultSet));
            }
            return elements;
        });
    }

    public SQLConnector getSqlConnector() {
        return this.sqlConnector;
    }

    public SQLExecutor(SQLConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }
}

