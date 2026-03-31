package net.blockdynasty.economy.gui.gui.templates.users;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.PayUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.items.payment.IPayWithItemsUseCase;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.ITextInput;

import java.math.BigDecimal;

public class CurrencyItemListPay extends CurrencyListToPay{
    private final IPayWithItemsUseCase payWithItemsUseCase;
    private final Player targetPlayer;

    public CurrencyItemListPay(IEntityGUI player, Player targetPlayer, SearchCurrencyUseCase searchCurrencyUseCase,
                               PayUseCase payUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, targetPlayer, searchCurrencyUseCase, payUseCase, parentGUI, textInput);
        this.payWithItemsUseCase = HardCashUseCaseFactory.getPayWithItemsUseCase();
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount){
        payWithItemsUseCase.execute(sender.asEntityHardCash(),targetPlayer.getNickname(),currency,amount.intValue());
        return "";
    }
}
