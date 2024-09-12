package fr.maxlego08.zvoteparty.adapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.implementations.ZPlayerVote;
import fr.maxlego08.zvoteparty.zcore.ZPlugin;

/**
 * Custom Gson TypeAdapter for serializing and deserializing PlayerVote objects.
 */
public class PlayerAdapter extends TypeAdapter<PlayerVote> {

    private final ZPlugin plugin;
    private final Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

    private static final String UNIQUEID = "uuid";
    private static final String VOTES = "votes";

    /**
     * Constructs a PlayerAdapter with the specified plugin.
     *
     * @param plugin the ZPlugin instance used for Gson operations
     */
    public PlayerAdapter(ZPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlayerVote read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        String raw = reader.nextString();
        Map<String, Object> keys = this.plugin.getGson().fromJson(raw, mapType);

        UUID uuid = UUID.fromString((String) keys.get(UNIQUEID));
        List<Vote> votes = (List<Vote>) keys.get(VOTES);

        return new ZPlayerVote(uuid, votes);
    }

    @Override
    public void write(JsonWriter writer, PlayerVote playerVote) throws IOException {
        if (playerVote == null) {
            writer.nullValue();
            return;
        }

        Map<String, Object> serial = new HashMap<>();
        serial.put(UNIQUEID, playerVote.getUniqueId().toString());
        serial.put(VOTES, playerVote.getVotes());

        writer.value(this.plugin.getGson().toJson(serial));
    }
}
