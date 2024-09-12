package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandAdd extends VCommand {

    public CommandAdd(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_ADD);
        addSubCommand("add");
        setPermission(Permission.ZVOTEPARTY_ADD);
        addRequireArg("player");
        addOptionalArg("party (true/false)");
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        String player = argAsString(0);
        boolean updateVoteParty = argAsBoolean(1, false);
        plugin.getManager().vote(sender, player, updateVoteParty);
        return CommandType.SUCCESS;
    }
}
