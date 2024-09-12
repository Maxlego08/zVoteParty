package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandIndex extends VCommand {

    public CommandIndex(ZVotePartyPlugin plugin) {
        super(plugin);
        setPermission(Permission.ZVOTEPARTY_USE);
        registerSubCommands(
            new CommandVersion(plugin),
            new CommandReload(plugin),
            new CommandHelp(plugin),
            new CommandAdd(plugin),
            new CommandRemove(plugin),
            new CommandConfig(plugin),
            new CommandStartParty(plugin)
        );
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        manager.sendNeedVote(sender);
        return CommandType.SUCCESS;
    }

    /**
     * Registers multiple subcommands in a single call.
     * 
     * @param commands The subcommands to register
     * @return this
     */
    private VCommand registerSubCommands(VCommand... commands) {
        for (VCommand command : commands) {
            addSubCommand(command);
        }
        return this;
    }
}
