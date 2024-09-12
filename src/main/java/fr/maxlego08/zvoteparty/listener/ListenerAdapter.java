package fr.maxlego08.zvoteparty.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

@SuppressWarnings("deprecation")
public abstract class ListenerAdapter extends ZUtils {

    /**
     * Called when a player joins the server.
     * 
     * @param event  The PlayerJoinEvent
     * @param player The player who joined
     */
    protected void onConnect(PlayerJoinEvent event, Player player) {
    }

    /**
     * Called when a player quits the server.
     * 
     * @param event  The PlayerQuitEvent
     * @param player The player who quit
     */
    protected void onQuit(PlayerQuitEvent event, Player player) {
    }

    /**
     * Called when a player moves.
     * 
     * @param event  The PlayerMoveEvent
     * @param player The player who moved
     */
    protected void onMove(PlayerMoveEvent event, Player player) {
    }

    /**
     * Called when a player clicks an item in an inventory.
     * 
     * @param event  The InventoryClickEvent
     * @param player The player who clicked
     */
    protected void onInventoryClick(InventoryClickEvent event, Player player) {
    }

    /**
     * Called when a player closes an inventory.
     * 
     * @param event  The InventoryCloseEvent
     * @param player The player who closed the inventory
     */
    protected void onInventoryClose(InventoryCloseEvent event, Player player) {
    }

    /**
     * Called when a player drags items in an inventory.
     * 
     * @param event  The InventoryDragEvent
     * @param player The player who dragged items
     */
    protected void onInventoryDrag(InventoryDragEvent event, Player player) {
    }

    /**
     * Called when a player breaks a block.
     * 
     * @param event  The BlockBreakEvent
     * @param player The player who broke the block
     */
    protected void onBlockBreak(BlockBreakEvent event, Player player) {
    }

    /**
     * Called when a player places a block.
     * 
     * @param event  The BlockPlaceEvent
     * @param player The player who placed the block
     */
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {
    }

    /**
     * Called when an entity dies.
     * 
     * @param event  The EntityDeathEvent
     * @param entity The entity that died
     */
    protected void onEntityDeath(EntityDeathEvent event, Entity entity) {
    }

    /**
     * Called when a player interacts with something.
     * 
     * @param event  The PlayerInteractEvent
     * @param player The player who interacted
     */
    protected void onInteract(PlayerInteractEvent event, Player player) {
    }

    /**
     * Called when a player sends a chat message.
     * 
     * @param event   The AsyncPlayerChatEvent
     * @param message The chat message
     */
    protected void onPlayerTalk(AsyncPlayerChatEvent event, String message) {
    }

    /**
     * Called when a player crafts an item.
     * 
     * @param event The CraftItemEvent
     */
    protected void onCraftItem(CraftItemEvent event) {
    }

    /**
     * Called when a player executes a command.
     * 
     * @param event   The PlayerCommandPreprocessEvent
     * @param player  The player who executed the command
     * @param message The command message
     */
    protected void onCommand(PlayerCommandPreprocessEvent event, Player player, String message) {
    }

    /**
     * Called when a player's game mode changes.
     * 
     * @param event  The PlayerGameModeChangeEvent
     * @param player The player whose game mode changed
     */
    protected void onGamemodeChange(PlayerGameModeChangeEvent event, Player player) {
    }

    /**
     * Called when a player drops an item.
     * 
     * @param event  The PlayerDropItemEvent
     * @param player The player who dropped the item
     */
    public void onDrop(PlayerDropItemEvent event, Player player) {
    }

    /**
     * Called when a player picks up an item.
     * 
     * @param event  The PlayerPickupItemEvent
     * @param player The player who picked up the item
     */
    public void onPickUp(PlayerPickupItemEvent event, Player player) {
    }

    /**
     * Called when a mob spawns.
     * 
     * @param event The CreatureSpawnEvent
     */
    public void onMobSpawn(CreatureSpawnEvent event) {
    }

    /**
     * Called when an entity is damaged by another entity.
     * 
     * @param event  The EntityDamageByEntityEvent
     * @param cause  The cause of the damage
     * @param damage The amount of damage
     * @param damager The entity that inflicted the damage
     * @param entity  The entity that was damaged
     */
    public void onDamageByEntity(EntityDamageByEntityEvent event, DamageCause cause, double damage, LivingEntity damager, LivingEntity entity) {
    }

    /**
     * Called when a player is damaged by another player.
     * 
     * @param event  The EntityDamageByEntityEvent
     * @param cause  The cause of the damage
     * @param damage The amount of damage
     * @param damager The player that inflicted the damage
     * @param entity  The player that was damaged
     */
    public void onPlayerDamagaByPlayer(EntityDamageByEntityEvent event, DamageCause cause, double damage, Player damager, Player entity) {
    }

    /**
     * Called when a player is damaged by an arrow.
     * 
     * @param event  The EntityDamageByEntityEvent
     * @param cause  The cause of the damage
     * @param damage The amount of damage
     * @param damager The projectile that inflicted the damage
     * @param entity  The player that was damaged
     */
    public void onPlayerDamagaByArrow(EntityDamageByEntityEvent event, DamageCause cause, double damage, Projectile damager, Player entity) {
    }

    /**
     * Called when an item is on the ground.
     * 
     * @param event    The PlayerDropItemEvent
     * @param player   The player who dropped the item
     * @param item     The item on the ground
     * @param location The location where the item is on the ground
     */
    public void onItemisOnGround(PlayerDropItemEvent event, Player player, Item item, Location location) {
    }

    /**
     * Called when an item is moved.
     * 
     * @param event    The PlayerDropItemEvent
     * @param player   The player who dropped the item
     * @param item     The item being moved
     * @param location The new location of the item
     * @param block    The block where the item is moved
     */
    public void onItemMove(PlayerDropItemEvent event, Player player, Item item, Location location, Block block) {
    }

    /**
     * Called when a player walks.
     * 
     * @param event  The PlayerMoveEvent
     * @param player The player who walked
     * @param i      Some integer parameter (purpose unclear)
     */
    public void onPlayerWalk(PlayerMoveEvent event, Player player, int i) {
    }
}
