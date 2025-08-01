package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyException;
import org.bukkit.entity.Player;

public class CreateCurrencyGUI {
    private final Player player;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private String singularName;

    public CreateCurrencyGUI( Player player,CreateCurrencyUseCase createCurrencyUseCase) {
        this.player = player;
        this.createCurrencyUseCase = createCurrencyUseCase;

        openSingularNameInput();
    }

    private void openSingularNameInput() {
        AnvilMenu.open(player,"Nombre Singular","Name..", s -> {
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
        }
        catch (CurrencyException e) {
            player.sendMessage("§6[Banco] §cError al crear la moneda: §e" + e.getMessage());
        }
    }
}