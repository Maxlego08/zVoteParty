package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandVersion extends VCommand {

	public CommandVersion(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_VERSION);
		this.addSubCommand("version", "ver", "v");
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		message(sender, "§aVersion du plugin§7: §2" + plugin.getDescription().getVersion());
		message(sender, "§aAuteur§7: §2Maxlego08");
		message(sender, "§aDiscord§7: §2http://discord.groupez.dev/");
		message(sender, "§aDownload now§7: §2https://groupez.dev/resources/124");
		message(sender, "§aServeur Minecraft Vote§7: §fhttps://serveur-minecraft-vote.fr/");
		
		return CommandType.SUCCESS;
	}

}
