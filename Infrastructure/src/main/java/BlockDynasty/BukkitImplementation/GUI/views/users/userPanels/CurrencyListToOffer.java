package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.CurrenciesList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

//muestro lista de monedas para ofrecer y selecciono su monto, selecciono moneda que quiero recibir y monto.
public class CurrencyListToOffer extends CurrenciesList {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final CreateOfferUseCase createOfferUseCase;
    private Currency currency;
    private BigDecimal amount;

    public CurrencyListToOffer(Player player,BlockDynasty.Economy.domain.entities.account.Player target,
                               SearchCurrencyUseCase searchCurrencyUseCase, CreateOfferUseCase createOfferUseCase, IGUI parentGUI){
        super(player, searchCurrencyUseCase, parentGUI);
        this.target = target;
        this.createOfferUseCase = createOfferUseCase;
    }


    @Override
    public IGUI getParent() {
        return super.getParent();
    }
    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    private boolean isFirstStep() {
        return !(super.getParent() instanceof CurrencyListToOffer);
    }

    @Override
    protected String execute(Player sender, Currency currency, java.math.BigDecimal amount) {
        if (isFirstStep()) {
            this.currency = currency;
            this.amount = amount;

            GUIFactory.currencyListToOffer(sender, target, this).open();
            return "Selecciona la moneda que deseas recibir";
        } else {
            CurrencyListToOffer parentGUI = (CurrencyListToOffer) super.getParent();

            Result<Void> result= createOfferUseCase.execute(
                    sender.getUniqueId(),
                    UUID.fromString(target.getUuid()),
                    currency.getSingular(),
                    amount,
                    parentGUI.getCurrency().getPlural(),
                    parentGUI.getAmount()
            );
            if (!result.isSuccess()){
                sender.sendMessage("§cError: " + result.getErrorMessage()+ "."+result.getErrorCode());
            }

            return null;
        }
    }
}
