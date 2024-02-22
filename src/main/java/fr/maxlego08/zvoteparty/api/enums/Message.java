package fr.maxlego08.zvoteparty.api.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zvoteparty.zcore.utils.nms.NMSUtils;

public enum Message {

	PREFIX("�8(�6zVoteParty�8) "),
	
	INVENTORY_NULL("�cImpossible to find the inventory with the id �6%id%�c."),
	INVENTORY_CLONE_NULL("�cThe inventory clone is null!"),
	INVENTORY_OPEN_ERROR("�cAn error occurred with the opening of the inventory �6%id%�c."),
	INVENTORY_BUTTON_PREVIOUS("�f� �7Previous page"),
	INVENTORY_BUTTON_NEXT("�f� �7Next page"),
	
	TIME_DAY("%02d jour(s) %02d heure(s) %02d minute(s) %02d seconde(s)"),
	TIME_HOUR("%02d heure(s) %02d minute(s) %02d seconde(s)"),
	TIME_HOUR_SIMPLE("%02d:%02d:%02d"),
	TIME_MINUTE("%02d minute(s) %02d seconde(s)"),
	TIME_SECOND("%02d seconde(s)"),
	
	COMMAND_SYNTAXE_ERROR("�cYou must execute the command like this�7: �a%syntax%"),
	COMMAND_NO_PERMISSION("�cYou do not have permission to run this command."),
	COMMAND_NO_CONSOLE("�cOnly one player can execute this command."),
	COMMAND_NO_ARG("�cImpossible to find the command with its arguments."),
	COMMAND_SYNTAXE_HELP("�a%syntax% �b� �7%description%"), 
	
	DESCRIPTION_VERSION("Show plugin version"),
	DESCRIPTION_RELOAD("Reload configurations"),
	DESCRIPTION_CONFIG("Change configuration"),
	DESCRIPTION_ADD("Add a vote to a player."),
	DESCRIPTION_REMOVE("Remove a vote to a player."),
	DESCRIPTION_STARTPARTY("Force launch a Vote Party"),
	DESCRIPTION_HELP("Show commands"), 
	DESCRIPTION_VOTE("Allows you to open the voting inventory"), 
	
	RELOAD_SUCCESS("�aYou have just reloaded the configuration."), 
	RELOAD_ERROR("�cAn error has occurred, go to the console."), 
	
	VOTE_INFORMATIONS(MessageType.CENTER,
			"�8�m-+------------------------------+-",
			"", 
			"�7Vote pour le serveur �5Serveur name here�7 !", 
			"", 
			"�8�m-+------------------------------+-"),
	
	VOTE_BROADCAST_ACTION(MessageType.ACTION, "�f%player% �7has just voted �8(�b%zvoteparty_votes_recorded%�7/�a%zvoteparty_votes_required_total%�8)"),
	VOTE_BROADCAST_TCHAT("�f%player% �7has just voted �8(�b%zvoteparty_votes_recorded%�7/�a%zvoteparty_votes_required_total%�8)"),
	VOTE_MESSAGE("�7You have just voted for the server �5Serveur name�7."),
	VOTE_LATER("�7You have just received �b%amount% �7votes."),
	VOTE_SEND("�7You just gave a vote to �f%player%�7"),
	VOTE_REMOVE_SUCCESS("�aYou have just removed a yours from the �f%player%�a."),
	VOTE_REMOVE_ERROR("�cImpossible to remove a vote from the �f%player%�c, the player has no vote."),
	VOTE_NEEDED(
			"�b%zvoteparty_votes_required_party% �fvotes �7needed for the next party !",
			"�7Serveur Minecraft Vote�8: �fhttps://serveur-minecraft-vote.fr/"
			),
	
	VOTE_PARTY_START(MessageType.CENTER,
			"�8�m-+------------------------------+-",
			"", 
			"�7Launch of the voting party!", 
			"", 
			"�8�m-+------------------------------+-"
			), 
	
	VOTE_STARTPARTY("�aYou just launched the voting party."),
	
	;

	private List<String> messages;
	private String message;
	private Map<String, Object> titles = new HashMap<>();
	private boolean use = true;
	private MessageType type = MessageType.TCHAT;

	private ItemStack itemStack;
	
	/**
	 * 
	 * @param message
	 */
	private Message(String message) {
		this.message = message;
		this.use = true;
	}

	/**
	 * 
	 * @param title
	 * @param subTitle
	 * @param a
	 * @param b
	 * @param c
	 */
	private Message(String title, String subTitle, int a, int b, int c) {
		this.use = true;
		this.titles.put("title", title);
		this.titles.put("subtitle", subTitle);
		this.titles.put("start", a);
		this.titles.put("time", b);
		this.titles.put("end", c);
		this.titles.put("isUse", true);
		this.type = MessageType.TITLE;
	}

	/**
	 * 
	 * @param message
	 */
	private Message(String... message) {
		this.messages = Arrays.asList(message);
		this.use = true;
	}
	
	/**
	 * 
	 * @param message
	 */
	private Message(MessageType type, String... message) {
		this.messages = Arrays.asList(message);
		this.use = true;
		this.type = type;
	}
	
	/**
	 * 
	 * @param message
	 */
	private Message(MessageType type, String message) {
		this.message = message;
		this.use = true;
		this.type = type;
	}

	/**
	 * 
	 * @param message
	 * @param use
	 */
	private Message(String message, boolean use) {
		this.message = message;
		this.use = use;
	}

	public String getMessage() {
		return message;
	}

	public String toMsg() {
		return message;
	}

	public String msg() {
		return message;
	}

	public boolean isUse() {
		return use;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getMessages() {
		return messages == null ? Arrays.asList(message) : messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public boolean isMessage() {
		return messages != null && messages.size() > 1;
	}

	public String getTitle() {
		return (String) titles.get("title");
	}

	public Map<String, Object> getTitles() {
		return titles;
	}

	public void setTitles(Map<String, Object> titles) {
		this.titles = titles;
	}

	public String getSubTitle() {
		return (String) titles.get("subtitle");
	}

	public boolean isTitle() {
		return titles.containsKey("title");
	}

	public int getStart() {
		return ((Number) titles.get("start")).intValue();
	}

	public int getEnd() {
		return ((Number) titles.get("end")).intValue();
	}

	public int getTime() {
		return ((Number) titles.get("time")).intValue();
	}

	public boolean isUseTitle() {
		return (boolean) titles.getOrDefault("isUse", "true");
	}

	public String replace(String a, String b) {
		return message.replace(a, b);
	}

	public MessageType getType() {
		return type.equals(MessageType.ACTION) && NMSUtils.isVeryOldVersion() ? MessageType.TCHAT : type;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
	
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

}

