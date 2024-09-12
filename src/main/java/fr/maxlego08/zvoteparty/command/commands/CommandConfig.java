package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandConfig extends VCommand {

    public CommandConfig(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_CONFIG);
        addSubCommand("config");
        setPermission(Permission.ZVOTEPARTY_CONFIG);
        setConsoleCanUse(false);
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        createInventory(plugin, player, EnumInventory.INVENTORY_CONFIG);
        return CommandType.SUCCESS;
    }
}
