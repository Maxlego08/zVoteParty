package fr.maxlego08.zvoteparty.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.button.Button;
import fr.maxlego08.zvoteparty.api.button.buttons.PlaceholderButton;
import fr.maxlego08.zvoteparty.api.enums.ButtonType;
import fr.maxlego08.zvoteparty.api.enums.InventoryType;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class InventoryObject extends ZUtils implements Inventory {

	private final String name;
	private final InventoryType type;
	private final int size;
	private final List<Button> buttons;
	private final String fileName;

	private final Map<Integer, PlaceholderButton> renderButtons = new HashMap<Integer, PlaceholderButton>();
	private final List<Button> needToRenderButtons = new ArrayList<Button>();

	/**
	 * @param name
	 * @param type
	 * @param size
	 * @param buttons
	 */
	public InventoryObject(String name, InventoryType type, int size, List<Button> buttons, String fileName) {
		super();
		this.name = color(name);
		this.type = type;
		this.size = size;
		this.buttons = buttons;
		this.fileName = fileName;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getName(String replace, String newChar) {
		return name.replace(replace, newChar);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Button> List<T> getButtons(Class<T> type) {
		return this.getButtons().stream().filter(e -> type.isAssignableFrom(e.getClass())).map(e -> (T) e)
				.collect(Collectors.toList());
	}

	@Override
	public List<Button> getButtons() {
		return Config.enableInventoryPreRender ? this.needToRenderButtons : this.buttons;
	}

	@Override
	public void open(Player player) {

	}

	@Override
	public List<PlaceholderButton> sortButtons(int page) {
		List<PlaceholderButton> buttons = new ArrayList<PlaceholderButton>();
		for (Button button : this.getButtons()) {

			int slot = (button.getSlot() - ((page - 1) * size));
			if ((slot >= 0 && slot < size) || button.isPermament()) {
				button.setTmpSlot(slot);
				buttons.add(button.toButton(PlaceholderButton.class));
			}

		}
		return buttons;
	}

	@Override
	public InventoryType getType() {
		return type;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public int getMaxPage() {
		return 1; // TODO
	}

	@Override
	public Map<Integer, PlaceholderButton> getRenderButtons() {
		return this.renderButtons;
	}

	@Override
	public void renderPermanentButtons() {

		this.renderButtons.clear();
		this.needToRenderButtons.clear();

		this.buttons.forEach(button -> {
			if (button.getType().isPermament() && button.isPermament()) {
				int slot = button.getSlot();
				this.renderButtons.put(slot, button.toButton(PlaceholderButton.class));
			} else {
				this.needToRenderButtons.add(button);
			}
		});
	}

	@Override
	public List<Button> getButtons(ButtonType type) {
		return this.buttons.stream().filter(e -> e.getType() == type).collect(Collectors.toList());
	}

}
