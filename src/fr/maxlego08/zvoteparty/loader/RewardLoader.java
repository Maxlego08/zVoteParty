package fr.maxlego08.zvoteparty.loader;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.implementations.ZReward;
import fr.maxlego08.zvoteparty.zcore.utils.loader.Loader;

public class RewardLoader implements Loader<Reward> {

	@Override
	public Reward load(YamlConfiguration configuration, String path, Object... args) {

		double percent = configuration.getDouble(path + "percent", 10);
		List<String> commands = configuration.getStringList(path + "commands");
		boolean needToBeOnline = configuration.getBoolean(path + "needToBeOnline", false);

		return new ZReward(percent, commands, needToBeOnline);
	}

	@Override
	public void save(Reward object, YamlConfiguration configuration, String path) {
		// TODO Auto-generated method stub

	}

}
