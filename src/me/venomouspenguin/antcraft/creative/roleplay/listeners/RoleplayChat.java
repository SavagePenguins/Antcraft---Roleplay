package me.venomouspenguin.antcraft.creative.roleplay.listeners;

import java.util.UUID;

import me.venomouspenguin.antcraft.creative.core.Main;
import me.venomouspenguin.antcraft.creative.core.SettingsManager;
import me.venomouspenguin.antcraft.creative.core.methods.UsefulMethods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RoleplayChat extends UsefulMethods implements Listener{

	private Main plugin;
	SettingsManager settings = SettingsManager.getInstance();
	
	public RoleplayChat(Main plugin)
	{
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		
		
		for(Player all : Bukkit.getServer().getOnlinePlayers())
		{
			if(plugin.rpChat.get(rpName).contains(p.getName()))
			{
				e.setCancelled(true);
				all.sendMessage(plugin.ROLEPLAY_LOGO + ChatColor.AQUA + p.getDisplayName() + ChatColor.RED + "> " + ChatColor.AQUA + e.getMessage());
			}
			if(plugin.rpChatOff.get(rpName).contains(p.getName()))
			{
				all.sendMessage(plugin.ROLEPLAY_LOGO + ChatColor.AQUA + p.getDisplayName() + ChatColor.RED + "> " + ChatColor.AQUA + e.getMessage());
			}
		}
	}
}