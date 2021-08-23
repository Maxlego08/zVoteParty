package fr.maxlego08.zvoteparty.adapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.implementations.ZReward;
import fr.maxlego08.zvoteparty.zcore.ZPlugin;

public class RewardAdapter extends TypeAdapter<Reward> {

	private final ZPlugin plugin;

	private final Type seriType = new TypeToken<Map<String, Object>>() {
	}.getType();

	private final String PERCENT = "percent";
	private final String COMMANDS = "commands";

	/**
	 * @param plugin
	 */
	public RewardAdapter(ZPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Reward read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		}

		String raw = reader.nextString();
		
		Map<String, Object> keys = this.plugin.getGson().fromJson(raw, this.seriType);

		Number percent = (Number) keys.get(this.PERCENT);
		List<String> commands = (List<String>) keys.get(this.COMMANDS);

		return new ZReward(percent.doubleValue(), commands, false);
	}

	@Override
	public void write(JsonWriter writer, Reward reward) throws IOException {
		
		if (reward == null) {
			writer.nullValue();
			return;
		}
		
		Map<String, Object> serial = new HashMap<String, Object>();
		
		serial.put(this.PERCENT, reward.getPercent());
		serial.put(this.COMMANDS, reward.getCommands());
		
		writer.value(this.plugin.getGson().toJson(serial));
	}

}
