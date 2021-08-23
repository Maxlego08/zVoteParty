package fr.maxlego08.zvoteparty.command.commands;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandAdd extends VCommand {

	public CommandAdd(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_ADD);
		this.addSubCommand("add");
		this.setPermission(Permission.ZVOTEPARTY_ADD);
		this.addRequireArg("player");
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		OfflinePlayer player = this.argAsOfflinePlayer(0);
		this.plugin.getManager().vote(this.sender, player);
		
		return CommandType.SUCCESS;
	}

}
