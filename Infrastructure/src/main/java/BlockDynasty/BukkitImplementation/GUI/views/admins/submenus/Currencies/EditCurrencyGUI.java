package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EditCurrencyGUI extends AbstractGUI {
    private final Player player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditCurrencyGUI( Player player, Currency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parentGUI) {
        super("Editar Moneda: " + currency.getSingular(), 4,player, parentGUI);
        this.player = player;
        this.currency = currency;
        this.editCurrencyUseCase =editCurrencyUseCase;

        setupGUI();
    }

    private void setupGUI() {
        // Current currency info
        ChatColor color = ChatColor.valueOf(currency.getColor());
        setItem(4, createItem(Material.GOLD_INGOT,
                        color + currency.getSingular() + " / " + currency.getPlural(),
                        "§7Símbolo: " + color + currency.getSymbol(),
                        "§7Color: " + color + currency.getColor(),
                        "§7Saldo inicial: " + color + currency.getDefaultBalance(),
                        "§7Tasa de cambio: " + color + currency.getExchangeRate(),
                        "§7Pagable: " + (currency.isPayable() ? "§aSí" : "§cNo"),
                        "§7Moneda predeterminada: " + (currency.isDefaultCurrency() ? "§aSí" : "§cNo"),
                        "§7Soporta decimales: " + (currency.isDecimalSupported() ? "§aSí" : "§cNo")),
                null);

        // Edit Start Balance button
        setItem(10, createItem(Material.EMERALD_BLOCK, "§aEditar Saldo Inicial",
                "§7Click para modificar el saldo inicial"), f -> {openStartBalanceInput();});

        // Set Currency Rate button
        setItem(12, createItem(Material.GOLD_NUGGET, "§eEstablecer Tasa de Cambio",
                "§7Click para modificar la tasa de cambio"), f -> {openExchangeRateInput();});

        // Edit Color button
        setItem(14, createItem(MaterialAdapter.getLimeDye(), "§aEditar Color",
                "§7Click para cambiar el color de la moneda"), f -> {openColorSelectionGUI();});

        // Edit Symbol button
        setItem(16, createItem(Material.NAME_TAG, "§eEditar Símbolo",
                "§7Click para cambiar el símbolo de la moneda"), f -> {openSymbolInput();});

        // Set Default Currency button
        setItem(28, createItem(Material.NETHER_STAR, "§bEstablecer como Predeterminada",
                "§7Click para hacer esta moneda predeterminada"), f -> {
            try {
                editCurrencyUseCase.setDefaultCurrency(currency.getSingular());
                player.sendMessage("§a[Banco] §7" + currency.getSingular() + " ahora es la moneda predeterminada.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
        });

        // Toggle Payable button
        setItem(20, createItem(
                        currency.isPayable() ? MaterialAdapter.getLimeConcrete(): MaterialAdapter.getRedConcrete(),
                        currency.isPayable() ? "§aToggle Pagable: §aActivado" : "§cToggle Pagable: §cDesactivado",
                        "§7Click para " + (currency.isPayable() ? "desactivar" : "activar") + " la opción de pago"),
                f -> {
                    try {
                        editCurrencyUseCase.togglePayable(currency.getSingular());
                        player.sendMessage("§a[Banco] §7Opción de pago cambiada");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Edit Singular Name button
        setItem(30, createItem(Material.PAPER, "§eEditar Nombre Singular",
                "§7Click para cambiar el nombre singular"), f -> {openSingularNameInput();});

        // Edit Plural Name button
        setItem(32, createItem(Material.BOOK, "§eEditar Nombre Plural",
                "§7Click para cambiar el nombre plural"), f -> {openPluralNameInput();});

        // Toggle Decimals button
        setItem(24, createItem(
                        currency.isDecimalSupported() ?MaterialAdapter.getLimeConcrete() : MaterialAdapter.getRedConcrete(),
                        currency.isDecimalSupported() ? "§aToggle Decimales: §aActivado" : "§cToggle Decimales: §cDesactivado",
                        "§7Click para " + (currency.isDecimalSupported() ? "desactivar" : "activar") + " soporte de decimales"),
                f -> {
                    try {
                        editCurrencyUseCase.toggleDecimals(currency.getSingular());
                        player.sendMessage("§a[Banco] §7Soporte de decimales cambiado.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Back button
        setItem(34, createItem(Material.BARRIER, "§cVolver",
                "§7Click para volver"), f -> {
            GUIFactory.currencyListEditPanel( player, this.getParent().getParent()).open();
            //this.openParent();
        });
    }

    private void openColorSelectionGUI() {
        GUIFactory.colorSelectorPanel(player,currency,this).open();
    }

    private void openEditCurrencyGUI() {
        GUIFactory.editCurrencyPanel(player, currency, this.getParent()).open();
    }

    private void openStartBalanceInput(){
        AnvilMenu.open(this,player,"saldo inicial",currency.getDefaultBalance().toString(), s->{
            try {
                double startBal = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.editStartBal(currency.getSingular(), startBal);
                    player.sendMessage("§a[Banco] §7Saldo inicial actualizado correctamente.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                    openEditCurrencyGUI();
                }
            }catch (NumberFormatException e){
                return "Formato inválido";
            }
            return null;
        });
    }

    private void openExchangeRateInput(){
        AnvilMenu.open(this,player,"tasa de cambio",String.valueOf(currency.getExchangeRate()),s->{
            try {
                double rate = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.setCurrencyRate(currency.getSingular(), rate);
                    player.sendMessage("§a[Banco] §7Tasa de cambio actualizada correctamente.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                    openEditCurrencyGUI();
                }
            } catch (NumberFormatException e) {
                return "Formato inválido";
            }
            return null;
        });
    }

    private void openSymbolInput(){
        AnvilMenu.open(this,player,"Simbolo de moneda",currency.getSymbol(),s ->{
            try {
                editCurrencyUseCase.editSymbol(currency.getSingular(), s);
                player.sendMessage("§a[Banco] §7Símbolo actualizado correctamente.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openSingularNameInput(){
        AnvilMenu.open(this,player,"Nombre Singular",currency.getSingular(), s ->{
            try {
                editCurrencyUseCase.setSingularName(currency.getSingular(), s);
                player.sendMessage("§a[Banco] §7Nombre singular actualizado correctamente.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openPluralNameInput() {
        AnvilMenu.open(this,player,"Nombre Plural", currency.getPlural(),s->{
            try {
                editCurrencyUseCase.setPluralName(currency.getSingular(), s);
                player.sendMessage("§a[Banco] §7Nombre plural actualizado correctamente.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

}