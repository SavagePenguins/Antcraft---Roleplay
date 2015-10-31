package me.venomouspenguin.antcraft.creative.roleplay.commands;

import me.venomouspenguin.antcraft.creative.core.Main;
import me.venomouspenguin.antcraft.creative.core.SettingsManager;
import me.venomouspenguin.antcraft.creative.core.methods.UsefulMethods;

import org.bukkit.Bukkit;
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
					p.sendMessage(ChatColor.YELLOW + "To leave a roleplay - " + ChatColor.AQUA + "/rp leave");
					p.sendMessage(ChatColor.YELLOW + "To kick a player form the roleplay - " + ChatColor.AQUA + "/rp kick <player>");
					p.sendMessage(ChatColor.YELLOW + "To list current roleplays - " + ChatColor.AQUA + "/rp list");
					p.sendMessage(ChatColor.YELLOW + "To get info about a roleplay - " + ChatColor.AQUA + "/rp info <name>");
					p.sendMessage(ChatColor.YELLOW + "To toggle into roleplay chat - " + ChatColor.AQUA + "/rp chat <on | off>");
					p.sendMessage(ChatColor.YELLOW + "To set a players tag - " + ChatColor.AQUA + "/rp tag <player> <tag>");
					return true;
			}
				
			//Create command
			if(args[0].equalsIgnoreCase("create"))
			{
				if(args.length <= 3)
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
				plugin.rpTag.put(p.getName(), "");
				plugin.rpSlots.put(name, slots);
				plugin.rpLeader.put(name, p.getName());
				plugin.rpType.put(name, type);
				plugin.rpChatOff.put(name, p.getName());
				plugin.rpName.put(p.getName(), name);
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
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have joined: " + ChatColor.AQUA + rpName);
				plugin.rp.put(rpName, p.getName());
				//Puts the player in the viewable chat
				plugin.rpChatOff.put(rpName, p.getName());
				plugin.rpName.put(p.getName(), rpName);
				plugin.rpTag.put(p.getName(), "");
				//Send message to others
				return true;
			}
			
			if(args[0].equalsIgnoreCase("leave"))
			{
				String senderName = p.getName();
				if(!(plugin.rp.containsValue(senderName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in a roleplay");
					return true;
				}
				
				String rpName = plugin.rpName.get(senderName);
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have left the roleplay: " + ChatColor.AQUA + rpName);
				
				plugin.rp.get(rpName).remove(senderName);
				plugin.rpChatOff.get(rpName).remove(senderName);
				plugin.rpTag.remove(senderName);
				plugin.rpName.remove(senderName);
				
				for(Player all : Bukkit.getServer().getOnlinePlayers())
				{
					if(plugin.rp.get(rpName).contains(all.getName()))
					{
						all.sendMessage(plugin.LOGO + ChatColor.AQUA + senderName + ChatColor.YELLOW + " has left the roleplay");
					}
				}
				
				if(plugin.rp.get(rpName).isEmpty())
				{
					plugin.rp.remove(rpName, null);
					plugin.rpChat.remove(rpName, null);
					plugin.rpChatOff.remove(rpName, null);
					plugin.rpSlots.remove(rpName);
					plugin.rpLeader.remove(rpName);
					plugin.rpType.remove(rpName);
					return true;
				}
				
				return true;
			}
			
			
			if(args[0].equalsIgnoreCase("info"))
			{
				if(args.length == 1)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You must specify a roleplay name");
					return true;
				}
				
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
					String rpTag = plugin.rpTag.get(name);
					builder.append((builder.length() > 0 ? ChatColor.YELLOW + ", " + ChatColor.AQUA : "") + rpTag + " " + ChatColor.AQUA + name);
				}
				String names = builder.toString();
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "Players In Roleplay: ");
				p.sendMessage(plugin.LOGO_NO_SPACE + ChatColor.AQUA + names);
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
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp chat <on | off>");
					return true;
				}
				if(plugin.rpName.get(p.getName()) == null || !(plugin.rpName.containsKey(p.getName())))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in a roleplay");
					return true;
				}
				
				String rpName = plugin.rpName.get(p.getName());
				
				if(args[1].equalsIgnoreCase("on"))
				{
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
			
			if(args[0].equalsIgnoreCase("settag"))
			{
				if(args.length == 1)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp settag <player> <tag>");
					return true;
				}
			
				String senderName = p.getName();
				
				if(!(plugin.rpName.containsKey(senderName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in a roleplay");
					return true;
				}
				
				String rpName = plugin.rpName.get(senderName);
				
				if(!(plugin.rpLeader.get(rpName).contains(senderName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not the leader of the roleplay");
					return true;
				}
				
				@SuppressWarnings("deprecation")
				Player player =  Bukkit.getServer().getPlayer(args[1]);
				
				if(player == null)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Player not found"); 
					return true;
				}
				
				String playerName = player.getName();
				
				if(!(plugin.rp.get(rpName)).contains(playerName))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "The player is not part of this roleplay");
					return true;
				}
				
				if(args.length == 2)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp settag <player> <tag>");
					return true;
				}
				String tag = ChatColor.WHITE + " [" + args[2].replace("&", "§") + ChatColor.WHITE + "]";
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have changed player, " + ChatColor.AQUA + playerName + ChatColor.YELLOW + ", tag to:" + ChatColor.AQUA + tag);
				plugin.rpTag.put(playerName, tag);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("kick"))
			{
				if(args.length == 1)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Incorrect Arguments - /rp kick <player>");
					return true;
				}
				
				String senderName = p.getName();
				
				if(!(plugin.rpName.containsKey(senderName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not in a roleplay");
					return true;
				}
				
				String rpName = plugin.rpName.get(p.getName());
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getServer().getPlayer(args[1]);
				
				if(player == null)
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Player not found");
					return true;
				}
				
				String playerName = player.getName();
				
				if(!(plugin.rpLeader.get(rpName).contains(senderName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You are not the leader of the roleplay");
					return true;
				}
				
				if(!(plugin.rp.get(rpName).contains(playerName)))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "The player is not in your roleplay");
					return true;
				}
				if(playerName.equals(senderName))
				{
					p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "You can not kick yourself");
					return true;
				}
				
				p.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have kicked: " + ChatColor.AQUA + playerName);
				player.sendMessage(plugin.LOGO + ChatColor.YELLOW + "You have been kicked out of: " + rpName);
			
				plugin.rp.get(rpName).remove(playerName);
				plugin.rpTag.remove(playerName);
				plugin.rpChatOff.remove(rpName, playerName);
				plugin.rpName.remove(playerName);
				return true;
			}
			
			p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "To get help - /rp help");
			return true;
		}
		p.sendMessage(plugin.LOGO + ChatColor.RED + "Error: " + ChatColor.YELLOW + "Missing Permissions");
		return false;
	}

}
