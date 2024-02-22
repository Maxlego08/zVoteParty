package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;

public class CommandRemove extends VCommand {

	public CommandRemove(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_REMOVE);
		this.addSubCommand("remove");
		this.setPermission(Permission.ZVOTEPARTY_REMOVE);
		this.addRequireArg("player");
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		OfflinePlayer player = this.argAsOfflinePlayer(0);
		this.plugin.getManager().removeVote(this.sender, player);
		
		return CommandType.SUCCESS;
	}

}
