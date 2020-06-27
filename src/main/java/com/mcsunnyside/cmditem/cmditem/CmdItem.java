package com.mcsunnyside.cmditem.cmditem;

import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static org.bukkit.Material.AIR;

public final class CmdItem extends JavaPlugin implements Listener {

    private Plugin placeHolderAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
        this.placeHolderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
            if(e.getAction() != Action.RIGHT_CLICK_AIR){
                return;
            }
        }
        if(e.getItem() == null || e.getItem().getType() == Material.AIR){
            return;
        }
        if(!e.getItem().hasItemMeta()){
            return;
        }
        ItemMeta meta = e.getItem().getItemMeta();
        if(!meta.hasLore()){
            return;
        }
        for (String lore : meta.getLore()){
            lore = ChatColor.stripColor(lore);
            lore = lore.replace("{player}",e.getPlayer().getName());
            if(this.placeHolderAPI != null){
               lore =  PlaceholderAPI.setPlaceholders(e.getPlayer(), lore);
            }
            if(lore.startsWith("[[[")){
                if(!lore.endsWith("]]]")){
                    continue;
                }
                String command = StringUtils.substringBetween(lore,"[[[","]]]");
                if(command.startsWith("lp")){
                    continue;
                }
                if(command.startsWith("op")){
                    continue;
                }
                if(command.startsWith("plugman")){
                    continue;
                }
                getLogger().info("Player "+e.getPlayer()+" executing command from Console by CmdItem: "+command);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
                continue;
            }
            if(lore.startsWith("#") && lore.endsWith("#")){
                e.getItem().setAmount(e.getItem().getAmount()-1);
                if(e.getItem().getAmount() <=0 ){
                    e.getItem().setType(Material.AIR);
                }
                continue;
            }
            if(lore.startsWith("%") && lore.endsWith("%")){
                e.setCancelled(true);
            }
        }
    }
}
