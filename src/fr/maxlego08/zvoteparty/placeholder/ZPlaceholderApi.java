package fr.maxlego08.zvoteparty.placeholder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZPlaceholderApi extends ZUtils {

	private ZVotePartyPlugin plugin;
	private final String prefix = "zvoteparty";
	private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");

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
	public String setPlaceholders(Player player, String placeholder) {

		if (placeholder == null || !placeholder.contains("%")) {
			return placeholder;
		}

		final String realPrefix = this.prefix + "_";

		Matcher matcher = this.pattern.matcher(placeholder);
		while (matcher.find()) {
			String stringPlaceholder = matcher.group(0);
			String regex = matcher.group(1).replace(realPrefix, "");
			String replace = this.onRequest(player, regex);
			if (replace != null) {
				placeholder = placeholder.replace(stringPlaceholder, replace);
			}
		}
		
		return placeholder;
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
	protected String removeColor(String string) {
		if (string == null) {
			return string;
		}
		for (ChatColor chatColor : ChatColor.values()) {
			string = string.replace("§" + chatColor.getChar(), "");
		}
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
		case "votes_progressbar":
			return this.getProgressBar(iStorage.getVoteCount(), manager.getNeedVotes(), Config.progressBar);
		case "player_votes":
			return player == null ? null : String.valueOf(manager.getPlayerVoteCount(player));

		}

		return null;
	}

}
