package me.venomouspenguin.antcraft.creative.roleplay.listeners;

import me.venomouspenguin.antcraft.creative.core.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RoleplayLeaderLeave implements Listener {

	private Main plugin;
	
	public RoleplayLeaderLeave(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		String playerName = p.getName();
		if(!(plugin.rp.containsValue(playerName))) return;
		String rpName = plugin.rpName.get(playerName);
		
		if(plugin.rpLeader.get(rpName).contains(playerName))
		{
			for(Player all : Bukkit.getServer().getOnlinePlayers())
			{
				if(plugin.rp.get(rpName).contains(all.getName()))
				{
					all.sendMessage(plugin.LOGO + ChatColor.YELLOW + "The leader has left the server");
					all.sendMessage(plugin.LOGO + ChatColor.YELLOW + "The roleplay has been disbanded");
					plugin.rp.get(rpName).remove(all.getName());
					plugin.rpTag.remove(all.getName());
					plugin.rpName.remove(all.getName());
					plugin.rp.get(rpName).clear();
					plugin.rpChat.get(rpName).clear();
					plugin.rpChatOff.get(rpName).clear();
					plugin.rpDeniedList.get(rpName).clear();
					plugin.rpSlots.remove(rpName);
					plugin.rpLeader.remove(rpName);
					plugin.rpType.remove(rpName);
				}
			}
		}
	}
	
}
