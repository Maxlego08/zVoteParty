package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandHelp extends VCommand {

    public CommandHelp(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_HELP);
        addSubCommand("help", "aide", "?");
        setPermission(Permission.ZVOTEPARTY_HELP);
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        if (parent != null) {
            parent.getSubVCommands().stream()
                .filter(command -> command.getPermission() == null || sender.hasPermission(command.getPermission()))
                .forEach(command -> messageWO(sender, Message.COMMAND_SYNTAXE_HELP, "%syntax%", command.getSyntax(), "%description%",
                        command.getDescription()));
        }
        return CommandType.SUCCESS;
    }
}
