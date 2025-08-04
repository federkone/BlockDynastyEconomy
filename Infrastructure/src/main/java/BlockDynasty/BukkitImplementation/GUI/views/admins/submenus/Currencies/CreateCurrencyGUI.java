package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyException;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.entity.Player;

public class CreateCurrencyGUI {
    private final Player player;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private String singularName;
    private final IGUI parent;

    public CreateCurrencyGUI(Player player, CreateCurrencyUseCase createCurrencyUseCase, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parent) {
        this.parent = parent;
        this.player = player;
        this.createCurrencyUseCase = createCurrencyUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        openSingularNameInput();
    }

    private void openSingularNameInput() {
        AnvilMenu.open(parent,player,"Nombre Singular","Name..", s -> {
            singularName = s.trim();
            openPluralNameInput();
            return null;
        });
    }

    private void openPluralNameInput() {
        AnvilMenu.open(player,"Nombre plural", "Name..", s -> {
            createCurrency(singularName, s.trim());
            return null;
        });
    }

    private void createCurrency(String singular, String plural) {
        try {
            createCurrencyUseCase.createCurrency(singular, plural);
            player.sendMessage("§6[Banco] §aLa moneda §e" + singular + "§a ha sido creada correctamente.");

            Result<Currency> result = searchCurrencyUseCase.getCurrency(singular);
            if (result.isSuccess()) {
                GUIFactory.editCurrencyPanel(player, result.getValue(), parent).open();
            }
        }
        catch (CurrencyException e) {
            player.sendMessage("§6[Banco] §cError al crear la moneda: §e" + e.getMessage());
            player.closeInventory();
        }
    }
}