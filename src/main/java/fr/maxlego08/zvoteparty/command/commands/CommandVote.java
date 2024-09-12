package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandVote extends VCommand {

    public CommandVote(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_VOTE); // Adjusted to correct message description
        setPermission(Permission.ZVOTEPARTY_VOTE);
        setConsoleCanUse(false);
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        manager.openVote(player);
        return CommandType.SUCCESS;
    }
}
