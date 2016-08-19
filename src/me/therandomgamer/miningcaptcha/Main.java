package me.therandomgamer.miningcaptcha;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by robin on 19/08/16.
 */
public class Main extends JavaPlugin implements Listener{

    private HashMap<Player,Integer> attempts ;
    private List<Player> inventoryClose;
    private List<Player> closedInventory;

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getPluginManager().registerEvents(this, this);
        attempts = new HashMap<Player, Integer>();
        inventoryClose = new ArrayList<Player>();
        closedInventory = new ArrayList<Player>();

        new BukkitRunnable(){

            @Override
            public void run() {
                if(closedInventory.size()>0){
                    for(Player allPlayer: closedInventory){
                        closedInventory.remove(allPlayer);
                        Random r = new Random();
                        Material material = getRandomMaterial();
                        Inventory inv = Bukkit.createInventory(null,36, ChatColor.AQUA+"Click The "+material.toString());
                        int position = r.nextInt(36);

                        for(int i = 35
                                    ;i>=0;i--){
                            if(i == position){
                                inv.setItem(i,new ItemStack(material,1));
                            }else{
                                inv.setItem(i,new ItemStack(getRandomMaterial(),1));
                            }
                        }
                        allPlayer.openInventory(inv);
                    }
                }
            }
        }.runTaskTimer(this,0,100);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Material m = e.getBlock().getType();
        if(m == Material.COBBLESTONE || m == Material.STONE || m == Material.COAL_ORE || m == Material.LAPIS_ORE || m == Material.REDSTONE_ORE || m == Material.GOLD_ORE || m == Material.IRON_ORE || m == Material.DIAMOND_ORE || m == Material.EMERALD_ORE){
            Random r = new Random();
            int rint = r.nextInt(200)+1;
            if(rint == 1){

                Material material = getRandomMaterial();

                Inventory inv = Bukkit.createInventory(null,36, ChatColor.AQUA+"Click The "+material.toString());

                int position = r.nextInt(36);

                for(int i = 35;i>=0;i--){
                    if(i == position){
                        inv.setItem(i,new ItemStack(material,1));
                    }else{
                        inv.setItem(i,new ItemStack(getRandomMaterial(),1));
                    }
                }
                p.openInventory(inv);
            }
        }
    }


    public Material getRandomMaterial(){
        ArrayList<Material> materials = Lists.newArrayList(Material.APPLE,Material.GOLDEN_APPLE,Material.BOW,Material.NETHER_STAR,Material.TRAP_DOOR,Material.COBBLE_WALL,Material.ICE,Material.ACACIA_FENCE, Material.ANVIL,Material.ARROW,Material.BAKED_POTATO,Material.BEDROCK,Material.BOAT,Material.BONE,Material.BOOK,Material.BLAZE_ROD,Material.BLAZE_POWDER,Material.BOOKSHELF,Material.BREWING_STAND_ITEM,Material.BUCKET,Material.LAVA_BUCKET,Material.WATER_BUCKET,Material.CARROT_ITEM,Material.CACTUS,Material.CAULDRON_ITEM,Material.CHEST,Material.CLAY_BALL,Material.COAL,Material.COAL_BLOCK,Material.COBBLESTONE,Material.MOSSY_COBBLESTONE,Material.COOKED_BEEF,Material.COOKIE,Material.DIAMOND_BLOCK,Material.DIAMOND,Material.DIAMOND_AXE,Material.DIAMOND_SWORD,Material.DIAMOND_SPADE,Material.EGG,Material.EMERALD,Material.EMERALD_BLOCK,Material.ENDER_PEARL,Material.EXP_BOTTLE,Material.ENDER_STONE,Material.FEATHER,Material.GOLD_BLOCK,Material.GHAST_TEAR,Material.GLASS_BOTTLE,Material.GLOWSTONE,Material.GOLD_INGOT,Material.HOPPER,Material.ICE,Material.IRON_BLOCK,Material.JUKEBOX,Material.LAPIS_ORE,Material.LAPIS_BLOCK,Material.OBSIDIAN,Material.POTATO_ITEM,Material.PAPER,Material.SUGAR_CANE,Material.SUGAR);
        Random r = new Random();
        int rint = r.nextInt(materials.size());

        return materials.get(rint);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory().getName().contains(ChatColor.AQUA+"Click The")){
            e.setCancelled(true);
            Material good = Material.valueOf(e.getClickedInventory().getName().split("\\s")[2]);
            Player p = (Player) e.getWhoClicked();
            inventoryClose.add(p);
            p.closeInventory();
            if(e.getCurrentItem().getType() == good || e.getCursor().getType() == good){
                attempts.remove(p);
                p.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"*** Thank You For Completing The Captcha ***");
            }else{

                int attempt = 0;
                if(attempts.containsKey(p)){
                    attempt = attempts.get(p);
                    attempts.remove(p);
                }
                attempts.put(p,attempt+1);
                if(attempts.get(p) >= 3){
                    attempts.remove(p);
                    p.kickPlayer(ChatColor.RED+"You Failed To Do The Captcha");
                }else{

                    Random r = new Random();


                        Material material = getRandomMaterial();

                        Inventory inv = Bukkit.createInventory(null,36, ChatColor.AQUA+"Click The "+material.toString());

                        int position = r.nextInt(36);

                        for(int i = 35
                                    ;i>=0;i--){
                            if(i == position){
                                inv.setItem(i,new ItemStack(material,1));
                            }else{
                                inv.setItem(i,new ItemStack(getRandomMaterial(),1));
                            }
                        }
                        p.openInventory(inv);
                }
            }
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if(inventoryClose.contains((Player) e.getPlayer())){
            inventoryClose.remove((Player) e.getPlayer());
            return;
        }

        if(e.getInventory().getName().contains(ChatColor.AQUA+"Click The")){
            Player p = (Player) e.getPlayer();

            int attempt = 0;
            if(attempts.containsKey(p)){
                attempt = attempts.get(p);
                attempts.remove(p);
            }
            attempts.put(p,attempt+1);
            if(attempts.get(p) >= 3){
                p.kickPlayer(ChatColor.RED+"You Failed To Do The Captcha");
            }else{
                closedInventory.add(p);
            }

        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e){
        if(e.getSource().getName().contains(ChatColor.AQUA+"Click The") || e.getDestination().getName().contains(ChatColor.AQUA+"Click The")){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e){
        if(e.getInventory().getName().contains(ChatColor.AQUA+"Click The")){
            e.setCancelled(true);
        }
    }



}
