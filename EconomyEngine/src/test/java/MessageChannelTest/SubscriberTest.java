package MessageChannelTest;

import net.blockdynasty.economy.core.aplication.events.EventManager;
import net.blockdynasty.economy.core.aplication.services.AccountService;
import net.blockdynasty.economy.core.aplication.services.CurrencyService;
import net.blockdynasty.economy.core.aplication.services.OfferService;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.currency.Currency;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.transactionsEvents.TransferEvent;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.core.domain.services.ICurrencyService;
import net.blockdynasty.economy.core.domain.services.IOfferService;
import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.courier.Message;
import net.blockdynasty.economy.core.domain.services.courier.PlayerTargetMessage;
import EngineTest.mocks.Platform;
import net.blockdynasty.economy.engine.MessageChannel.Subscriber;
import MessageChannelTest.mock.PublisherMock;
import MessageChannelTest.mock.SubscriberMock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mockClass.CourierTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.blockdynasty.economy.engine.platform.IPlatform;
import repositoryTest.FactoryRepo;

import static org.junit.jupiter.api.Assertions.*;


import java.math.BigDecimal;
import java.util.UUID;

public class SubscriberTest {
    private Subscriber subscriber;
    private IPlatform platformAdapter;
    private IOfferService offerService;
    private ICurrencyService currencyService;
    private IAccountService accountService;
    private EventManager eventManager;
    private IRepository repository;
    private Courier courier;

    @BeforeEach
    public void setup() {
        repository = FactoryRepo.getDb();
        eventManager = new EventManager();
        platformAdapter = new Platform();
        offerService = new OfferService(new CourierTest(),eventManager);
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5,repository,currencyService);

        subscriber = new SubscriberMock(platformAdapter, offerService, currencyService, accountService, eventManager);
        courier = new PublisherMock(subscriber);
    }

    @Test
    void testProcessMessage(){
        Player player = new Player(UUID.randomUUID(), "TestPlayer");
        Player player2 = new Player(UUID.randomUUID(), "TestPlayer2");
        ICurrency currency = Currency.builder()
                .setPlural("Coins")
                .setSingular("Coin")
                .setSymbol("C")
                .build();
        BigDecimal amount = new BigDecimal("100");

        TransferEvent eventToSend = new TransferEvent(player, player2, currency, amount);
        PlayerTargetMessage message = PlayerTargetMessage.builder()
                .setType(Message.Type.EVENT)
                .setTarget(player2.getUuid())
                .setTargetPlayer(player2)
                .setData(eventToSend.toJson())
                .build();

        eventManager.subscribe(TransferEvent.class , event -> {
                assertEquals(event.toJson(), eventToSend.toJson());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String prettyJson = gson.toJson(event);
                System.out.println("Event handled successfully: "+ prettyJson);
        }
        );

        courier.sendUpdateMessage(message);
    }
}
