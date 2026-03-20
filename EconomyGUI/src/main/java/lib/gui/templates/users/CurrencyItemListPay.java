package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.items.payment.IPayWithItemsUseCase;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;

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
