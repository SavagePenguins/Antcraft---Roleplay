package me.venomouspenguin.antcraft.creative.core;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SettingsManager {

	public SettingsManager() { }
	
	static SettingsManager instance = new SettingsManager();
	
    public static SettingsManager getInstance() 
    {
        return instance;
    }
	
	Plugin p;
	FileConfiguration roleplayConfig;
	File roleplayFile;
	
	public void setup(Plugin p)
	{
		roleplayFile = new File(p.getDataFolder(), "roleplays.yml");
		
		if(!roleplayFile.exists())
		{
			try
			{
				roleplayFile.createNewFile();	
			} catch(IOException e)
			{
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not generate a roleplays.yml");
			}
		}
		
		roleplayConfig = YamlConfiguration.loadConfiguration(roleplayFile);
	}
	
	public FileConfiguration getRoleplayConfig()
	{
		return roleplayConfig;
	}
	public void saveRoleplayConfig()
	{
		try
		{
			roleplayConfig.save(roleplayFile);
		} catch(IOException e)
		{
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save roleplays.yml");
		}
	}
	public void reloadRoleplayConfig()
	{
		roleplayConfig = YamlConfiguration.loadConfiguration(roleplayFile);
	}
}
