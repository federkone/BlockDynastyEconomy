package BlockDynasty.BukkitImplementation.GUI.views.admins.adminPanels;

import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyException;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class CreateCurrencyGUI {
    private final JavaPlugin plugin;
    private final Player player;
    private final CreateCurrencyUseCase createCurrencyUseCase;
    private String singularName;

    public CreateCurrencyGUI(JavaPlugin plugin, Player player,CreateCurrencyUseCase createCurrencyUseCase) {
        this.plugin = plugin;
        this.player = player;
        this.createCurrencyUseCase = createCurrencyUseCase;

        openSingularNameInput();
    }

    private void openSingularNameInput() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String input = stateSnapshot.getText().trim();
                    if (input.isEmpty()) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cNombre no válido"));
                    }

                    // Save the singular name and proceed to plural input
                    singularName = input;
                    return List.of(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> openPluralNameInput())
                    );
                })
                .text("Dólar")
                .title("Nombre Singular")
                .plugin(plugin)
                .open(player);
    }

    private void openPluralNameInput() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String pluralName = stateSnapshot.getText().trim();
                    if (pluralName.isEmpty()) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cNombre no válido"));
                    }

                    // Execute the create currency use case
                    return List.of(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> createCurrency(singularName, pluralName))
                    );
                })
                .text("Dólares")
                .title("Nombre Plural")
                .plugin(plugin)
                .open(player);
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