package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class CurrencyListToPay extends CurrenciesList {
    private final PayUseCase payUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;
    private final MessageService messageService;

    public CurrencyListToPay(Player player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                             SearchCurrencyUseCase searchCurrencyUseCase, PayUseCase payUseCase, MessageService messageService, IGUI parentGUI) {
        super(player, searchCurrencyUseCase, parentGUI);
        this.payUseCase = payUseCase;
        this.targetPlayer = targetPlayer;
        this.messageService = messageService;
    }

    @Override
    public String execute(Player sender,Currency currency, BigDecimal amount){
        Result<Void> result = payUseCase.execute(sender.getUniqueId(), UUID.fromString(targetPlayer.getUuid()), currency.getSingular(), amount);
        if (!result.isSuccess()) {
            messageService.sendErrorMessage(result.getErrorCode(),sender,currency.getSingular());
            return null;
        }else{
            return "Payment successful!";
        }
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(org.bukkit.Material.PAPER, "§aSelect Currency to Pay",
                Arrays.asList("§7Click to select the currency you want to pay", "§7And before that, the amount")),
                unused -> {});

    }
}
