package me.nd.upgrades.api;

import org.bukkit.configuration.file.*;

import me.nd.upgrades.Main;
import java.util.*;
import java.text.*;

public class FormatterAPI {
    
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.##");
    private static final String[] NUMBER_SUFFIX = new String[]{"K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV", "QNV", "SEV", "SPV", "OVG", "NVG", "TG", "TGV"};
    private static final DecimalFormat FORMATTER = new DecimalFormat("###,###,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
   
    
    public static String formatter(double value) {
    	FileConfiguration m = Main.get().getConfig();
        if (m.getString("Config.format").equalsIgnoreCase("NUMEROS"))
          return formatPunctuating(value); 
        return formatShortening(value);
      }
      
      public static String formatShortening(double value) {
        if (value < 1000.0D)
          return (int)value + ""; 
        return format(value, 0);
      }
      
      public static String formatPunctuating(double value) {
        return FORMATTER.format(value);
      }
      
      private static String format(double n, int iteration) {
        double f = n / 100.0D / 10.0D;
        return (f < 1000.0D || iteration >= NUMBER_SUFFIX.length - 1) ? (DECIMAL_FORMAT.format(f) + NUMBER_SUFFIX[iteration]) : format(f, iteration + 1);
      }

    }