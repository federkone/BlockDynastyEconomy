package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOfferUseCase {
    private final IOfferService offerService;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final SearchAccountUseCase searchAccountUseCase;
    private final Courier courier;
    private final EventManager eventManager;

    //todo: crear oferta si, solo si no existe una pendiente entre enviador y receptor. primero tengo que obtener ambos jugadores con GetplayerUseCase y asegurarse que existan, ,GetCurrencyUseCase y asegurarse que la moneda existente exista, y luego crear la oferta guardandola en el OfferManager
    public CreateOfferUseCase(IOfferService offerService, Courier courier, EventManager eventManager,SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase) {
        this.offerService = offerService;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
        this.courier = courier;
        this.eventManager = eventManager;
    }

    public Result<Void> execute (UUID playerSender, UUID playerReciber, String currencyNameValue, BigDecimal amountCurrencyValue, String currencyNameOffer, BigDecimal amountCurrencyOffer) {
        if (playerSender.equals(playerReciber)) {
            return Result.failure( "Sender and receiver cannot be the same player", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        Result<Account> accountSenderResult = this.searchAccountUseCase.getAccount(playerSender);
        if (!accountSenderResult.isSuccess()) {
            return Result.failure(accountSenderResult.getErrorMessage(), accountSenderResult.getErrorCode());
        }

        Result<Account> accountReciberResult = this.searchAccountUseCase.getAccount(playerReciber);
        if (!accountReciberResult.isSuccess()) {
            return Result.failure(accountReciberResult.getErrorMessage(), accountReciberResult.getErrorCode());
        }

        if(accountSenderResult.getValue().isBlocked()){
            return Result.failure("Sender account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if(accountReciberResult.getValue().isBlocked()){
            return Result.failure("Receiver account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        Result<Currency> currencyValueResult = this.searchCurrencyUseCase.getCurrency(currencyNameValue);
        if (!currencyValueResult.isSuccess()) {
            return Result.failure(currencyValueResult.getErrorMessage(), currencyValueResult.getErrorCode());
        }
        Result<Currency> currencyOfferResult = this.searchCurrencyUseCase.getCurrency(currencyNameOffer);
        if (!currencyOfferResult.isSuccess()) {
            return Result.failure(currencyOfferResult.getErrorMessage(), currencyOfferResult.getErrorCode());
        }

        //if(this.offerService.hasOfferTo(playerReciber)){ //todo test, permitir ofertas multiples de diferentes jugadores
        //    return Result.failure("There is already an offer pending", ErrorCode.OFFER_ALREADY_EXISTS);
        //}

        if (currencyValueResult.getValue().getUuid().equals(currencyOfferResult.getValue().getUuid())) {
           return Result.failure("The currencies must be different", ErrorCode.CURRENCY_MUST_BE_DIFFERENT);
        }

        //si el monto es menor o igual a 0
        if (amountCurrencyValue.compareTo(BigDecimal.ZERO) <= 0 || amountCurrencyOffer.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

    // si la moneda soporta decimales y el monto no es entero
        if (!currencyOfferResult.getValue().isValidAmount(amountCurrencyOffer)) {
            return Result.failure("Amount must be an integer", ErrorCode.INVALID_AMOUNT);
        }

    // si la moneda soporta decimales y el monto no es entero
        if (!currencyValueResult.getValue().isValidAmount(amountCurrencyValue)) {
           return Result.failure("Amount must be an integer", ErrorCode.INVALID_AMOUNT);
        }

        if (!accountSenderResult.getValue().hasEnough(currencyValueResult.getValue(), amountCurrencyValue)) {
            return Result.failure("Insufficient funds", ErrorCode.INSUFFICIENT_FUNDS);
        }

        offerService.createOffer(accountSenderResult.getValue().getPlayer(), accountReciberResult.getValue().getPlayer(), amountCurrencyValue, amountCurrencyOffer, currencyValueResult.getValue(), currencyOfferResult.getValue());
        eventManager.emit(new OfferCreated(offerService.getOfferSeller(playerSender)));
        courier.sendUpdateMessage("event",new OfferCreated(offerService.getOfferSeller(playerSender)).toJson(),playerReciber.toString());
        return Result.success(null);
    }
}
