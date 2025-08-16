package me.nd.upgrades.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.nd.upgrades.Main;

public class SimpleScroller {
    private Map<Integer, Inventory> pages;
    private String name;
    private int inventorySize;
    private int backSlot;
    private int previousPage;
    private int nextPage;
    private PlayerRunnable backRunnable;
    @SuppressWarnings("unused")
	private ChooseItemRunnable onClickRunnable;
    private Map<Integer, Consumer<Player>> clickActions = new HashMap<>();

    public SimpleScroller(String name, int inventorySize) {
        this.name = name;
        this.inventorySize = inventorySize;
        this.pages = new HashMap<>();
        this.createInventories();
    }

    private void createInventories() {
        int page = 1;
            Inventory inventory = Bukkit.createInventory(new ScrollerHolder(this, page), this.inventorySize, this.name);
            if (page!= 1) {
                inventory.setItem(this.previousPage, this.getPageFlecha(page - 1));
            }
            inventory.setItem(this.nextPage, this.getPageFlecha(page + 1));
            if (this.backRunnable!= null) {
                inventory.setItem(this.backSlot, this.getBackFlecha());
            }
            this.pages.put(page, inventory);
            ++page;
        
        this.pages.get(this.pages.size()).setItem(this.nextPage, new ItemStack(Material.AIR));
    }

    private ItemStack getBackFlecha() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Voltar");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPageFlecha(int page) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Página " + page);
        item.setItemMeta(meta);
        return item;
    }

    public int getPages() {
        return this.pages.size();
    }

    public boolean hasPage(int page) {
        return this.pages.containsKey(page);
    }

    public void open(Player player) {
        this.open(player, 1);
    }

    public void open(Player player, int page) {
        player.openInventory(this.pages.get(page));
    }

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getInventory().getHolder() instanceof ScrollerHolder) {
                    e.setCancelled(true);
                    ScrollerHolder holder = (ScrollerHolder) e.getInventory().getHolder();
                    if (e.getSlot() == holder.getScroller().previousPage) {
                        if (holder.getScroller().hasPage(holder.getPage() - 1)) {
                            holder.getScroller().open((Player) e.getWhoClicked(), holder.getPage() - 1);
                        }
                    } else if (e.getSlot() == holder.getScroller().nextPage) {
                        if (holder.getScroller().hasPage(holder.getPage() + 1)) {
                            holder.getScroller().open((Player) e.getWhoClicked(), holder.getPage() + 1);
                        }
                    } else if (e.getSlot() == holder.getScroller().backSlot) {
                        e.getWhoClicked().closeInventory();
                        holder.getScroller().backRunnable.run((Player) e.getWhoClicked());
                    } else if (holder.getScroller().clickActions.containsKey(e.getSlot())) {
                        holder.getScroller().clickActions.get(e.getSlot()).accept((Player) e.getWhoClicked());
                    }
                }
            }
        }, (Plugin) Main.getPlugin(Main.class));
    }

    public static interface ChooseItemRunnable {
        public void run(Player var1, ItemStack var2, ClickType var3);
    }

    public static interface PlayerRunnable {
        public void run(Player var1);
    }

    public Inventory getInventory(int page) {
        if (this.hasPage(page)) {
            return this.pages.get(page);
        } else {
            return null;
        }
    }

    private int currentPage = 1; // Campo para rastrear o número da página

    // Método para obter o número da página atual
    public int getCurrentPage() {
        return this.currentPage;
    }

    // Método para definir o número da página atual (se necessário)
    public void setCurrentPage(int page) {
        this.currentPage = page;
    }

    // Método para adicionar um item ao menu na página atual
    public static void addItemToMenu(SimpleScroller scroller, ItemStack item, int slot) {
        scroller.getInventory(scroller.getCurrentPage()).setItem(slot, item);
    }

    // Método para adicionar uma Funcionalidade ha um item ao menu na página atual
    public static void addClickFunctionality(SimpleScroller scroller, ItemStack item, int slot, Consumer<Player> onClick) {
        scroller.getInventory(scroller.getCurrentPage()).setItem(slot, item);
        scroller.addClickAction(slot, onClick);
    }

    // Método para adicionar um click ha um item ao menu na página atual
    public void addClickAction(int slot, Consumer<Player> onClick) {
        clickActions.put(slot, onClick);
    }

    private class ScrollerHolder
    implements InventoryHolder {
        private SimpleScroller scroller;
        private int page;

        public ScrollerHolder(SimpleScroller scroller, int page) {
            this.scroller = scroller;
            this.page = page;
        }

        public Inventory getInventory() {
            return null;
        }

        public SimpleScroller getScroller() {
            return this.scroller;
        }

        public int getPage() {
            return this.page;
        }
    }
}