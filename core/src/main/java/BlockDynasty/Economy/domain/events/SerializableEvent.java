package BlockDynasty.Economy.domain.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class SerializableEvent extends Event {
    public String toJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("eventType",this.getClass().getSimpleName());
        jsonObject.add("data", gson.toJsonTree(this));

        return jsonObject.toString();
    }
}