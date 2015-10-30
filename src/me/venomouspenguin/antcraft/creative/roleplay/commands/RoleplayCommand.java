package me.venomouspenguin.antcraft.creative.roleplay.commands;

import me.venomouspenguin.antcraft.creative.core.Main;
import me.venomouspenguin.antcraft.creative.core.SettingsManager;
import me.venomouspenguin.antcraft.creative.core.methods.UsefulMethods;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoleplayCommand extends UsefulMethods implements CommandExecutor {

	private Main plugin;
	SettingsManager settings = SettingsManager.getInstance();
	
	public RoleplayCommand(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		if(!(sender instanceof Player))
		{
			sender.sendMessage(plugin.LOGO + ChatColor.RED + "Sorry but you must be a player");
			return true;
		}

		//Safe casting
		Player p = (Player) sender;
		
		if(hasUserPermission(p, "roleplay"))
		{
			//Help command
			if(args.length == 0 || args[0].equalsIgnoreCase("help"))
			{
					//Send player help of commands
					p.sendMessage(ChatColor.YELLOW + "-=-=-= " + ChatColor.AQUA + "Roleplay" + ChatColor.YELLOW + " =-=-=-");
					p.sendMessage(ChatColor.YELLOW + "To create a roleplay - " + ChatColor.AQUA + "/rp create <slots> <type> <name>");
					p.sendMessage(ChatColor.YELLOW + "To join a roleplay - " + ChatColor.AQUA + "/rp join <name>");
					p.sendMessage(ChatColor.YELLOW + "To leave a roleplay - " + ChatColor.AQUA + "/rp leave <name>");
					p.sendMessage(ChatColor.YELLOW + "To list current roleplays - " + ChatColor.AQUA + "/rp list");
					p.sendMessage(ChatColor.YELLOW + "To get info about a roleplay - " + ChatColor.AQUA + "/rp info <name>");
					p.sendMessage(ChatColor.YELLOW + "To toggle into roleplay chat - " + ChatColor.AQUA + "/rp chat <on | off>");
					p.sendMessage(ChatColor.YELLOW + "To set a players tag - " + ChatColor.AQUA + "/rp tag <player> <tag>");
					return true;
			}
				
			//Create command
			if(args[0].equalsIgnoreCase("create"))
			{
				if(args.length < 0 && args.length >= 3)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp create <slots> <type> <name>");
					return true;
				}
				
				//Making sure argument slots is a number
				int slots;
				try
				{
				   slots = Integer.parseInt(args[1]);
				}catch(NumberFormatException e)
				{
				   p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Must be a integer. Argument: Slots");
				   return true;
				}
				
				if(slots < plugin.getConfig().getInt("antcraft.creative.core.min-slots") || slots > plugin.getConfig().getInt("antcraft.creative.core.max-slots"))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Must be more than " + ChatColor.AQUA + 
							plugin.getConfig().getInt("antcraft.creative.core.min-slots") + ChatColor.YELLOW + " and less than " + ChatColor.AQUA +
							plugin.getConfig().getInt("antcraft.creative.core.max-slots"));
					return true;
				}
				
				String type = args[2];
				
				String name = "";
				for(int i = 3; i < args.length; i++)
				{
					name += args[i] + " ";
				}
				
				if(plugin.rp.containsKey(name))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Roleplay already exists");
					return true;
				}
				
				p.sendMessage(plugin.LOGO + ChatColor.AQUA + name + ChatColor.YELLOW + "has been made with " + ChatColor.AQUA + slots + ChatColor.YELLOW + " slots and a type of " +
				ChatColor.AQUA + type);
				
				plugin.rp.put(name, p.getName());
				settings.getRoleplayConfig().set("antcraft.creative.roleplay." + p.getUniqueId() + ".current-roleplay", name);
				plugin.rpSlots.put(name, slots);
				plugin.rpLeader.put(name, p.getName());
				plugin.rpType.put(name, type);
				plugin.rpChatOff.put(name, p.getName());
				return true;
			}
			
			if(args[0].equalsIgnoreCase("join"))
			{
				if(args.length == 1)
					
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp join <name>");
					return true;
				}
				
				String rpName = "";
				for(int i = 1; i < args.length; i++)
				{
					rpName += args[i] + " ";
				}
				if(plugin.rp.containsValue(p.getName()))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are already in a roleplay");
					return true;
				}
				if(!(plugin.rp.containsKey(rpName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Roleplay Name Does Not Exist");
					return true;
				}
				if(plugin.rp.get(rpName).size() >= plugin.rpSlots.get(rpName))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Rp size has past capcity");
					return true;
				}
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have joined " + rpName);
				plugin.rp.put(rpName, p.getName());
				//Puts the player in the viewable chat
				plugin.rpChatOff.put(rpName, p.getName());
				settings.getRoleplayConfig().set("antcraft.creative.roleplay." + p.getUniqueId() + ".current-roleplay", rpName);
				settings.saveRoleplayConfig();
				//Send message to others
				return true;
			}
			
			if(args[0].equalsIgnoreCase("leave"))
			{
				if(args.length == 1)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp leave <name>");
					return true;
				}
				
				String rpName = "";
				
				for(int i = 1; i < args.length; i++)
				{
					rpName += args[i] + " ";
				}
				
				if(!(plugin.rp.containsValue(p.getName())))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in a roleplay");
					return true;
				}
				
				if(!(plugin.rp.containsKey(rpName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "No roleplay named: " + ChatColor.AQUA + rpName);
					return true;
				}
				
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have left roleplay: " + ChatColor.AQUA + rpName);
				plugin.rp.remove(rpName, p.getName());
				//Takes the player out of the viewable chat
				plugin.rpChatOff.remove(rpName, p.getName());
				settings.getRoleplayConfig().set("antcraft.creative.roleplay." + p.getUniqueId() + ".current-roleplay", null);
				settings.saveRoleplayConfig();
				settings.reloadRoleplayConfig();
				if(plugin.rp.get(rpName).isEmpty())
				{
					plugin.rp.remove(rpName, p.getName());
					plugin.rpLeader.remove(rpName);
					plugin.rpType.remove(rpName);
					plugin.rpSlots.remove(rpName);
					return true;
				}
				
				//Send message to others
				return true;
			}
			
			
			if(args[0].equalsIgnoreCase("info"))
			{
				String rpName = "";
				
				for(int i = 1; i < args.length; i++)
				{
					rpName += args[i] + " ";
				}
				
				if(!(plugin.rp.containsKey(rpName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "No roleplay named: " + ChatColor.AQUA + rpName);
					return true;
				}
				
				p.sendMessage(ChatColor.YELLOW + "-=-=-= " + ChatColor.AQUA + rpName + "Info" + ChatColor.YELLOW + " =-=-=-");
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Roleplay Leader: " + ChatColor.AQUA + plugin.rpLeader.get(rpName));

				if(plugin.rp.get(rpName).size() != plugin.rpSlots.get(rpName)) { p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Joinable: " + ChatColor.GREEN + "True");}
				else { p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Joinable: " + ChatColor.RED + "False"); }
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Slots: " + ChatColor.AQUA + "[" + plugin.rp.get(rpName).size() + "/" + plugin.rpSlots.get(rpName) + "]");
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Type: " + ChatColor.AQUA + plugin.rpType.get(rpName));
				StringBuilder builder = new StringBuilder();
				for(String name : plugin.rp.get(rpName))
				{
				   builder.append((builder.length() > 0 ? ChatColor.YELLOW + ", " + ChatColor.AQUA : "") + name);
				}
				String names = builder.toString();
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Players In Roleplay: ");
				p.sendMessage(plugin.LOGO + ChatColor.AQUA + names);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("list"))
			{
				p.sendMessage(ChatColor.YELLOW + "-=-=-= " + ChatColor.AQUA + "Current Roleplays" + ChatColor.YELLOW + " =-=-=-");
				
				StringBuilder builder = new StringBuilder();
				String names = "";
				if(plugin.rp.isEmpty())
				{
					names = ChatColor.YELLOW + "There are no roleplays";
				} else {
					for(String name : plugin.rp.keySet())
					{
					   builder.append((builder.length() > 0 ? ChatColor.YELLOW + "" + ChatColor.AQUA : "") + ChatColor.WHITE + "[" + ChatColor.YELLOW
							   + plugin.rpType.get(name) + ChatColor.WHITE + "] " +ChatColor.AQUA + name);
					}
					names = builder.toString();
					
				}
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "These are the current roleplays: ");
				p.sendMessage(plugin.LOGO + ChatColor.AQUA + names);
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "To find out about these roleplays do - " + ChatColor.AQUA + "/rp info <name>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("chat"))
			{
				if(args.length == 1)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp chat <on | off> <name>");
				}
				
				String rpName = "";
				for(int i = 2; i < args.length; i++)
				{
					rpName += args[i] + " ";
				}
				
				if(args[1].equalsIgnoreCase("on"))
				{
					if(!(plugin.rp.get(rpName).contains(p.getName())))
					{
						p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in that roleplay");
						return true;
					}
					if(plugin.rpChat.get(rpName).contains(p.getName()))
					{
						p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You already have roleplay chat toggled on");
						return true;
					}
					p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have joined the roleplay chat");
					plugin.rpChatOff.remove(rpName, p.getName());
					plugin.rpChat.put(rpName, p.getName());
					return true;
				}

				if(args[1].equalsIgnoreCase("off"))
				{
					if(!(plugin.rp.get(rpName).contains(p.getName())))
					{
						p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in that roleplay");
						return true;
					}
					if(plugin.rpChatOff.get(rpName).contains(p.getName()))
					{
						p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You already have roleplay chat toggled off");
						return true;
					}
					p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have left the roleplay chat");
					plugin.rpChat.remove(rpName, p.getName());
					plugin.rpChatOff.put(rpName, p.getName());
					return true;
				}
				return true;
			}
			
			p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Command");
			return true;
		}
		p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Missing Permissions");
		return false;
	}

}
