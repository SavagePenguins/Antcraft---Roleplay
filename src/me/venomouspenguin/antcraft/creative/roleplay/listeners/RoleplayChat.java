package me.venomouspenguin.antcraft.creative.roleplay.listeners;

import me.venomouspenguin.antcraft.creative.core.Main;
import me.venomouspenguin.antcraft.creative.core.methods.UsefulMethods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RoleplayChat extends UsefulMethods implements Listener{

	private Main plugin;
	
	public RoleplayChat(Main plugin)
	{
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		String rpName = plugin.rpName.get(p.getName());
		if(plugin.rpChat.get(rpName).contains(p.getName()))
		{
			for(Player all : Bukkit.getServer().getOnlinePlayers())
			{
				String rpTag = plugin.rpTag.get(p.getName());
				
				if(plugin.rpChat.get(rpName).contains(all.getName()))
				{
					e.setCancelled(true);
					all.sendMessage(plugin.ROLEPLAY_LOGO + ChatColor.AQUA + rpTag + " " + ChatColor.AQUA + p.getDisplayName() + ChatColor.RED + " > " + ChatColor.AQUA + e.getMessage());
				}
				if(plugin.rpChatOff.get(rpName).contains(all.getName()))
				{
					all.sendMessage(plugin.ROLEPLAY_LOGO + ChatColor.AQUA + rpTag + " " +ChatColor.AQUA + p.getDisplayName() + ChatColor.RED + " > " + ChatColor.AQUA + e.getMessage());
				}
				if(plugin.rpAdminChat.containsKey(all.getName()))
				{
					all.sendMessage(plugin.LOGO + ChatColor.WHITE + "[" + ChatColor.YELLOW + "Roleplay Spy" + ChatColor.WHITE + "] " + p.getDisplayName() + ChatColor.RED + " > " + ChatColor.AQUA + e.getMessage());
				}
			}
		}
		
	}

}