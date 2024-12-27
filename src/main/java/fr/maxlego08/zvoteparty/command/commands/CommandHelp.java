package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandHelp extends VCommand {

	public CommandHelp(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_HELP);
		this.addSubCommand("help", "aide", "?");
		this.setPermission(Permission.ZVOTEPARTY_HELP);
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {

		this.parent.getSubVCommands().forEach(command -> {
			if (command.getPermission() == null || this.sender.hasPermission(command.getPermission())) {
				messageWO(this.sender, Message.COMMAND_SYNTAX_HELP, "%syntax%", command.getSyntax(), "%description%",
						command.getDescription());
			}
		});

		return CommandType.SUCCESS;
	}

}
