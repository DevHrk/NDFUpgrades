package me.nd.upgrades.dao;

import me.nd.upgrades.dao.adapter.FactionAdapter;
import me.nd.upgrades.database.SQLExecutor;
import me.nd.upgrades.database.SQLite;
import me.nd.upgrades.model.FactionModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.bukkit.Bukkit;

public class FactionDAO {
    private static SQLExecutor sqlExecutor;
	
	public void setupFactions() {
	    try {
	      PreparedStatement stm = SQLite.getConnection().prepareStatement("SELECT * FROM fupgrade");
	      ResultSet rs = stm.executeQuery();
	      while (rs.next()) {
	    	  String tag = rs.getString("tag");
		        stm.setString(0, tag);
        }
	      Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] Todas as facções foram carregadas.");
	    } catch (SQLException ex) {
	      ex.printStackTrace();
	      Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] §cErro ao carregar as facções.");
	    } 
	  }
    
    public static void deleteFac(final FactionModel factionModel) {
        PreparedStatement stm = null;
        try {
            stm = SQLite.getConnection().prepareStatement("DELETE FROM fupgrade WHERE tag = ?");
            stm.setString(1, factionModel.getTag());
            stm.executeUpdate();
        }
        catch (SQLException ex) {}
    }
    
    public static void deleteAll(final FactionModel factionModel) {
        PreparedStatement stm = null;
        try {
            stm = SQLite.getConnection().prepareStatement("DELETE FROM fupgrade poder = ?, membros = ?, mobs = ?, moedas = ?, voar = ?, plantacao = ?, blocos = ?, attack = ?, defesas = ?, ally = ?  WHERE tag = ?");
            stm.setInt(1, factionModel.getPoder());
            stm.setInt(2, factionModel.getMembros());
            stm.setInt(3, factionModel.getMobs());
            stm.setInt(4, factionModel.getMoedas());
            stm.setInt(5, factionModel.getVoar());
            stm.setInt(6, factionModel.getPlantacao());
            stm.setInt(7, factionModel.getBlocos());
            stm.setInt(8, factionModel.getAttack());
            stm.setInt(9, factionModel.getDefesas());
            stm.setInt(10, factionModel.getAlly());
            stm.setString(11, factionModel.getTag());
            stm.executeUpdate();
        }
        catch (SQLException ex) {}
    }
    
    public void createFaction(FactionModel factionModel) {
        sqlExecutor.updateQuery("INSERT INTO fupgrade VALUES(?,?,?,?,?,?,?,?,?,?,?);", statement -> {
            statement.set(1, factionModel.getPoder());
            statement.set(2, factionModel.getMembros());
            statement.set(3, factionModel.getMobs());
            statement.set(4, factionModel.getMoedas());
            statement.set(5, factionModel.getVoar());
            statement.set(6, factionModel.getPlantacao());
            statement.set(7, factionModel.getBlocos());
            statement.set(8, factionModel.getAttack());
            statement.set(9, factionModel.getDefesas());
            statement.set(10, factionModel.getAlly());
            statement.set(11, factionModel.getTag());
        });
    }
    
    public Set<FactionModel> getAllFactions() {
        return sqlExecutor.resultManyQuery("SELECT * FROM fupgrade", statement -> {}, FactionAdapter.class);
    }
    public Set<FactionModel> getAllFactionsOrder() {
        return sqlExecutor.resultManyQuery("SELECT * FROM fupgrade ORDER BY moedas DESC", statement -> {}, FactionAdapter.class);
    }
    public void saveAll(FactionModel factionModel) {
        sqlExecutor.updateQuery("UPDATE fupgrade SET poder = ?, membros = ?, mobs = ?, moedas = ?, voar = ?, plantacao = ?, blocos = ?, attack = ?, defesas = ?, ally = ? WHERE tag = ?", statement -> {
            statement.set(1, factionModel.getPoder());
            statement.set(2, factionModel.getMembros());
            statement.set(3, factionModel.getMobs());
            statement.set(4, factionModel.getMoedas());
            statement.set(5, factionModel.getVoar());
            statement.set(6, factionModel.getPlantacao());
            statement.set(7, factionModel.getBlocos());
            statement.set(8, factionModel.getAttack());
            statement.set(9, factionModel.getDefesas());
            statement.set(10, factionModel.getAlly());
            statement.set(11, factionModel.getTag());
        });
    }
    @SuppressWarnings("static-access")
	public FactionDAO(SQLExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }
}