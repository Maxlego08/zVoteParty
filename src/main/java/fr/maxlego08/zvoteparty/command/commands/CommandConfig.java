package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandConfig extends VCommand {

	public CommandConfig(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_CONFIG);
		this.addSubCommand("config");
		this.setPermission(Permission.ZVOTEPARTY_CONFIG);
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		this.createInventory(plugin, this.player, EnumInventory.INVENTORY_CONFIG);
		
		return CommandType.SUCCESS;
	}

}
