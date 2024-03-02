package fr.maxlego08.zvoteparty.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.exceptions.InventoryOpenException;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;
import fr.maxlego08.zvoteparty.zcore.utils.builder.ItemBuilder;
import fr.maxlego08.zvoteparty.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.zvoteparty.zcore.utils.inventory.ItemButton;

public abstract class VInventory extends ZUtils implements Cloneable {

	protected int id;
	protected ZVotePartyPlugin plugin;
	protected Map<Integer, ItemButton> items = new HashMap<Integer, ItemButton>();
	protected Player player;
	protected int page;
	protected Object[] args;
	protected Inventory inventory;
	protected String guiName;
	protected boolean disableClick = true;
	protected boolean openAsync = false;

	/**
	 * Id de l'inventaire
	 * 
	 * @param id
	 * @return
	 */
	public VInventory setId(int id) {
		this.id = id;
		return this;
	}

	public int getId() {
		return id;
	}

	/**
	 * Permet de cr§er l'inventaire
	 * 
	 * @param name
	 * @return this
	 */
	protected VInventory createInventory(String name) {
		return createInventory(name, 54);
	}

	/**
	 * Permet de cr§er l'inventaire
	 * 
	 * @param name
	 * @param size
	 * @return this
	 */
	protected VInventory createInventory(String name, int size) {
		this.guiName = name;
		this.inventory = Bukkit.createInventory(null, size, name);
		return this;
	}

	/**
	 * Create default inventory with default size and name
	 */
	private void createDefaultInventory() {
		if (this.inventory == null)
			this.inventory = Bukkit.createInventory(null, 54, "§cDefault Inventory");
	}

	/**
	 * Ajout d'un item
	 * 
	 * @param slot
	 * @param item
	 * @return
	 */
	public ItemButton addItem(int slot, Material material, String name) {
		return addItem(slot, new ItemBuilder(material, name).build());
	}

	/**
	 * 
	 * @param slot
	 * @param item
	 * @return
	 */
	public ItemButton addItem(int slot, ItemBuilder item) {
		return addItem(slot, item.build());
	}

	/**
	 * 
	 * @param slot
	 * @param item
	 * @return
	 */
	public ItemButton addItem(int slot, ItemStack item) {
		// Pour §viter les erreurs, on cr§e un inventaire
		createDefaultInventory();

		ItemButton button = new ItemButton(item, slot);
		this.items.put(slot, button);

		if (this.openAsync)
			runAsync(this.plugin, () -> this.inventory.setItem(slot, item));
		else
			this.inventory.setItem(slot, item);

		return button;
	}

	/**
	 * Permet de retirer un item de la liste des items
	 * 
	 * @param slot
	 */
	public void removeItem(int slot) {
		this.items.remove(slot);
	}

	/**
	 * Permet de supprimer tous les items
	 */
	public void clearItem() {
		this.items.clear();
	}

	/**
	 * Permet de r§cup§rer tous les items
	 * 
	 * @return
	 */
	public Map<Integer, ItemButton> getItems() {
		return items;
	}

	/**
	 * Si le click dans l'inventaire est d§sactiv§ (se qui est par default)
	 * alors il va retourner vrai
	 * 
	 * @return vrai ou faux
	 */
	public boolean isDisableClick() {
		return disableClick;
	}

	/**
	 * Changer le fait de pouvoir cliquer dans l'inventaire
	 * 
	 * @param disableClick
	 */
	protected void setDisableClick(boolean disableClick) {
		this.disableClick = disableClick;
	}

	/**
	 * Permet de r§cup§rer le joueur
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Permet de r§cup§rer la page
	 * 
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @return the args
	 */
	public Object[] getObjets() {
		return args;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the guiName
	 */
	public String getGuiName() {
		return guiName;
	}

	protected InventoryResult preOpenInventory(ZVotePartyPlugin main, Player player, int page, Object... args)
			throws InventoryOpenException {

		this.page = page;
		this.args = args;
		this.player = player;
		this.plugin = main;

		return openInventory(main, player, page, args);
	}

	public abstract InventoryResult openInventory(ZVotePartyPlugin main, Player player, int page, Object... args)
			throws InventoryOpenException;

	/**
	 * 
	 * @param event
	 * @param plugin
	 * @param player
	 */
	protected void onClose(InventoryCloseEvent event, ZVotePartyPlugin plugin, Player player) {
	}

	/**
	 * 
	 * @param event
	 * @param plugin
	 * @param player
	 */
	protected void onDrag(InventoryDragEvent event, ZVotePartyPlugin plugin, Player player) {
	}

	@Override
	protected VInventory clone() {
		try {
			return (VInventory) getClass().newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
