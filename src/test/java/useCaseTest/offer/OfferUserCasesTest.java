package useCaseTest.offer;

import mockClass.CourierTest;
import me.BlockDynasty.Economy.aplication.services.AccountService;
import me.BlockDynasty.Economy.aplication.services.CurrencyService;
import me.BlockDynasty.Economy.aplication.services.OfferService;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
import me.BlockDynasty.Economy.domain.services.IOfferService;
import mockClass.MockListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mockClass.LoggerTest;
import repositoryTest.FactoryrRepo;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfferUserCasesTest {
    private CreateOfferUseCase  createOfferUseCase;
    private AcceptOfferUseCase acceptOfferUseCase;
    private CancelOfferUseCase cancelOfferUseCase;

    private GetAccountsUseCase getAccountsUseCase;
    private GetCurrencyUseCase getCurrencyUseCase;
    private TradeCurrenciesUseCase tradeCurrenciesUseCase ;
    private IAccountService accountService;
    private ICurrencyService currencyService;
    private IRepository dataStore;
    private IOfferService offerService;

    private Account nullplague;
    private Account cris;

    private Currency dollar;
    private  Currency coin;

    @BeforeEach
    public void setup() {
        this.accountService = new AccountService(5);   //cargar en cache alguna cuenta para las pruebas
        this.dataStore = FactoryrRepo.getDb();
        this.currencyService = new CurrencyService(dataStore);  //cargar en cache alguna moneda para las pruebas

        this.coin= new Currency(UUID.randomUUID(),"coin","coins");
        this.dollar = new Currency(UUID.randomUUID(),"dollar","dollars");
        currencyService.add(dollar);
        currencyService.add(coin);

        nullplague = new Account(UUID.randomUUID(), "nullplague");
        cris = new Account(UUID.randomUUID(), "cris");

        nullplague.setBalance(dollar, BigDecimal.valueOf(1000));
        nullplague.setBalance(coin, BigDecimal.valueOf(1000));
        cris.setBalance(dollar, BigDecimal.valueOf(1000));
        cris.setBalance(coin, BigDecimal.valueOf(1000));

        accountService.addAccountToCache(nullplague);
        accountService.addAccountToCache(cris);
        dataStore.saveCurrency(dollar);
        dataStore.saveCurrency(coin);
        dataStore.saveAccount(nullplague);
        dataStore.saveAccount(cris);

        getAccountsUseCase = new GetAccountsUseCase( accountService, currencyService, dataStore);
        getCurrencyUseCase = new GetCurrencyUseCase( currencyService, dataStore);
        tradeCurrenciesUseCase = new TradeCurrenciesUseCase( getCurrencyUseCase, getAccountsUseCase, dataStore,new CourierTest(),new LoggerTest());
        offerService = new OfferService(new MockListener(),1);

        createOfferUseCase = new CreateOfferUseCase( offerService, getCurrencyUseCase, getAccountsUseCase);
        acceptOfferUseCase = new AcceptOfferUseCase( offerService, tradeCurrenciesUseCase);
        cancelOfferUseCase = new CancelOfferUseCase( offerService);
    }

    @Test
    public void testCreateOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(),dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo (cris.getUuid()), "Offer should be created successfully");
        assertEquals(false, offerService.hasOfferTo( nullplague.getUuid()), "Offer should be created successfully for the sender");
    }

    @Test
    public void testAcceptOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(), dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo(cris.getUuid()), "Offer should exist before acceptance");


        Result<Void> result =acceptOfferUseCase.execute(cris.getUuid() , nullplague.getUuid());

        assertEquals(true, result.isSuccess(), result.getErrorMessage()+ " " + result.getErrorCode());
        assertEquals(false, offerService.hasOfferTo(cris.getUuid()), "Offer should be removed after acceptance");
        assertEquals(BigDecimal.valueOf(900).setScale(2), nullplague.getBalance(dollar).getAmount(), "Sender's dollar balance should be reduced");
        assertEquals(BigDecimal.valueOf(1200).setScale(2), nullplague.getBalance(coin).getAmount(), "Sender's coin balance should be increased");
        assertEquals(BigDecimal.valueOf(1100).setScale(2), cris.getBalance(dollar).getAmount(), "Receiver's dollar balance should be increased");
        assertEquals(BigDecimal.valueOf(800).setScale(2), cris.getBalance(coin).getAmount(), "Receiver's coin balance should be reduced");
    }

    @Test
    public void testCancelOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(), dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo(cris.getUuid()), "Offer should exist before cancellation");

        Result<Void> result = cancelOfferUseCase.execute(cris.getUuid());

        assertEquals(true, result.isSuccess(), result.getErrorMessage()+ " " + result.getErrorCode());
        assertEquals(false, offerService.hasOfferTo(cris.getUuid()), "Offer should be removed after cancellation");
    }

    @Test
    public void testExpiredOffer() {
        createOfferUseCase.execute( nullplague.getUuid(), cris.getUuid(), dollar.getSingular(), BigDecimal.valueOf(100), coin.getSingular(), BigDecimal.valueOf(200));
        assertEquals(true, offerService.hasOfferTo(cris.getUuid()), "Offer should exist before expiration");


        // Simulate waiting for the offer to expire, 60 seconds is the expiration time
        try {
            Thread.sleep(1100); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertEquals(false, offerService.hasOfferTo(cris.getUuid()), "Offer should be removed after expiration");
        assertEquals(null, offerService.getOffer(cris.getUuid()), "Offer should exist before expiration");
        assertEquals(null, offerService.getOffer(nullplague.getUuid()), "Offer should exist before expiration");
    }

}
