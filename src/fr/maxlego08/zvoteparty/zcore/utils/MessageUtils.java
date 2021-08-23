package fr.maxlego08.zvoteparty.zcore.utils;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.zcore.enums.DefaultFontInfo;
import fr.maxlego08.zvoteparty.zcore.utils.players.ActionBar;

public abstract class MessageUtils extends LocationUtils {

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void messageWO(CommandSender player, Message message, Object... args) {
		player.sendMessage(getMessage(message, args));
	}

	protected void messageWO(CommandSender player, String message, Object... args) {
		player.sendMessage(getMessage(message, args));
	}

	protected void message(CommandSender sender, String message, Object... args) {
		sender.sendMessage(Message.PREFIX.msg() + getMessage(message, args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void message(CommandSender sender, Message message, Object... args) {

		if (sender instanceof ConsoleCommandSender)
			sender.sendMessage(Message.PREFIX.msg() + this.papi(getMessage(message, args), null));
		else {

			Player player = (Player) sender;
			switch (message.getType()) {
			case ACTION:
				this.actionMessage(player, message, args);
				break;
			case TCHAT:
				if (message.getMessages().size() > 0) {
					message.getMessages().forEach(msg -> {
						sender.sendMessage(Message.PREFIX.msg() + this.papi(getMessage(msg, args), player));
					});
				} else
					sender.sendMessage(Message.PREFIX.msg() + this.papi(getMessage(message, args), player));
				break;
			case CENTER:
				if (message.getMessages().size() > 0) {
					message.getMessages().forEach(msg -> {
						sender.sendMessage(this.papi(this.getCenteredMessage(getMessage(msg, args)), player));
					});
				} else
					sender.sendMessage(this.papi(this.getCenteredMessage(getMessage(message, args)), player));
				break;
			case TITLE:
				// gestion du title message
				String title = message.getTitle();
				String subTitle = message.getSubTitle();
				int fadeInTime = message.getStart();
				int showTime = message.getTime();
				int fadeOutTime = message.getEnd();
				this.title(player, this.papi(title, player), this.papi(subTitle, player), fadeInTime, showTime,
						fadeOutTime);
				break;
			default:
				break;

			}

		}

	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void broadcast(Message message, Object... args) {
		for (Player player : Bukkit.getOnlinePlayers())
			message(player, message, args);
		message(Bukkit.getConsoleSender(), message, args);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void broadcastTchat(Message message, Object... args) {
		Bukkit.broadcastMessage(getMessage(message, args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void actionMessage(Player player, Message message, Object... args) {
		ActionBar.sendActionBar(player, this.papi(getMessage(message, args), player));
	}

	protected String getMessage(Message message, Object... args) {
		return getMessage(message.getMessage(), args);
	}

	protected String getMessage(String message, Object... args) {
		if (args.length % 2 != 0)
			System.err.println("Impossible d'appliquer la méthode pour les messages.");
		else
			for (int a = 0; a < args.length; a += 2) {
				String replace = args[a].toString();
				String to = args[a + 1].toString();
				message = message.replace(replace, to);
			}
		return message;
	}

	protected final Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server."
					+ Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Send title to player
	 * 
	 * @param player
	 * @param title
	 * @param subtitle
	 * @param fadeInTime
	 * @param showTime
	 * @param fadeOutTime
	 */
	protected void title(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
		System.out.println(fadeInTime + " - " + showTime + " - " + fadeOutTime);
		try {
			Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\": \"" + title + "\"}");
			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
					int.class, int.class, int.class);
			Object packet = titleConstructor.newInstance(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
					fadeInTime, showTime, fadeOutTime);

			Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\": \"" + subtitle + "\"}");
			Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
					int.class, int.class, int.class);
			Object timingPacket = timingTitleConstructor.newInstance(
					getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null),
					chatsTitle, fadeInTime, showTime, fadeOutTime);

			sendPacket(player, packet);
			sendPacket(player, timingPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param player
	 * @param packet
	 */
	protected final void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final transient static int CENTER_PX = 154;

	/**
	 * 
	 * @param message
	 * @return message
	 */
	protected String getCenteredMessage(String message) {
		if (message == null || message.equals(""))
			return "";
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		return sb.toString() + message;
	}

}
