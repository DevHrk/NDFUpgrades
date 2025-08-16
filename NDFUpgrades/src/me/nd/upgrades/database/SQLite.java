package me.nd.upgrades.database;

import java.io.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

import me.nd.upgrades.Main;
import me.nd.upgrades.model.FactionModel;

import java.sql.*;
import java.util.function.Consumer;

public class SQLite {
    private static Connection connection;
    
    public static boolean openConnection() {
        FileConfiguration m = Main.get().getConfig();
        File file = new File("plugins/NDFUpgrades/"+m.getString("SQLite.file"));
        String url = ("jdbc:sqlite:" + file);
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] Conexão com §fSQLite §baberta com sucesso");
            createTables();
            return true; // Conexão estabelecida com sucesso
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] §cHouve um erro ao tentar fazer conexão com §6SQLite");
            return false; // Conexão não estabelecida
        }
    }
    
    public static void closeConnection() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] §cConexão com SQLite fechada com sucesso");
        }
        catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] §cOcorreu um erro ao tentar fechar a conexão com o SQLite, erro:");
            e.printStackTrace();
        }
    }

    public static boolean executeQuery(String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void createFacInDB(FactionModel factionModel) {
    	SQLite.executeQuery("INSERT INTO fupgrade VALUES( "+ factionModel.getPoder() +", "+ factionModel.getMembros() +", "
         + factionModel.getMobs() +", "+ factionModel.getMoedas() +", "+ factionModel.getVoar() +", "
    	  + factionModel.getPlantacao() +", "+ factionModel.getBlocos() +", "+ factionModel.getAttack() +", "
    	  + factionModel.getDefesas() +", "+ factionModel.getAlly()  +", '" + factionModel.getTag() +"' )");
    }

    public static void createTables() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS fupgrade(poder INT,membros INT,mobs INT,moedas INT,voar INT,plantacao INT,blocos INT,attack INT,defesas INT,ally INT,tag VARCHAR(16));");
            preparedStatement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void saveAll(FactionModel factionModel) {
        SQLite.saveFaction(factionModel);
    }
    
    public static void saveFaction(FactionModel factionModel) {
        String query = "INSERT OR REPLACE INTO fupgrade VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, factionModel.getPoder());
            preparedStatement.setInt(2, factionModel.getMembros());
            preparedStatement.setInt(3, factionModel.getMobs());
            preparedStatement.setInt(4, factionModel.getMoedas());
            preparedStatement.setInt(5, factionModel.getVoar());
            preparedStatement.setInt(6, factionModel.getPlantacao());
            preparedStatement.setInt(7, factionModel.getBlocos());
            preparedStatement.setInt(8, factionModel.getAttack());
            preparedStatement.setInt(9, factionModel.getDefesas());
            preparedStatement.setInt(10, factionModel.getAlly());
            preparedStatement.setString(11, factionModel.getTag());

            // Execute o PreparedStatement DENTRO do bloco try-with-resources
            preparedStatement.execute(); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static SQLExecutor getExecutor() {
    	FileConfiguration m = Main.get().getConfig();
    	File file = new File("plugins/NDFUpgrades/"+m.getString("SQLite.file"));
        SQLDatabaseType databaseType = new SQLDatabaseType("org.sqlite.JDBC", "jdbc:sqlite:" + file) {
            @Override
            public SQLConnector connect() {
                return new SQLConnector(this) {
                    @Override
                    public void consumeConnection(Consumer<Connection> consumer) {
                        try {
                            Class.forName(getDriverClassName());
                            Connection connection = DriverManager.getConnection(getJdbcUrl());
                            consumer.accept(connection);
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        };
        return new SQLExecutor(databaseType.connect());
    }
    
    public static Connection getConnection() {
        return connection;
    }
}
