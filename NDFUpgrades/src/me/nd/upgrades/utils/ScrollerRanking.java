package me.nd.upgrades.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

public class ScrollerRanking {
    private List<ItemStack> items;
    private HashMap<Integer, Inventory> pages;
    private String name;
    private int inventorySize;
    private List<Integer> slots;
    private int backSlot;
    private int previousPage;
    private int nextPage;
    private PlayerRunnable backRunnable;
    private ChooseItemRunnable onClickRunnable;
    private HashMap<Integer, Consumer<Player>> clickActions = new HashMap<>();

	public ScrollerRanking(ScrollerBuilder builder) {
        this.items = builder.items;
        this.pages = new HashMap();
        this.name = builder.name;
        this.inventorySize = builder.inventorySize;
        this.slots = builder.slots;
        this.backSlot = builder.backSlot;
        this.backRunnable = builder.backRunnable;
        this.previousPage = builder.previousPage;
        this.nextPage = builder.nextPage;
        this.onClickRunnable = builder.clickRunnable;
        this.createInventories();
    }

    private void createInventories() {
        List<List<ItemStack>> lists = this.getPages(this.items, this.slots.size());
        int page = 1;
        for (List<ItemStack> list : lists) {
            Inventory inventory = Bukkit.createInventory((InventoryHolder)new ScrollerHolder(this, page), (int)this.inventorySize, (String)this.name);
            int slot = 0;
            for (ItemStack it : list) {
                inventory.setItem(this.slots.get(slot).intValue(), it);
                ++slot;
            }
            if (page != 1) {
                inventory.setItem(this.previousPage, this.getPageFlecha(page - 1));
            }
            inventory.setItem(this.nextPage, this.getPageFlecha(page + 1));
            if (this.backRunnable != null) {
                inventory.setItem(this.backSlot, this.getBackFlecha());
            }
            this.pages.put(page, inventory);
            ++page;
        }
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

    private <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
        ArrayList<T> list = new ArrayList<T>(c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size()) {
            pageSize = list.size();
        }
        int numPages = (int)Math.ceil((double)list.size() / (double)pageSize.intValue());
        ArrayList<List<T>> pages = new ArrayList<List<T>>(numPages);
        int pageNum = 0;
        while (pageNum < numPages) {
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        }
        return pages;
    }

    static {
        Bukkit.getPluginManager().registerEvents(new Listener(){

            @EventHandler
            public void onClick(InventoryClickEvent e) throws ParseException {
                if (e.getInventory().getHolder() instanceof ScrollerHolder) {
                    e.setCancelled(true);
                    ScrollerHolder holder = (ScrollerHolder)e.getInventory().getHolder();
                    if (e.getSlot() == holder.getScroller().previousPage) {
                        if (holder.getScroller().hasPage(holder.getPage() - 1)) {
                            holder.getScroller().open((Player)e.getWhoClicked(), holder.getPage() - 1);
                        }
                    } else if (e.getSlot() == holder.getScroller().nextPage) {
                        if (holder.getScroller().hasPage(holder.getPage() + 1)) {
                            holder.getScroller().open((Player)e.getWhoClicked(), holder.getPage() + 1);
                        }
                    } else if (e.getSlot() == holder.getScroller().backSlot) {
                        e.getWhoClicked().closeInventory();
                        holder.getScroller().backRunnable.run((Player)e.getWhoClicked());
                    } else if (holder.getScroller().slots.contains(e.getSlot()) && holder.getScroller().onClickRunnable != null) {
                        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                            return;
                        }
                        holder.getScroller().onClickRunnable.run((Player)e.getWhoClicked(), e.getCurrentItem(), e.getClick());
                    } else if (holder.getScroller().clickActions.containsKey(e.getSlot())) {
                        holder.getScroller().clickActions.get(e.getSlot()).accept((Player)e.getWhoClicked());
                    }
                }
            }
        }, (Plugin)Main.getPlugin(Main.class));
    }

    public static class ScrollerBuilder {
        private List<ItemStack> items = new ArrayList<ItemStack>();
        private String name = "";
        private int inventorySize = 45;
        private List<Integer> slots = ALLOWED_SLOTS;
        private int backSlot = -1;
        private int previousPage = 18;
        private int nextPage = 26;
        private PlayerRunnable backRunnable;
        private ChooseItemRunnable clickRunnable;
        private static final List<Integer> ALLOWED_SLOTS = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60);

        public ScrollerBuilder withItems(List<ItemStack> items) {
            this.items = items;
            return this;
        }

        public ScrollerBuilder withOnClick(ChooseItemRunnable clickRunnable) {
            this.clickRunnable = clickRunnable;
            return this;
        }

        public ScrollerBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ScrollerBuilder withSize(int size) {
            this.inventorySize = size;
            return this;
        }

        public ScrollerBuilder withArrowsSlots(int previousPage, int nextPage) {
            this.previousPage = previousPage;
            this.nextPage = nextPage;
            return this;
        }

        public ScrollerBuilder withBackItem(int slot, PlayerRunnable runnable) {
            this.backSlot = slot;
            this.backRunnable = runnable;
            return this;
        }

        public ScrollerBuilder withItemsSlots(List<Integer> slots) {
            this.slots = slots;
            return this;
        }

        public ScrollerRanking build() {
            return new ScrollerRanking(this);
        }
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
    public static void addItemToMenu(ScrollerRanking scroller, ItemStack item, int slot) {
        scroller.getInventory(scroller.getCurrentPage()).setItem(slot, item);
    }
    // Método para adicionar uma Funcionalidade ha um item ao menu na página atual
    public static void addClickFunctionality(ScrollerRanking scroller, ItemStack item, int slot, Consumer<Player> onClick) {
        scroller.getInventory(scroller.getCurrentPage()).setItem(slot, item);
        scroller.addClickAction(slot, onClick);
    }
    // Método para adicionar um click ha um item ao menu na página atual
    public void addClickAction(int slot, Consumer<Player> onClick) {
        clickActions.put(slot, onClick);
    }
    
    
    private class ScrollerHolder
    implements InventoryHolder {
        private ScrollerRanking scroller;
        private int page;

        public ScrollerHolder(ScrollerRanking scroller, int page) {
            this.scroller = scroller;
            this.page = page;
        }

        public Inventory getInventory() {
            return null;
        }

        public ScrollerRanking getScroller() {
            return this.scroller;
        }

        public int getPage() {
            return this.page;
        }
    }

    
}
