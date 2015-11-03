package me.venomouspenguin.antcraft.creative.roleplay.listeners;

import me.venomouspenguin.antcraft.creative.core.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RoleplayMemberLeave implements Listener {

	private Main plugin;
	
	public RoleplayMemberLeave(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		String pName = p.getName();
		if(!plugin.rpName.containsKey(pName))
		{
			return;
		}
		String rpName = plugin.rpName.get(pName);
		
		if(plugin.rp.get(rpName).contains(pName))
		{
			plugin.rp.get(rpName).remove(pName);
			plugin.rpChat.get(rpName).remove(pName);
			plugin.rpChatOff.get(rpName).remove(pName);
			plugin.rpTag.remove(pName);
			for(Player all : Bukkit.getServer().getOnlinePlayers())
			{
				if(plugin.rp.get(rpName).contains(all.getName()))
				{
					all.sendMessage(plugin.LOGO + ChatColor.AQUA + pName + ChatColor.YELLOW + " has left the server and has been kicked from the roleplay");
				}
			}
			plugin.rpName.remove(pName);
		}
	}
	
}
