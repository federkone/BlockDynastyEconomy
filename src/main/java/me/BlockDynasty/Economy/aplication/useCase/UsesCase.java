package me.BlockDynasty.Economy.aplication.useCase;


import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.*;
import me.BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.domain.repository.IRepository;

public class UsesCase {
    private final WithdrawUseCase withdrawUseCase ;
    private final DepositUseCase depositUseCase ;
    private final SetBalanceUseCase setBalanceUseCase ;
    private final PayUseCase payUseCase ;
    private final ExchangeUseCase exchangeUseCase ;
    private final TransferFundsUseCase transferFundsUseCase ;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase ;

    private final GetAccountsUseCase getAccountsUseCase ;
    private final GetCurrencyUseCase getCurrencyUseCase ;
    private final GetBalanceUseCase getBalanceUseCase ;
    private final CreateAccountUseCase createAccountUseCase ;

    private final CreateCurrencyUseCase createCurrencyUseCase ;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase ;
    private final EditCurrencyUseCase editCurrencyUseCase ;
    private final ToggleFeaturesUseCase toggleFeaturesUseCase ;

    private final CreateOfferUseCase createOfferUseCase ;
    private final AcceptOfferUseCase acceptOfferUseCase ;
    private final CancelOfferUseCase cancelOfferUseCase ;

    public UsesCase(AccountCache accountCache, CurrencyCache currencyCache, AbstractLogger economyLogger, OfferManager offerManager, IRepository repository, UpdateForwarder updateForwarder) {
        this.getCurrencyUseCase = new GetCurrencyUseCase(currencyCache,repository);
        this.getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, repository);
        this.withdrawUseCase = new WithdrawUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, updateForwarder, economyLogger);
        this.depositUseCase = new DepositUseCase( this.getCurrencyUseCase,  this.getAccountsUseCase,repository, updateForwarder, economyLogger);
        this.createCurrencyUseCase = new CreateCurrencyUseCase(currencyCache, this.getAccountsUseCase, updateForwarder,repository);
        this.setBalanceUseCase = new SetBalanceUseCase(  this.getCurrencyUseCase,  this.getAccountsUseCase,repository, updateForwarder, economyLogger);
        this.payUseCase = new PayUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, updateForwarder, economyLogger);
        this.exchangeUseCase = new ExchangeUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, updateForwarder, economyLogger);
        this.createAccountUseCase = new CreateAccountUseCase(accountCache, currencyCache, this.getAccountsUseCase, repository);
        this.tradeCurrenciesUseCase = new TradeCurrenciesUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, updateForwarder, economyLogger);
        this.transferFundsUseCase = new TransferFundsUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, updateForwarder, economyLogger);
        this.deleteCurrencyUseCase = new DeleteCurrencyUseCase(currencyCache, this.getAccountsUseCase,repository,updateForwarder);
        this.editCurrencyUseCase = new EditCurrencyUseCase(currencyCache,updateForwarder,repository);
        this.toggleFeaturesUseCase = new ToggleFeaturesUseCase(currencyCache,repository,updateForwarder);
        this.createOfferUseCase = new CreateOfferUseCase(offerManager, this.getCurrencyUseCase, this.getAccountsUseCase);
        this.acceptOfferUseCase = new AcceptOfferUseCase(offerManager, this.tradeCurrenciesUseCase);
        this.cancelOfferUseCase = new CancelOfferUseCase(offerManager);
        this.getBalanceUseCase = new GetBalanceUseCase( this.getAccountsUseCase);
    }

    public WithdrawUseCase getWithdrawUseCase() {
        return withdrawUseCase;
    }

    public DepositUseCase getDepositUseCase() {
        return depositUseCase;
    }

    public SetBalanceUseCase getSetBalanceUseCase() {
        return setBalanceUseCase;
    }

    public PayUseCase getPayUseCase() {
        return payUseCase;
    }

    public ExchangeUseCase getExchangeUseCase() {
        return exchangeUseCase;
    }

    public TransferFundsUseCase getTransferFundsUseCase() {
        return transferFundsUseCase;
    }

    public TradeCurrenciesUseCase getTradeCurrenciesUseCase() {
        return tradeCurrenciesUseCase;
    }

    public GetAccountsUseCase getAccountsUseCase() {
        return getAccountsUseCase;
    }

    public GetCurrencyUseCase getCurrencyUseCase() {
        return getCurrencyUseCase;
    }

    public GetBalanceUseCase getGetBalanceUseCase() {
        return getBalanceUseCase;
    }

    public CreateAccountUseCase getCreateAccountUseCase() {
        return createAccountUseCase;
    }

    public CreateCurrencyUseCase getCreateCurrencyUseCase() {
        return createCurrencyUseCase;
    }

    public DeleteCurrencyUseCase deleteCurrencyUseCase() {
        return deleteCurrencyUseCase;
    }

    public EditCurrencyUseCase getEditCurrencyUseCase() {
        return editCurrencyUseCase;
    }

    public ToggleFeaturesUseCase getToggleFeaturesUseCase() {
        return toggleFeaturesUseCase;
    }

    public CreateOfferUseCase getCreateOfferUseCase() {
        return createOfferUseCase;
    }

    public AcceptOfferUseCase getAcceptOfferUseCase() {
        return acceptOfferUseCase;
    }

    public CancelOfferUseCase getCancelOfferUseCase() {
        return cancelOfferUseCase;
    }
}
