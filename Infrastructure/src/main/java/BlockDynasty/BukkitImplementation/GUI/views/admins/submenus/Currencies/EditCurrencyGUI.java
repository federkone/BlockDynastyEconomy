package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditCurrencyGUI extends AbstractGUI {
    private final Player player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final AbstractGUI parentGUI;

    public EditCurrencyGUI( Player player, Currency currency,
                           EditCurrencyUseCase editCurrencyUseCase, SearchCurrencyUseCase searchCurrencyUseCase, AbstractGUI parentGUI) {
        super("Editar Moneda: " + currency.getSingular(), 4);
        this.player = player;
        this.parentGUI = parentGUI;
        this.currency = currency;
        this.editCurrencyUseCase =editCurrencyUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;

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
            parentGUI.open(player);
        });
    }

    private void openColorSelectionGUI() {
        ColorSelectionGUI colorGUI = new ColorSelectionGUI();
        colorGUI.open(player);
    }
    private void openEditCurrencyGUI() {
        EditCurrencyGUI gui = new EditCurrencyGUI(player, currency,editCurrencyUseCase, searchCurrencyUseCase,parentGUI);
        gui.open(player);
    }

    private void openStartBalanceInput(){
        AnvilMenu.open(player,"saldo inicial",currency.getDefaultBalance().toString(), s->{
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
        AnvilMenu.open(player,"tasa de cambio",String.valueOf(currency.getExchangeRate()),s->{
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
        AnvilMenu.open(player,"Simbolo de moneda",currency.getSymbol(),s ->{
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
        AnvilMenu.open(player,"Nombre Singular",currency.getSingular(), s ->{
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
        AnvilMenu.open(player,"Nombre Plural", currency.getPlural(),s->{
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

    // Inner class for color selection
    private class ColorSelectionGUI extends AbstractGUI {

        public ColorSelectionGUI() {
            super("Seleccionar Color", 4);
            setupColorGUI();
        }

        private void setupColorGUI() {
            // Create slots for each color
            setItem(10, createColorItem("WHITE_WOOL", ChatColor.WHITE, "Blanco"),
                    unused -> handleColorSelection(ChatColor.WHITE, "Blanco"));
            setItem(11, createColorItem("YELLOW_WOOL", ChatColor.YELLOW, "Amarillo"),
                    unused -> handleColorSelection(ChatColor.YELLOW, "Amarillo"));
            setItem(12, createColorItem("RED_WOOL", ChatColor.RED, "Rojo"),
                    unused -> handleColorSelection(ChatColor.RED, "Rojo"));
            setItem(13, createColorItem("PINK_WOOL", ChatColor.LIGHT_PURPLE, "Rosa"),
                    unused -> handleColorSelection(ChatColor.LIGHT_PURPLE, "Rosa"));
            setItem(14, createColorItem("PURPLE_WOOL", ChatColor.DARK_PURPLE, "Morado"),
                    unused -> handleColorSelection(ChatColor.DARK_PURPLE, "Morado"));
            setItem(15, createColorItem("ORANGE_WOOL", ChatColor.GOLD, "Dorado"),
                    unused -> handleColorSelection(ChatColor.GOLD, "Dorado"));
            setItem(16, createColorItem("LIME_WOOL", ChatColor.GREEN, "Verde"),
                    unused -> handleColorSelection(ChatColor.GREEN, "Verde"));

            setItem(19, createColorItem("GRAY_WOOL", ChatColor.GRAY, "Gris"),
                    unused -> handleColorSelection(ChatColor.GRAY, "Gris"));
            setItem(20, createColorItem("LIGHT_GRAY_WOOL", ChatColor.DARK_GRAY, "Gris Oscuro"),
                    unused -> handleColorSelection(ChatColor.DARK_GRAY, "Gris Oscuro"));
            setItem(21, createColorItem("CYAN_WOOL", ChatColor.AQUA, "Agua"),
                    unused -> handleColorSelection(ChatColor.AQUA, "Agua"));
            setItem(22, createColorItem("LIGHT_BLUE_WOOL", ChatColor.BLUE, "Azul"),
                    unused -> handleColorSelection(ChatColor.BLUE, "Azul"));
            setItem(23, createColorItem("BLUE_WOOL", ChatColor.DARK_BLUE, "Azul Oscuro"),
                    unused -> handleColorSelection(ChatColor.DARK_BLUE, "Azul Oscuro"));
            setItem(24, createColorItem("BROWN_WOOL", ChatColor.DARK_RED, "Rojo Oscuro"),
                    unused -> handleColorSelection(ChatColor.DARK_RED, "Rojo Oscuro"));
            setItem(25, createColorItem("GREEN_WOOL", ChatColor.DARK_GREEN, "Verde Oscuro"),
                    unused -> handleColorSelection(ChatColor.DARK_GREEN, "Verde Oscuro"));

            // Back button
            setItem(31, createItem(Material.BARRIER, "§cVolver",
                    "§7Click para volver"), unused -> {
                //player.closeInventory();
                openEditCurrencyGUI();
            });
        }

        private void handleColorSelection(ChatColor chatColor, String colorName) {
            //player.closeInventory();
            try {
                editCurrencyUseCase.editColor(currency.getSingular(), chatColor.name());
                player.sendMessage("§a[Banco] §7Color actualizado correctamente a " + colorName + ".");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Banco] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
        }

        private ItemStack createColorItem(String material, ChatColor chatColor, String colorName) {
            return createItem(MaterialAdapter.adaptWool(material), chatColor + colorName,
                    "§7Click para seleccionar este color",
                    chatColor + "Ejemplo: " + currency.getSingular());
        }
    }
}