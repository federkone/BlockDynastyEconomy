package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.*;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.*;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;

public class UsesCaseFactory {
    private final WithdrawUseCase withdrawUseCase ;
    private final DepositUseCase depositUseCase ;
    private final SetBalanceUseCase setBalanceUseCase ;
    private final PayUseCase payUseCase ;
    private final ExchangeUseCase exchangeUseCase ;
    private final TransferFundsUseCase transferFundsUseCase ;
    private final TradeCurrenciesUseCase tradeCurrenciesUseCase ;

    private final GetAccountsUseCase getAccountsUseCase ;
    private final DeleteAccountUseCase deleteAccountUseCase ;
    private final GetCurrencyUseCase getCurrencyUseCase ;
    private final GetBalanceUseCase getBalanceUseCase ;
    private final CreateAccountUseCase createAccountUseCase ;

    private final CreateCurrencyUseCase createCurrencyUseCase ;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase ;
    private final EditCurrencyUseCase editCurrencyUseCase ;

    private final CreateOfferUseCase createOfferUseCase ;
    private final AcceptOfferUseCase acceptOfferUseCase ;
    private final CancelOfferUseCase cancelOfferUseCase ;

    public UsesCaseFactory(IAccountService accountService, ICurrencyService currencyService, Log economyLogger, IOfferService offerService, IRepository repository, Courier courier) {
        this.getCurrencyUseCase = new GetCurrencyUseCase(currencyService,repository);
        this.getAccountsUseCase = new GetAccountsUseCase(accountService, currencyService, repository);
        this.withdrawUseCase = new WithdrawUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, courier, economyLogger);
        this.depositUseCase = new DepositUseCase( this.getCurrencyUseCase,  this.getAccountsUseCase,repository, courier, economyLogger);
        this.createCurrencyUseCase = new CreateCurrencyUseCase(currencyService, this.getAccountsUseCase, courier,repository);
        this.setBalanceUseCase = new SetBalanceUseCase(  this.getCurrencyUseCase,  this.getAccountsUseCase,repository, courier, economyLogger);
        this.payUseCase = new PayUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, courier, economyLogger);
        this.exchangeUseCase = new ExchangeUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, courier, economyLogger);
        this.createAccountUseCase = new CreateAccountUseCase(accountService, currencyService, this.getAccountsUseCase, repository);
        this.tradeCurrenciesUseCase = new TradeCurrenciesUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, courier, economyLogger);
        this.transferFundsUseCase = new TransferFundsUseCase( this.getCurrencyUseCase, this.getAccountsUseCase, repository, courier, economyLogger);
        this.deleteCurrencyUseCase = new DeleteCurrencyUseCase(currencyService, this.getAccountsUseCase,repository,courier);
        this.editCurrencyUseCase = new EditCurrencyUseCase(currencyService,courier,repository);
        this.createOfferUseCase = new CreateOfferUseCase(offerService, this.getCurrencyUseCase, this.getAccountsUseCase);
        this.acceptOfferUseCase = new AcceptOfferUseCase(offerService, this.tradeCurrenciesUseCase);
        this.cancelOfferUseCase = new CancelOfferUseCase(offerService);
        this.getBalanceUseCase = new GetBalanceUseCase( this.getAccountsUseCase);
        this.deleteAccountUseCase = new DeleteAccountUseCase(repository, accountService,getAccountsUseCase);
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

    public CreateOfferUseCase getCreateOfferUseCase() {
        return createOfferUseCase;
    }

    public AcceptOfferUseCase getAcceptOfferUseCase() {
        return acceptOfferUseCase;
    }

    public CancelOfferUseCase getCancelOfferUseCase() {
        return cancelOfferUseCase;
    }

    public DeleteAccountUseCase getDeleteAccountUseCase() {
        return deleteAccountUseCase;
    }
}
