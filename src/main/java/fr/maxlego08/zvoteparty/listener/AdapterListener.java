package fr.maxlego08.zvoteparty.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class AdapterListener extends ZUtils implements Listener {

    private final ZVotePartyPlugin plugin;

    /**
     * Constructor to initialize the AdapterListener with the plugin instance.
     * 
     * @param plugin The instance of ZVotePartyPlugin
     */
    public AdapterListener(ZVotePartyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles player join events by notifying all registered listener adapters.
     * 
     * @param event The PlayerJoinEvent
     */
    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, player));
    }

    /**
     * Handles player quit events by notifying all registered listener adapters.
     * 
     * @param event The PlayerQuitEvent
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getListenerAdapters().forEach(adapter -> adapter.onQuit(event, player));
    }

    /**
     * Handles inventory click events by notifying all registered listener adapters.
     * 
     * @param event The InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            plugin.getListenerAdapters().forEach(adapter -> adapter.onInventoryClick(event, player));
        }
    }

    /**
     * Handles inventory drag events by notifying all registered listener adapters.
     * 
     * @param event The InventoryDragEvent
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            plugin.getListenerAdapters().forEach(adapter -> adapter.onInventoryDrag(event, player));
        }
    }

    /**
     * Handles inventory close events by notifying all registered listener adapters.
     * 
     * @param event The InventoryCloseEvent
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            plugin.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event, player));
        }
    }
}
