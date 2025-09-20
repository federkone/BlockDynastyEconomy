package BlockDynasty.Economy.aplication.events;

import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.offersEvents.OfferAccepted;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.events.offersEvents.OfferExpired;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EventRegistry {
    private static final Map<String, Function<String, Event>> deserializers = new HashMap<>();
    private static final Gson gson = new GsonBuilder().create();

    // Registrar un tipo de evento
    public static void registerEventType(String eventType, Function<String, Event> deserializer) {
        deserializers.put(eventType, deserializer);
    }

    // Deserializar cualquier evento
    public static Event deserializeEvent(String jsonString) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            String eventType = jsonObject.get("eventType").getAsString();

            Function<String, Event> deserializer = deserializers.get(eventType);
            if (deserializer == null) {
                throw new IllegalArgumentException("Tipo de evento no registrado: " + eventType);
            }

            return deserializer.apply(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializando evento: " + e.getMessage(), e);
        }
    }

    // Inicializar el registro
    static {
        registerEventType("DepositEvent", DepositEvent::fromJson);
        registerEventType("WithdrawEvent", WithdrawEvent::fromJson);
        registerEventType("TransferEvent", TransferEvent::fromJson);
        registerEventType("SetEvent", SetEvent::fromJson);
        registerEventType("TradeEvent", TradeEvent::fromJson);
        registerEventType("OfferCreated", OfferCreated::fromJson);
        registerEventType("OfferCanceled", OfferCanceled::fromJson);
        registerEventType("OfferExpired", OfferExpired::fromJson);
        registerEventType("OfferAccepted", OfferAccepted::fromJson);
    }
}