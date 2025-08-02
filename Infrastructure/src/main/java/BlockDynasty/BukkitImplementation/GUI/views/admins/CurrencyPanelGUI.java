package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.CreateCurrencyGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.CurrencyListDelete;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.CurrencyListEdit;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CurrencyPanelGUI extends AbstractGUI {
    private final Player player;
    private final AbstractGUI parent;
    private  final CreateCurrencyUseCase createCurrencyUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public CurrencyPanelGUI( Player player, SearchCurrencyUseCase searchCurrencyUseCase,
                            EditCurrencyUseCase editCurrencyUseCase, CreateCurrencyUseCase createCurrencyUseCase,
                            DeleteCurrencyUseCase deleteCurrencyUseCase,AbstractGUI parent) {
        super("Administrador de Monedas", 3);
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.createCurrencyUseCase = createCurrencyUseCase;
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;
        this.player = player;
        this.parent = parent;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Material.EMERALD, "§aCrear Moneda",
                "§7Click para crear una nueva moneda"), unused -> {
            player.closeInventory();
            new CreateCurrencyGUI(player, this.createCurrencyUseCase);
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
            parent.open(player);
        });
    }

    private void openCurrencyListGUI() {
        CurrencyListEdit currencyListEdit = new CurrencyListEdit(player,this.searchCurrencyUseCase,this.editCurrencyUseCase,this);
        currencyListEdit.open(player);
    }

    private void currencyListDelete() {
        CurrencyListDelete currencyListDelete = new CurrencyListDelete( player,this.searchCurrencyUseCase,this.deleteCurrencyUseCase,this);
        currencyListDelete.open(player);
    }
}