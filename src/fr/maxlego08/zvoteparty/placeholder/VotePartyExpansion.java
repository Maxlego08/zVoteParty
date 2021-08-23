package fr.maxlego08.zvoteparty.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class VotePartyExpansion extends PlaceholderExpansion {

	private final Plugin plugin;

	/**
	 * @param plugin
	 */
	public VotePartyExpansion(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public String getAuthor() {
		return "Maxlego08";
	}

	@Override
	public String getIdentifier() {
		return "zvoteparty";
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String params) {
		return ZPlaceholderApi.getInstance().onRequest(player, params);
	}

}
