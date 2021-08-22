package fr.maxlego08.zvoteparty.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.button.Button;
import fr.maxlego08.zvoteparty.exceptions.ButtonsNotFoundException;
import fr.maxlego08.zvoteparty.zcore.utils.loader.Loader;

public class ButtonCollections implements Loader<List<Button>> {

	private final ZVotePartyPlugin plugin;

	/**
	 * @param plugin
	 * @param economy
	 */
	public ButtonCollections(ZVotePartyPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public List<Button> load(YamlConfiguration configuration, String name, Object... args){

		List<Button> buttons = new ArrayList<Button>();

		if (!configuration.contains("items") || !configuration.isConfigurationSection("items."))
			try {
				throw new ButtonsNotFoundException(
						"Impossible to find the list of buttons for the " + name + " inventory!");
			} catch (ButtonsNotFoundException e) {
				e.printStackTrace();
			}

		ConfigurationSection section = configuration.getConfigurationSection("items.");

		Loader<Button> loader = new ButtonLoader(plugin);
		for (String tmpPath : section.getKeys(false)) {
			Button button = loader.load(configuration, "items." + tmpPath + ".", name);
			buttons.add(button);
		}

		return buttons;
	}

	@Override
	public void save(List<Button> object, YamlConfiguration configuration, String path) {

	}

}
