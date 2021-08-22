package fr.maxlego08.zvoteparty.inventory.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.button.buttons.BackButton;
import fr.maxlego08.zvoteparty.api.button.buttons.HomeButton;
import fr.maxlego08.zvoteparty.api.button.buttons.InventoryButton;
import fr.maxlego08.zvoteparty.api.button.buttons.PerformButton;
import fr.maxlego08.zvoteparty.api.button.buttons.PlaceholderButton;
import fr.maxlego08.zvoteparty.api.button.buttons.SlotButton;
import fr.maxlego08.zvoteparty.api.command.Command;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.exceptions.InventoryOpenException;
import fr.maxlego08.zvoteparty.exceptions.InventoryTypeException;
import fr.maxlego08.zvoteparty.inventory.VInventory;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.ElapsedTime;
import fr.maxlego08.zvoteparty.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.zvoteparty.zcore.utils.inventory.ItemButton;

public class InventoryDefault extends VInventory {

	private Inventory inventory;
	private List<Inventory> oldInventories;
	private Command command;

	@SuppressWarnings("unchecked")
	@Override
	public InventoryResult openInventory(ZVotePartyPlugin plugin, Player player, int page, Object... args)
			throws InventoryOpenException {

		ElapsedTime elapsedTime = new ElapsedTime("InventoryDefault");
		elapsedTime.start();

		long ms = System.currentTimeMillis();

		if (args.length < 3)
			throw new InventoryOpenException("Pas assez d'argument pour ouvrir l'inventaire");

		ElapsedTime argElapsed = new ElapsedTime("Reading the arguments");
		argElapsed.start();

		/* Les arguments de base */
		this.inventory = (Inventory) args[0];
		this.oldInventories = (List<Inventory>) args[1];
		this.command = (Command) args[2];

		argElapsed.endDisplay();

		if (!this.inventory.getType().isDefault())
			throw new InventoryTypeException("Cannot open default inventory with type " + this.inventory.getType());

		ElapsedTime getMaxPageElapsed = new ElapsedTime("Max page retrieval");
		getMaxPageElapsed.start();
		int maxPage = this.inventory.getMaxPage();
		getMaxPageElapsed.endDisplay();

		ElapsedTime buttonBack = new ElapsedTime("Creation of back buttons");
		buttonBack.start();
		int size = oldInventories.size() - 1;
		if (size >= 0) {
			Inventory oldInventory = oldInventories.get(size);

			this.inventory.getButtons(BackButton.class).forEach(button -> {
				button.setBackInventory(oldInventory == null ? this.inventory : oldInventory);
			});
		}
		buttonBack.endDisplay();

		ElapsedTime buttonHome = new ElapsedTime("Creation of the home buttons");
		buttonHome.start();
		this.inventory.getButtons(HomeButton.class).forEach(button -> button.setBackInventory(command.getInventory()));
		buttonHome.endDisplay();

		ElapsedTime sortButtons = new ElapsedTime("Button Sorting");
		sortButtons.start();
		List<PlaceholderButton> buttons = this.inventory.sortButtons(page);
		sortButtons.endDisplay();

		ElapsedTime inventoryCreate = new ElapsedTime("Creation of the inventory");
		inventoryCreate.start();
		// Gestion du nom de l'inventaire
		String inventoryName = this.inventory.getName();
		inventoryName = inventoryName.replace("%page%", String.valueOf(page));
		inventoryName = inventoryName.replace("%maxPage%", String.valueOf(maxPage));

		super.createInventory(super.papi(super.color(inventoryName), player), this.inventory.size());
		inventoryCreate.endDisplay();

		ElapsedTime buildButtons = new ElapsedTime("Creation of buttons");
		buildButtons.start();
		if (Config.enableOpenSyncInventory) {
			this.buildButtons(buttons, plugin, maxPage);
			if (Config.enableInventoryPreRender) {
				this.inventory.getRenderButtons().forEach((slot, button) -> this.buildButton(button, plugin, maxPage));
			}
		} else {
			runAsync(plugin, () -> {
				this.buildButtons(buttons, plugin, maxPage);
				if (Config.enableInventoryPreRender) {
					this.inventory.getRenderButtons()
							.forEach((slot, button) -> this.buildButton(button, plugin, maxPage));
				}
			});
		}
		buildButtons.endDisplay();

		elapsedTime.endDisplay();

		/* Système de debug */
		if (Config.enableDebug) {
			String prefix = "(" + this.inventory.getType() + ") ";
			System.out.println(
					prefix + "Ouverture de l'inventaire en: " + (System.currentTimeMillis() - ms) + "ms page " + page);
		}

		return InventoryResult.SUCCESS;
	}

