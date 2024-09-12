package fr.maxlego08.zvoteparty.placeholder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZPlaceholderApi extends ZUtils {

    private ZVotePartyPlugin plugin;
    private static final String PREFIX = "zvoteparty";
    private static final Pattern PATTERN = Pattern.compile("%([^%]+)%");

    // Singleton instance
    private static volatile ZPlaceholderApi instance;

    // Private constructor
    private ZPlaceholderApi() {
    }

    /**
     * Sets the plugin instance.
     * 
     * @param plugin the plugin instance
     */
    public void setPlugin(ZVotePartyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the singleton instance of ZPlaceholderApi.
     * 
     * @return the singleton instance
     */
    public static ZPlaceholderApi getInstance() {
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
     * Replaces placeholders in a string with their values.
     * 
     * @param player the player context
     * @param placeholder the string containing placeholders
     * @return the string with placeholders replaced
     */
    public String setPlaceholders(Player player, String placeholder) {
        if (placeholder == null || !placeholder.contains("%")) {
            return placeholder;
        }

        Matcher matcher = PATTERN.matcher(placeholder);
        while (matcher.find()) {
            String fullPlaceholder = matcher.group(0);
            String key = matcher.group(1).replace(PREFIX + "_", "");
            String replacement = onRequest(player, key);
            if (replacement != null) {
                placeholder = placeholder.replace(fullPlaceholder, replacement);
            }
        }

        return placeholder;
    }

    /**
     * Replaces placeholders in a list of strings with their values.
     * 
     * @param player the player context
     * @param lore the list of strings containing placeholders
     * @return the list of strings with placeholders replaced
     */
    public List<String> setPlaceholders(Player player, List<String> lore) {
        return lore == null ? null : lore.stream()
                .map(line -> setPlaceholders(player, line))
                .collect(Collectors.toList());
    }

    /**
     * Handles custom placeholder replacements.
     * 
     * @param player the player context
     * @param placeholder the placeholder key
     * @return the replacement value or null if not handled
     */
    private String onRequest(Player player, String placeholder) {
        if (plugin == null) {
            return null;
        }

        VotePartyManager manager = plugin.getManager();
        IStorage storage = plugin.getIStorage();

        switch (placeholder) {
            case "votes_recorded":
                return String.valueOf(storage.getVoteCount());
            case "votes_required_party":
                return String.valueOf(manager.getNeedVotes() - storage.getVoteCount());
            case "votes_required_total":
                return String.valueOf(manager.getNeedVotes());
            case "votes_progressbar":
                return getProgressBar(storage.getVoteCount(), manager.getNeedVotes(), Config.progressBar);
            case "player_votes":
                return player == null ? null : String.valueOf(manager.getPlayerVoteCount(player));
            default:
                return null;
        }
    }
}
