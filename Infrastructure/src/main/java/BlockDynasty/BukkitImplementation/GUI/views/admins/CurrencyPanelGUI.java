package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CurrencyPanelGUI extends AbstractGUI {
    private final Player player;
    private final AbstractGUI parent;

    public CurrencyPanelGUI( Player player, AbstractGUI parent) {
        super("Administrador de Monedas", 3,player);
        this.player = player;
        this.parent = parent;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Material.EMERALD, "§aCrear Moneda",
                "§7Click para crear una nueva moneda"), unused -> {
            GUIFactory.createCurrencyPanel(player);

        });

        // Delete Currency button
        setItem(12, createItem(Material.REDSTONE, "§cEliminar Moneda",
                "§7Click para eliminar una moneda existente"), unused -> {
            currencyListDelete();
        });

        // Edit Currency button
        setItem(14, createItem(Material.BOOK, "§eEditar Moneda",
                "§7Click para editar una moneda existente"), unused -> {
            openCurrencyListGUI();
        });

        // Toggle Features button
        setItem(16, createItem(Material.PAPER, "§bConfigurar Características",
                "§7Click para activar/desactivar características"), unused -> {
            player.sendMessage("§a[Banco] §7Función no disponible en este momento.");
        });

        // Exit button
        setItem(22, createItem(Material.BARRIER, "§cAtrás",
                "§7Click para atrás"), unused -> {
            parent.open();
        });
    }

    private void openCurrencyListGUI() {
        GUIFactory.currencyListEditPanel(player, this).open();
    }

    private void currencyListDelete() {
        GUIFactory.currencyListDeletePanel(player, this).open();
    }
}