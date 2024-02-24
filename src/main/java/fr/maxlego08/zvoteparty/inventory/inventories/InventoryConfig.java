package fr.maxlego08.zvoteparty.inventory.inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zvoteparty.api.enums.Options;
import fr.maxlego08.zvoteparty.inventory.VInventory;
import fr.maxlego08.zvoteparty.zcore.utils.builder.ItemBuilder;
import fr.maxlego08.zvoteparty.zcore.utils.inventory.ItemButton;
import fr.maxlego08.zvoteparty.zcore.utils.inventory.PaginateInventory;

public class InventoryConfig extends PaginateInventory<Options> {

	public InventoryConfig() {
		super("§8zVoteParty - Config %p%/%mp%", 54);
	}

	@Override
	public ItemStack buildItem(Options object) {

		boolean isToggle = object.isToggle();
		ItemBuilder builder = new ItemBuilder(isToggle ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK,
				"§f" + object.getName());
		builder.addLine("§f§oStatus: §f" + yesNo(isToggle));
		builder.addLine("§f§oDescription:");
		object.getDescriptions().forEach(desc -> builder.addLine(" §7" + desc));
		if (isToggle)
			builder.glow();

		return builder.build();
	}

	private String yesNo(boolean b) {
		return b ? "§aEnable" : "§cDisabled";
	}

	@Override
	public void onClick(Options object, ItemButton button) {
			
		object.toggle(this.plugin);

		int slot = button.getSlot();
		boolean isToggle = object.isToggle();

		ItemBuilder builder = new ItemBuilder(isToggle ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK,
				"§f" + object.getName());
		builder.addLine("§f§oStatus: §f" + yesNo(isToggle));
		builder.addLine("§f§oDescription:");
		object.getDescriptions().forEach(desc -> builder.addLine(" §7" + desc));
		if (isToggle)
			builder.glow();

		this.inventory.setItem(slot, builder.build());
	}

	@Override
	public List<Options> preOpenInventory() {
		return Arrays.asList(Options.values());
	}

	@Override
	public void postOpenInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public VInventory clone() {
		return new InventoryConfig();
	}

}
