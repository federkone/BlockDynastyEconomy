package BlockDynasty.Economy.domain.events;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public interface SerializableEvent {
    // Método para obtener el tipo de evento (usado para deserialización)
    String getEventType();

    // Método para serializar a JSON
    default String toJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("eventType", getEventType());
        jsonObject.add("data", gson.toJsonTree(this));

        return jsonObject.toString();
    }

    // Método estático para deserializar desde JSON
    static <T extends Event> T fromJson(String jsonString, Class<T> eventClass) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            // Verificar que el tipo de evento coincide
            String eventType = jsonObject.get("eventType").getAsString();
            if (!eventType.equals(eventClass.getSimpleName())) {
                throw new IllegalArgumentException("Tipo de evento no coincide: " + eventType);
            }

            // Deserializar los datos
            JsonObject data = jsonObject.get("data").getAsJsonObject();
            Gson gson = new GsonBuilder().create();

            return gson.fromJson(data, eventClass);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializando evento: " + e.getMessage(), e);
        }
    }
}