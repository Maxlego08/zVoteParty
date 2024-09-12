package fr.maxlego08.zvoteparty.command.commands;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandRemove extends VCommand {

    public CommandRemove(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_REMOVE);
        addSubCommand("remove");
        setPermission(Permission.ZVOTEPARTY_REMOVE);
        addRequireArg("player");
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        OfflinePlayer player = argAsOfflinePlayer(0);
        plugin.getManager().removeVote(sender, player);
        return CommandType.SUCCESS;
    }
}
