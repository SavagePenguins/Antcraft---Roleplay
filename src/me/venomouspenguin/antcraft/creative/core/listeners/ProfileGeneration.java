package me.venomouspenguin.antcraft.creative.core.listeners;

import java.util.UUID;

import me.venomouspenguin.antcraft.creative.core.SettingsManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProfileGeneration implements Listener{

	SettingsManager settings = SettingsManager.getInstance();
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		
		if(!(settings.getRoleplayConfig().isSet("antcraft.creative.roleplay." + u + ".names")))
		{
			settings.getRoleplayConfig().set("antcraft.creative.roleplay." + u + ".names", p.getName());
			settings.saveRoleplayConfig();
			settings.reloadRoleplayConfig();
		}
		
		if(!(settings.getRoleplayConfig().isSet("antcraft.creative.roleplay." + u + ".current-roleplay")))
		{
			settings.getRoleplayConfig().set("antcraft.creative.roleplay." + u + ".current-roleplay", "");
			settings.saveRoleplayConfig();
			settings.reloadRoleplayConfig();
		}
		
	}
	
}
