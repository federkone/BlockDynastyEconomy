package me.BlockDynasty.Economy.domain.services;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;

import java.math.BigDecimal;

//not implemented
public interface IMessageService {

    void sendMessage(Account a, Currency c, BigDecimal b, ErrorCode e);
    void sendMessageError(TransferResult t, Currency c, BigDecimal b, ErrorCode e);
    void sendMessageSuccesful(TransferResult t, Currency c, BigDecimal b, ErrorCode e);

    void sendMessage(String a,Result r);
}
