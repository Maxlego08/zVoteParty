package fr.maxlego08.zvoteparty.placeholder;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.storage.IStorage;

public class ZPlaceholderApi {

	private ZVotePartyPlugin plugin;
	private final String prefix = "zvoteparty";

	/**
	 * Set plugin instance
	 * 
	 * @param plugin
	 */
	public void setPlugin(ZVotePartyPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * static Singleton instance.
	 */
	private static volatile ZPlaceholderApi instance;

	/**
	 * Private constructor for singleton.
	 */
	private ZPlaceholderApi() {
	}

	/**
	 * Return a singleton instance of ZPlaceholderApi.
	 */
	public static ZPlaceholderApi getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (ZPlaceholderApi.class) {
				if (instance == null) {
					instance = new ZPlaceholderApi();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * @param player
	 * @param displayName
	 * @return
	 */
	public String setPlaceholders(Player player, String displayName) {

		if (displayName == null)
			return null;

		final String realPrefix = "%" + prefix + "_";

		String str = removeColor(displayName);

		for (String string : str.split(" "))
			if (string.startsWith(realPrefix) && string.endsWith("%")) {

				String request = string.replace(realPrefix, "");
				request = request.substring(0, request.length() - 1);

				String replace = this.onRequest(player, request);
				if (replace != null)
					displayName = displayName.replace(string, replace);
			}

		return displayName;
	}

	/**
	 * 
	 * @param player
	 * @param lore
	 * @return
	 */
	public List<String> setPlaceholders(Player player, List<String> lore) {
		return lore == null ? null
				: lore.stream().map(e -> e = setPlaceholders(player, e)).collect(Collectors.toList());
	}

	/**
	 * Remove color from message
	 * 
	 * @param string
	 * @return string without color
	 */
	private String removeColor(String string) {
		if (string == null)
			return string;
		for (ChatColor chatColor : ChatColor.values())
			string = string.replace("§" + chatColor.getChar(), "");
		return string;
	}

	/**
	 * Custom placeholder
	 * 
	 * @param player
	 * @param string
	 * @return
	 */
	public String onRequest(Player player, String string) {

		VotePartyManager manager = this.plugin.getManager();
		IStorage iStorage = this.plugin.getIStorage();

		switch (string) {

		case "votes_recorded":
			return String.valueOf(iStorage.getVoteCount());
		case "votes_required_party":
			return String.valueOf(manager.getNeedVotes() - iStorage.getVoteCount());
		case "votes_required_total":
			return String.valueOf(manager.getNeedVotes());
		case "player_votes":
			return String.valueOf(manager.getPlayerVoteCount(player));

		}

		return null;
	}

}
