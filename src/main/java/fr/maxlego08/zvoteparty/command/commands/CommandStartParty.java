package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandStartParty extends VCommand {

    public CommandStartParty(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_STARTPARTY);
        addSubCommand("startparty", "sp");
        setPermission(Permission.ZVOTEPARTY_STARTPARTY);    
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        plugin.getManager().forceStart(sender);
        return CommandType.SUCCESS;
    }
}
