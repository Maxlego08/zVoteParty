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

/**
 * Custom Gson TypeAdapter for serializing and deserializing Reward objects.
 */
public class RewardAdapter extends TypeAdapter<Reward> {

    private final ZPlugin plugin;
    private final Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

    private static final String PERCENT = "percent";
    private static final String COMMANDS = "commands";
    private static final String MESSAGES = "messages";

    /**
     * Constructs a RewardAdapter with the specified plugin.
     *
     * @param plugin the ZPlugin instance used for Gson operations
     */
    public RewardAdapter(ZPlugin plugin) {
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
        Map<String, Object> keys = this.plugin.getGson().fromJson(raw, mapType);

        Number percent = (Number) keys.get(PERCENT);
        List<String> commands = (List<String>) keys.get(COMMANDS);
        List<String> messages = (List<String>) keys.get(MESSAGES);

        return new ZReward(percent.doubleValue(), commands, false, messages);
    }

    @Override
    public void write(JsonWriter writer, Reward reward) throws IOException {
        if (reward == null) {
            writer.nullValue();
            return;
        }

        Map<String, Object> serial = new HashMap<>();
        serial.put(PERCENT, reward.getPercent());
        serial.put(COMMANDS, reward.getCommands());
        serial.put(MESSAGES, reward.getMessages());

        writer.value(this.plugin.getGson().toJson(serial));
    }
}
