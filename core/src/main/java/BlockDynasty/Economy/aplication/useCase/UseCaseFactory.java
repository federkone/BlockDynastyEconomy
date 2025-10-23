package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.services.ServicesManager;
import BlockDynasty.Economy.aplication.useCase.account.*;
import BlockDynasty.Economy.aplication.useCase.account.balance.*;
import BlockDynasty.Economy.aplication.useCase.currency.*;
import BlockDynasty.Economy.aplication.useCase.offer.*;
import BlockDynasty.Economy.aplication.useCase.transaction.*;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

public class UseCaseFactory {
    private ServicesManager servicesManager;
    private IRepository repository;
    private Courier courier;
    private Log log;

    public UseCaseFactory (ServicesManager servicesManager, IRepository repository, Courier courier, Log log){
        this.servicesManager = servicesManager;
        this.repository = repository;
        this.courier = courier;
        this.log = log;
    }

    public PayUseCase pay(){
        return new PayUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public CreateAccountUseCase createAccount(){
        return new CreateAccountUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository);
    }
    public DeleteAccountUseCase deleteAccount(){
        return new DeleteAccountUseCase(repository, servicesManager.getAccountService());
    }
    public EditAccountUseCase editAccount(){
        return new EditAccountUseCase(servicesManager.getAccountService(), repository, courier);
    }
    public SearchAccountUseCase searchAccount(){
        return new SearchAccountUseCase(servicesManager.getAccountService(), repository);
    }
    public DeleteAccountUseCase deleteBalance(){
        return new DeleteAccountUseCase(repository, servicesManager.getAccountService());
    }
    public GetBalanceUseCase getBalance(){
        return new GetBalanceUseCase(searchAccount());
    }
    public CreateCurrencyUseCase createCurrency(){
        return new CreateCurrencyUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), courier, repository);
    }
    public DeleteCurrencyUseCase deleteCurrency(){
        return new DeleteCurrencyUseCase( servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier);
    }
    public EditCurrencyUseCase editCurrency(){
        return new EditCurrencyUseCase(servicesManager.getCurrencyService(), courier, repository);
    }
    public SearchCurrencyUseCase searchCurrency(){
        return new SearchCurrencyUseCase(servicesManager.getCurrencyService(),repository);
    }
    public WithdrawUseCase withdraw(){
        return new WithdrawUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public WithdrawUseCase withdraw(Log log){
        return new WithdrawUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public DepositUseCase deposit(){
        return new DepositUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository, courier, log, servicesManager.getEventManager());
    }
    public DepositUseCase deposit(Log log){
        return new DepositUseCase(servicesManager.getAccountService(), servicesManager.getCurrencyService(), repository, courier, log, servicesManager.getEventManager());
    }
    public SetBalanceUseCase setBalance(){
        return new SetBalanceUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public SetBalanceUseCase setBalance(Log log){
        return new SetBalanceUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ExchangeUseCase exchange(){
        return new ExchangeUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public ExchangeUseCase exchange(Log log){
        return new ExchangeUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public TradeCurrenciesUseCase tradeCurrencies(){
        return new TradeCurrenciesUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public TradeCurrenciesUseCase tradeCurrencies(Log log){
        return new TradeCurrenciesUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public TransferFundsUseCase transferFunds(){
        return new TransferFundsUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }
    public TransferFundsUseCase transferFunds(Log log){
        return new TransferFundsUseCase(servicesManager.getCurrencyService(), servicesManager.getAccountService(), repository, courier, log, servicesManager.getEventManager());
    }

    public CreateOfferUseCase createOffer(){
        return new CreateOfferUseCase( servicesManager.getOfferService(), courier, servicesManager.getEventManager(), searchCurrency(), searchAccount());
    }
    public CancelOfferUseCase cancelOffer(){
        return new CancelOfferUseCase(  servicesManager.getOfferService(), courier, servicesManager.getEventManager());
    }
    public AcceptOfferUseCase acceptOffer() {
        return new AcceptOfferUseCase( servicesManager.getOfferService(), courier, servicesManager.getEventManager(), tradeCurrencies());
    }
    public SearchOfferUseCase searchOffer() {
        return new SearchOfferUseCase( servicesManager.getOfferService());
    }
}
