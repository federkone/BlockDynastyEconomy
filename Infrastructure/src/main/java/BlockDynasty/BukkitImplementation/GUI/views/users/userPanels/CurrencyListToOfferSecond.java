package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class CurrencyListToOfferSecond extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase createOfferUseCase;
    private final CurrencyListToOfferFirst parentGUI;

    public CurrencyListToOfferSecond(Player player, BlockDynasty.Economy.domain.entities.account.Player target, SearchCurrencyUseCase searchCurrencyUseCase,
                                     CreateOfferUseCase createOfferUseCase, CurrencyListToOfferFirst parentGUI) {
        super(player, searchCurrencyUseCase, parentGUI,parentGUI.getCurrency());
        this.createOfferUseCase = createOfferUseCase;
        this.target = target;
        this.parentGUI = parentGUI;
    }

    @Override
    protected String  execute(Player sender, Currency currency, java.math.BigDecimal amount){
        Result<Void> result= createOfferUseCase.execute(
                sender.getUniqueId(),
                UUID.fromString(target.getUuid()),
                parentGUI.getCurrency().getPlural(),
                parentGUI.getAmount(),
                currency.getSingular(),
                amount
        );

        if (!result.isSuccess()){
            sender.sendMessage("§cError: " + result.getErrorMessage()+ "."+result.getErrorCode());
        }else{
            GUIFactory.seeMyOffersPanel(sender,null).open();
        }

        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Material.PAPER, "§aSelect Currency to Receive",
                        Arrays.asList("§7Click to select the currency you want to receive", "And before that, the amount" )
                        ),
                unused -> {});

    }
}
