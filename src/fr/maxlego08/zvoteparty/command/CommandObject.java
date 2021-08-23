package fr.maxlego08.zvoteparty.command;

import java.util.List;

import fr.maxlego08.zvoteparty.api.command.Command;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;

public class CommandObject implements Command {

	private final String command;
	private final List<String> aliases;
	private final Inventory inventory;
	private final String permission;
	private final String description;

	/**
	 * @param command
	 * @param aliases
	 * @param inventory
	 * @param permission
	 * @param description
	 */
	public CommandObject(String command, List<String> aliases, Inventory inventory, String permission,
			String description) {
		super();
		this.command = command;
		this.aliases = aliases;
		this.inventory = inventory;
		this.permission = permission;
		this.description = description;
	}

	public static Command of(Inventory inventory){
		return new CommandObject(null, null, inventory, null, null);
	}
	
	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommandObject [command=" + command + ", aliases=" + aliases + ", inventory=" + inventory
				+ ", permission=" + permission + ", description=" + description + ", getCommand()=" + getCommand()
				+ ", getAliases()=" + getAliases() + ", getInventory()=" + getInventory() + ", getPermission()="
				+ getPermission() + ", getDescription()=" + getDescription() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
