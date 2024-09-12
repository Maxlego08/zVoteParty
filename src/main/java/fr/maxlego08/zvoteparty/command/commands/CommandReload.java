package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandReload extends VCommand {

    public CommandReload(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_RELOAD);
        addSubCommand("reload", "rl");
        setPermission(Permission.ZVOTEPARTY_RELOAD);
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        manager.reload(sender);
        return CommandType.SUCCESS;
    }
}