	/**
	 * Builds buttons
	 * 
	 * @param buttons
	 * @param plugin
	 * @param maxPage
	 */
	private void buildButtons(List<PlaceholderButton> buttons, ZVotePartyPlugin plugin, int maxPage) {
		for (PlaceholderButton button : buttons)
			this.buildButton(button, plugin, maxPage);
	}

	/**
	 * 
	 * @param button
	 * @param plugin
	 * @param maxPage
	 */
	private void buildButton(PlaceholderButton button, ZVotePartyPlugin plugin, int maxPage) {
		if (button.hasPermission()) {

			if (!button.checkPermission(player) && button.hasElseButton()) {

				int slot = button.getTmpSlot();
				PlaceholderButton elseButton = button.getElseButton().toButton(PlaceholderButton.class);
				ItemButton zButton = addItem(slot, elseButton.getCustomItemStack(player));

				if (elseButton.isClickable())
					zButton.setClick(clickEvent(plugin, player, page, maxPage, elseButton, slot));

			} else
				displayButton(button, plugin, maxPage);

		} else
			displayButton(button, plugin, maxPage);
	}

	/**
	 * Display natural buttons
	 * 
	 * @param button
	 * @param plugin
	 * @param maxPage
	 */
	private void displayButton(PlaceholderButton button, ZVotePartyPlugin plugin, int maxPage) {

		final int slot = button.getSlot();
		if (button.getType().isSlots()) {
			button.toButton(SlotButton.class).getSlots().forEach(s -> {
				addItem(s, button.getCustomItemStack(player)).setClick(event -> {
					event.setCancelled(button.isDisableEvent());
				});
			});

		} else
			displayFinalButton(button, plugin, maxPage, player, slot);
	}

	/**
	 * Display button as final
	 * 
	 * @param button
	 * @param plugin
	 * @param maxPage
	 * @param player
	 * @param slot
	 */
	private void displayFinalButton(PlaceholderButton button, ZVotePartyPlugin plugin, int maxPage, Player player,
			int slot) {
		ItemStack itemStack = button.getCustomItemStack(player);

		ItemButton zButton = addItem(slot, itemStack);

		if (button.isClickable()) {
			Consumer<InventoryClickEvent> consumer = clickEvent(plugin, player, page, maxPage, button, slot);
			zButton.setClick(consumer);
		}
	}

	/**
	 * 
	 * @param main
	 * @param player
	 * @param page
	 * @param maxPage
	 * @param button
	 * @return
	 */
	private Consumer<InventoryClickEvent> clickEvent(ZVotePartyPlugin plugin, Player player, int page, int maxPage,
			PlaceholderButton button, int slot) {
		return event -> {

			event.setCancelled(button.isDisableEvent());

			PlaceholderButton finalButton = button;

			if (finalButton.hasPermission()) {

				if (!button.checkPermission(player)) {

					if (finalButton.hasMessage())
						message(player, finalButton.getMessage());

					if (button.hasElseButton())
						finalButton = finalButton.getElseButton().toButton(PlaceholderButton.class);
					else
						return;

				}
			}

			finalButton.playSound(player);

			switch (finalButton.getType()) {
			case INVENTORY: {
				this.oldInventories.add(inventory);
				InventoryButton inventoryButton = finalButton.toButton(InventoryButton.class);
				Inventory toInventory = inventoryButton.getInventory();

				if (!toInventory.getType().isDefault()) {

					message(player, "§cUnable to navigate to the §f" + toInventory.getName()
							+ "inventory §c, please contact an administrator to correct the problem.");

					Logger.info("Player " + player.getName() + " wanted to go to the " + toInventory.getName()
							+ " inventory but the inventory type is incorrect.", LogType.ERROR);

					return;
				}

				Object[] clonedArrays = Arrays.copyOf(this.args, this.args.length);
				clonedArrays[0] = toInventory;
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, clonedArrays);

				break;
			}
			case HOME: {
				InventoryButton inventoryButton = finalButton.toButton(InventoryButton.class);
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, 1, inventoryButton.getInventory(),
						new ArrayList<>(), command, null);
				break;
			}
			case PERFORM_COMMAND: {
				PerformButton performButton = finalButton.toButton(PerformButton.class);
				performButton.execute(player);
				break;
			}
			case BACK: {

				InventoryButton inventoryButton = finalButton.toButton(InventoryButton.class);
				Inventory currentInventory = inventoryButton.getInventory();

				this.oldInventories.remove(currentInventory);

				Object[] clonedArrays = Arrays.copyOf(this.args, this.args.length);
				clonedArrays[0] = currentInventory;
				int newPage = this.args.length == 7 ? (int) args[5] : this.getPage();
				createInventory(plugin, player, EnumInventory.INVENTORY_DEFAULT, newPage, clonedArrays);

				break;
			}
			default:
				break;
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public Inventory getIInventory() {
		return inventory;
	}

}