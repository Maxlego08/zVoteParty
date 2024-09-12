package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandVersion extends VCommand {

    public CommandVersion(ZVotePartyPlugin plugin) {
        super(plugin);
        setDescription(Message.DESCRIPTION_VERSION);
        addSubCommand("version", "ver", "v");
    }

    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        sendVersionInfo(plugin);
        return CommandType.SUCCESS;
    }

    private void sendVersionInfo(ZVotePartyPlugin plugin) {
        String version = plugin.getDescription().getVersion();
        message(sender, String.format("§aVersion du plugin§7: §2%s", version));
        message(sender, "§aAuteur§7: §2Maxlego08");
        message(sender, "§aDiscord§7: §2http://discord.groupez.dev/");
        message(sender, "§aDownload now§7: §2https://groupez.dev/resources/124");
        message(sender, "§aServeur Minecraft Vote§7: §fhttps://serveur-minecraft-vote.fr/");
        message(sender, "§aSponsor§7: §chttps://serveur-minecraft-vote.fr/?ref=345");
    }
}
