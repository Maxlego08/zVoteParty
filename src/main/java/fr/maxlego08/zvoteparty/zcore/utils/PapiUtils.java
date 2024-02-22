package fr.maxlego08.zvoteparty.zcore.utils;

import java.util.List;
import java.util.stream.Collectors;

import fr.maxlego08.zvoteparty.placeholder.ZPlaceholderApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class PapiUtils {

	private transient boolean usePlaceHolder;

	public PapiUtils() {
		usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
	}

	/**
	 * 
	 * @param itemStack
	 * @param player
	 * @return
	 */
	protected ItemStack papi(ItemStack itemStack, Player player) {

		if (itemStack == null)
			return itemStack;

		if (!this.usePlaceHolder) {
			this.usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
		}

		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta.hasDisplayName()) {
			if (usePlaceHolder) {
				itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName()));
			} else
				itemMeta.setDisplayName(
						ZPlaceholderApi.getInstance().setPlaceholders(player, itemMeta.getDisplayName()));
		}

		if (itemMeta.hasLore()) {
			if (this.usePlaceHolder) {
				itemMeta.setLore(PlaceholderAPI.setPlaceholders(player, itemMeta.getLore()));
			} else {
				itemMeta.setLore(ZPlaceholderApi.getInstance().setPlaceholders(player, itemMeta.getLore()));
			}
		}

		itemStack.setItemMeta(itemMeta);
		return itemStack;

	}

	/**
	 * 
	 * @param placeHolder
	 * @param player
	 * @return string
	 */
	public String papi(String placeHolder, Player player) {

		if (placeHolder == null)
			return null;

		if (!usePlaceHolder)
			usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

		if (usePlaceHolder) {
			return PlaceholderAPI.setPlaceholders(player, placeHolder);
		} else
			return ZPlaceholderApi.getInstance().setPlaceholders(player, placeHolder);
	}

	/**
	 * Transforms a list into a list with placeholder API
	 * 
	 * @param placeHolder
	 * @param player
	 * @return
	 */
	public List<String> papi(List<String> placeHolder, Player player) {
		return placeHolder == null ? null : placeHolder.stream().map(e -> papi(e, player)).collect(Collectors.toList());
	}

}