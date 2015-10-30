package me.venomouspenguin.antcraft.creative.core.methods;

import org.bukkit.entity.Player;

public class UsefulMethods {

	/**
	 * @param player The player who should have the perm
	 * @param permission The permission after antcraft.user.[perm]
	 * @return
	 */
	public boolean hasUserPermission(Player player, String permission)
	{
		String finalPermissionNode = "antcraft.user." + permission;
		return player.hasPermission(finalPermissionNode);
	}

	/**
	 * @param player The player who should have the perm
	 * @param permission The permission after antcraft.staff.[perm]
	 * @return
	 */
	public boolean hasStaffPermission(Player player, String permission)
	{
		String finalPermissionNode = "antcraft.staff." + permission;
		return player.hasPermission(finalPermissionNode);
	}
	
}