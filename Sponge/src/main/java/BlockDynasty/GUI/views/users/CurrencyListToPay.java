package BlockDynasty.GUI.views.users;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.GUI.components.CurrenciesList;
import BlockDynasty.GUI.components.IGUI;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class CurrencyListToPay extends CurrenciesList {
    private final PayUseCase payUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;

    public CurrencyListToPay(ServerPlayer player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, PayUseCase payUseCase, IGUI parentGUI) {
        super(player, searchCurrencyUseCase, parentGUI);
        this.payUseCase = payUseCase;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String execute(ServerPlayer sender, Currency currency, BigDecimal amount){
        Result<Void> result = payUseCase.execute(sender.uniqueId(), UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (!result.isSuccess()) {
            //messageService.sendErrorMessage(result.getErrorCode(),sender,currency.getSingular());
            return null;
        }else{
            return "Successful!";
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(ItemTypes.PAPER.get(), "§aSelect Currency to Pay",
                        "§7Click to select the currency you want to pay", "§7And before that, the amount"),
                null);

    }
}
