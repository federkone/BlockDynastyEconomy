package me.BlockDynasty.Economy.aplication.useCase.offer;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertAlreadyExist;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;


import java.math.BigDecimal;
import java.util.UUID;

public class CreateOfferUseCase {
    private final OfferManager offerManager;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final GetAccountsUseCase getAccountsUseCase;

    //todo: crear oferta si, solo si no existe una pendiente entre enviador y receptor. primero tengo que obtener ambos jugadores con GetplayerUseCase y asegurarse que existan, ,GetCurrencyUseCase y asegurarse que la moneda existente exista, y luego crear la oferta guardandola en el OfferManager
    public CreateOfferUseCase(OfferManager offerManager, GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase) {
        this.offerManager = offerManager;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.getAccountsUseCase = getAccountsUseCase;

    }

    public void execute (UUID playerSender, UUID playerReciber, String currencyNameValue, BigDecimal amountCurrencyValue, String currencyNameOffer, BigDecimal amountCurrencyOffer) {
        Account accountSender = getAccountsUseCase.getAccount(playerSender);
        Account accountReciber = getAccountsUseCase.getAccount(playerReciber);
        Currency currencyValue = getCurrencyUseCase.getCurrency(currencyNameValue);
        Currency currencyOffer = getCurrencyUseCase.getCurrency(currencyNameOffer);

        //si la cuenta no existe
       /* if(accountSender == null || accountReciber == null) {  //todo:ya lo hace y lanza la excepcion el getAccountsUseCase
            throw new AccountNotFoundException("Account not found");
        }*/

        //si la moneda no existe
      /*  if(currencyOffer == null || currencyValue == null) {  //todo: ya lo hace el currencyUserCse
            throw new CurrencyNotFoundException("Currency not found");
        }*/

        if(offerManager.hasOfferTo(playerReciber)){
            throw new OffertAlreadyExist("Offer already pending for "+ playerReciber);
        }

        if (currencyValue.getUuid().equals(currencyOffer.getUuid())) {
            throw new CurrencyAmountNotValidException("Currency can not be the same");
        }

        //si el monto es menor o igual a 0
        if (amountCurrencyValue.compareTo(BigDecimal.ZERO) <= 0 || amountCurrencyOffer.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CurrencyAmountNotValidException("Amount must be greater than 0");
        }

// si la moneda soporta decimales y el monto no es entero
        if (!currencyOffer.isDecimalSupported() && amountCurrencyOffer.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new CurrencyAmountNotValidException("Amount must be an integer");
        }

// si la moneda soporta decimales y el monto no es entero
        if (!currencyValue.isDecimalSupported() && amountCurrencyValue.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new CurrencyAmountNotValidException("Amount must be an integer");
        }

        //si el enviador no tiene suficiente moneda para la oferta
        if(!accountSender.hasEnough(currencyValue,amountCurrencyValue)){
            throw new InsufficientFundsException("Insufficient funds");
        }

        //si el receptor no puede recibir la moneda
        if(!accountReciber.canReceiveCurrency()){
            throw new AccountCanNotReciveException(" account can not receive currency");
        }


        offerManager.createOffer(playerSender, playerReciber, amountCurrencyValue, amountCurrencyOffer, currencyValue, currencyOffer);
    }
}
