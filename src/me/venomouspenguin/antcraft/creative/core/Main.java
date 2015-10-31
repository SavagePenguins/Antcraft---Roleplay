package me.venomouspenguin.antcraft.creative.core;

import java.util.HashMap;
import java.util.logging.Logger;

import me.venomouspenguin.antcraft.creative.roleplay.commands.RoleplayCommand;
import me.venomouspenguin.antcraft.creative.roleplay.listeners.RoleplayChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Main extends JavaPlugin{

	//Logo for plugin as it sends in game messages
	public String LOGO = getConfig().getString("antcraft.creative.core.logo").replace("&", "§") + ChatColor.RESET + " ";
	public String LOGO_NO_SPACE = getConfig().getString("antcraft.creative.core.logo").replace("&", "§") + ChatColor.RESET;
	public String ROLEPLAY_LOGO = getConfig().getString("antcraft.creative.core.roleplay-chat-logo").replace("&", "§") + ChatColor.RESET + " ";
	
	SettingsManager settings = SettingsManager.getInstance();
	
	//HashMap's & Multimap's for the plugin to work
	public Multimap<String, String> rp = ArrayListMultimap.create();
	public Multimap<String, String> rpChat = ArrayListMultimap.create();
	public Multimap<String, String> rpChatOff = ArrayListMultimap.create();
	
	public HashMap<String, Integer> rpSlots = new HashMap<String, Integer>();
	public HashMap<String, String> rpLeader = new HashMap<String, String>();
	public HashMap<String, String> rpType = new HashMap<String, String>();
	public HashMap<String, String> rpName = new HashMap<String, String>();
	public HashMap<String, String> rpTag = new HashMap<String, String>();
	
	
	public void onEnable()
	{
		PluginDescriptionFile pdf = getDescription();
		Logger log = getLogger();
		
		saveDefaultConfig();
		
		//Random but informative messages : >
		log.info(" Enabling Roleplay Plugin");
		log.info(" This plugin is currently on version: " + pdf.getVersion());
		log.info(" This plugin was created by: " + pdf.getAuthors());
				
		log.info(" Enabling commands needed");
		getCommand("roleplay").setExecutor(new RoleplayCommand(this));
		
		Bukkit.getServer().getPluginManager().registerEvents(new RoleplayChat(this), this);
		
	}
	
	public void onDisable()
	{
		
	}
}
