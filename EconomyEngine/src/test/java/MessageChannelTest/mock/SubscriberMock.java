package MessageChannelTest.mock;

import net.blockdynasty.economy.core.aplication.events.EventManager;
import net.blockdynasty.economy.core.domain.services.IAccountService;
import net.blockdynasty.economy.core.domain.services.ICurrencyService;
import net.blockdynasty.economy.core.domain.services.IOfferService;
import net.blockdynasty.economy.core.domain.services.courier.PlayerTargetMessage;
import net.blockdynasty.economy.engine.MessageChannel.Subscriber;
import net.blockdynasty.economy.gui.gui.GUISystem;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.engine.platform.IPlatform;

public class SubscriberMock extends Subscriber {
    private ICurrencyService currencyService;
    private IAccountService accountService;
    private IOfferService offerService;
    private IPlatform platformAdapter;
    private EventManager eventManager;

    public SubscriberMock(IPlatform platformAdapter, IOfferService offerService, ICurrencyService currencyService, IAccountService accountService, EventManager eventManager) {
        super(platformAdapter, offerService, currencyService, accountService, eventManager);
        this.currencyService = currencyService;
        this.accountService = accountService;
        this.offerService = offerService;
        this.platformAdapter = platformAdapter;
        this.eventManager = eventManager;
    }

    @Override
    public void processMessage(String messageString) {
        PlayerTargetMessage message = PlayerTargetMessage.builder()
                .fromJson(messageString)
                .build();

        switch (message.getType()) {
            case EVENT:
                offerService.processNetworkEvent(message.getData());
                if(message.getTargetPlayer() != null){
                    platformAdapter.getScheduler().runAsync(
                            ContextualTask.build(
                                    () -> {
                                        accountService.syncOnlineAccount(message.getTargetPlayer());
                                        //GUISystem.refresh(message.getTarget());
                                        eventManager.processNetworkEvent(message.getData());
                                    }
                            )
                    );
                    break;
                }
                eventManager.processNetworkEvent(message.getData());
                break;
            case ACCOUNT:
                if(message.getTargetPlayer() != null){
                    platformAdapter.getScheduler().runAsync(
                            ContextualTask.build(
                                    () -> {
                                        accountService.syncOnlineAccount(message.getTargetPlayer());
                                        GUISystem.refresh(message.getTarget());
                                    }
                            )
                    );
                }
                break;
            case CURRENCY:
                currencyService.syncCurrency(message.getTarget());
                break;
        }
    }
}
