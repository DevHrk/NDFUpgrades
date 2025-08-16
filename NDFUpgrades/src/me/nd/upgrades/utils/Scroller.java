package me.nd.upgrades.utils;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import java.text.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

import me.nd.upgrades.Main;
import me.nd.upgrades.integration.Integration;

import java.util.*;
import org.bukkit.event.inventory.*;

public class Scroller
{
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
    
    public Scroller(final ScrollerBuilder builder, final Player p, final Integer nivel) {
        this.items = builder.items;
        this.pages = new HashMap<Integer, Inventory>();
        this.name = builder.name;
        this.inventorySize = builder.inventorySize;
        this.slots = builder.slots;
        this.backSlot = builder.backSlot;
        this.backRunnable = builder.backRunnable;
        this.previousPage = builder.previousPage;
        this.nextPage = builder.nextPage;
        this.onClickRunnable = builder.clickRunnable;
        this.createInventories(p, nivel);
    }
    
    private void createInventories(final Player p, final Integer nivel) {
    
        final List<List<ItemStack>> lists = this.getPages(this.items, this.slots.size());
        int page = 1;
        for (final List<ItemStack> list : lists) {
            final Inventory inventory = Bukkit.createInventory((InventoryHolder)new ScrollerHolder(this, page), this.inventorySize, this.name);
            int slot = 0;
            for (final ItemStack it : list) {
                inventory.setItem((int)this.slots.get(slot), it);
                ++slot;
            }
            final Integration integration = Main.get().getIntegration();
            inventory.setItem(31, new ItemBuilder(Material.EMERALD).setName("§aMelhorar nivel").setLore("", "§aInforma\u00e7\u00f5es:", "§f Clicando aqui, voc\u00ea estar\u00e1 realizando", "§f a compra de um nivel, e podera receber", "§f recompensas ou privil\u00e9gios do upgrade.", "", "§7Sua fac\u00e7\u00e3o: §f[" + integration.getTag(p) + "] " + integration.getName(p), "", "§fNivel atual: §a" + nivel, "§fPr\u00f3ximo nivel: §6" + (nivel + 1), "").toItemStack());
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
        final ItemStack item = new ItemStack(Material.ARROW);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Voltar");
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack getPageFlecha(final int page) {
        final ItemStack item = new ItemStack(Material.ARROW);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "P\ufffdgina " + page);
        item.setItemMeta(meta);
        return item;
    }
    
    public int getPages() {
        return this.pages.size();
    }
    
    public boolean hasPage(final int page) {
        return this.pages.containsKey(page);
    }
    
    public void open(final Player player) {
        this.open(player, 1);
    }
    
    public void open(final Player player, final int page) {
        player.openInventory((Inventory)this.pages.get(page));
    }
    
    private <T> List<List<T>> getPages(final Collection<T> c, Integer pageSize) {
        final List<T> list = new ArrayList<T>((Collection<? extends T>)c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size()) {
            pageSize = list.size();
        }
        final int numPages = (int)Math.ceil(list.size() / (double)pageSize);
        final List<List<T>> pages = new ArrayList<List<T>>(numPages);
        int pageNum = 0;
        while (pageNum < numPages) {
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        }
        return pages;
    }
    
    static {
        Bukkit.getPluginManager().registerEvents((Listener)new Listener() {
            @EventHandler
            public void onClick(final InventoryClickEvent e) throws ParseException {
                if (e.getInventory().getHolder() instanceof ScrollerHolder) {
                    e.setCancelled(true);
                    final ScrollerHolder holder = (ScrollerHolder)e.getInventory().getHolder();
                    if (e.getSlot() == holder.getScroller().previousPage) {
                        if (holder.getScroller().hasPage(holder.getPage() - 1)) {
                            holder.getScroller().open((Player)e.getWhoClicked(), holder.getPage() - 1);
                        }
                    }
                    else if (e.getSlot() == holder.getScroller().nextPage) {
                        if (holder.getScroller().hasPage(holder.getPage() + 1)) {
                            holder.getScroller().open((Player)e.getWhoClicked(), holder.getPage() + 1);
                        }
                    }
                    else if (e.getSlot() == holder.getScroller().backSlot) {
                        e.getWhoClicked().closeInventory();
                        holder.getScroller().backRunnable.run((Player)e.getWhoClicked());
                    }
                    else if (e.getSlot() == 31 && holder.getScroller().onClickRunnable != null) {
                        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                            return;
                        }
                        holder.getScroller().onClickRunnable.run((Player)e.getWhoClicked(), e.getCurrentItem(), e.getClick());
                    }
                }
            }
        }, (Plugin)Main.getPlugin(Main.class));
    }
    
    private class ScrollerHolder implements InventoryHolder
    {
        private Scroller scroller;
        private int page;
        
        public ScrollerHolder(final Scroller scroller, final int page) {
            this.scroller = scroller;
            this.page = page;
        }
        
        public Inventory getInventory() {
            return null;
        }
        
        public Scroller getScroller() {
            return this.scroller;
        }
        
        public int getPage() {
            return this.page;
        }
    }
    
    public static class ScrollerBuilder
    {
        private List<ItemStack> items;
        private String name;
        private int inventorySize;
        private List<Integer> slots;
        private int backSlot;
        private int previousPage;
        private int nextPage;
        private PlayerRunnable backRunnable;
        private ChooseItemRunnable clickRunnable;
        private static final List<Integer> ALLOWED_SLOTS;
        
        public ScrollerBuilder() {
            this.items = new ArrayList<ItemStack>();
            this.name = "";
            this.inventorySize = 45;
            this.slots = ScrollerBuilder.ALLOWED_SLOTS;
            this.backSlot = -1;
            this.previousPage = 18;
            this.nextPage = 26;
        }
        
        public ScrollerBuilder withItems(final List<ItemStack> items) {
            this.items = items;
            return this;
        }
        
        public ScrollerBuilder withOnClick(final ChooseItemRunnable clickRunnable) {
            this.clickRunnable = clickRunnable;
            return this;
        }
        
        public ScrollerBuilder withName(final String name) {
            this.name = name;
            return this;
        }
        
        public ScrollerBuilder withSize(final int size) {
            this.inventorySize = size;
            return this;
        }
        
        public ScrollerBuilder withArrowsSlots(final int previousPage, final int nextPage) {
            this.previousPage = previousPage;
            this.nextPage = nextPage;
            return this;
        }
        
        public ScrollerBuilder withBackItem(final int slot, final PlayerRunnable runnable) {
            this.backSlot = slot;
            this.backRunnable = runnable;
            return this;
        }
        
        public ScrollerBuilder withItemsSlots(final Integer... slots) {
            this.slots = Arrays.asList(slots);
            return this;
        }
        
        public Scroller build(final Player p, final Integer nivel) {
            return new Scroller(this, p, nivel);
        }
        
        static {
            ALLOWED_SLOTS = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
        }
    }
    
    public interface PlayerRunnable
    {
        void run(final Player p0);
    }
    
    public interface ChooseItemRunnable
    {
        void run(final Player p0, final ItemStack p1, final ClickType p2);
    }
}