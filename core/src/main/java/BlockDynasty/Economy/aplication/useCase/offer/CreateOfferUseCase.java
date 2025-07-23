package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.services.IOfferService;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOfferUseCase {
    private final IOfferService offerService;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final GetAccountsUseCase getAccountsUseCase;

    //todo: crear oferta si, solo si no existe una pendiente entre enviador y receptor. primero tengo que obtener ambos jugadores con GetplayerUseCase y asegurarse que existan, ,GetCurrencyUseCase y asegurarse que la moneda existente exista, y luego crear la oferta guardandola en el OfferManager
    public CreateOfferUseCase(IOfferService offerService, GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase) {
        this.offerService = offerService;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute (UUID playerSender, UUID playerReciber, String currencyNameValue, BigDecimal amountCurrencyValue, String currencyNameOffer, BigDecimal amountCurrencyOffer) {
        Result<Account> accountSenderResult = this.getAccountsUseCase.getAccount(playerSender);
        if (!accountSenderResult.isSuccess()) {
            return Result.failure(accountSenderResult.getErrorMessage(), accountSenderResult.getErrorCode());
        }

        Result<Account> accountReciberResult = this.getAccountsUseCase.getAccount(playerReciber);
        if (!accountReciberResult.isSuccess()) {
            return Result.failure(accountReciberResult.getErrorMessage(), accountReciberResult.getErrorCode());
        }

        Result<Currency> currencyValueResult = this.getCurrencyUseCase.getCurrency(currencyNameValue);
        if (!currencyValueResult.isSuccess()) {
            return Result.failure(currencyValueResult.getErrorMessage(), currencyValueResult.getErrorCode());
        }
        Result<Currency> currencyOfferResult = this.getCurrencyUseCase.getCurrency(currencyNameOffer);
        if (!currencyOfferResult.isSuccess()) {
            return Result.failure(currencyOfferResult.getErrorMessage(), currencyOfferResult.getErrorCode());
        }


        if(this.offerService.hasOfferTo(playerReciber)){
            return Result.failure("There is already an offer pending", ErrorCode.OFFER_ALREADY_EXISTS);
        }

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

        offerService.addOffer(playerSender, playerReciber, amountCurrencyValue, amountCurrencyOffer, currencyValueResult.getValue(), currencyOfferResult.getValue());
        return Result.success(null);
    }
}
