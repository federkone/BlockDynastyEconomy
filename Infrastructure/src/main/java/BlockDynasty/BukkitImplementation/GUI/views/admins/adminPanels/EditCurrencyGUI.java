package BlockDynasty.BukkitImplementation.GUI.views.admins.adminPanels;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class EditCurrencyGUI extends AbstractGUI {
    private final JavaPlugin plugin;
    private final Player player;
    private final Currency currency;
    private final GUIService guiService;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final AbstractGUI parentGUI;

    public EditCurrencyGUI(JavaPlugin plugin, GUIService guiService, Player player, Currency currency,
                           EditCurrencyUseCase editCurrencyUseCase, GetCurrencyUseCase getCurrencyUseCase,AbstractGUI parentGUI) {
        super("Editar Moneda: " + currency.getSingular(), 4);
        this.plugin = plugin;
        this.player = player;
        this.guiService = guiService;
        this.parentGUI = parentGUI;
        this.currency = currency;
        this.editCurrencyUseCase =editCurrencyUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;

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
                "§7Click para modificar el saldo inicial"), unused -> {
            //player.closeInventory();
            openStartBalanceInput();
        });

        // Set Currency Rate button
        setItem(12, createItem(Material.GOLD_NUGGET, "§eEstablecer Tasa de Cambio",
                "§7Click para modificar la tasa de cambio"), unused -> {
            //player.closeInventory();
            openExchangeRateInput();
        });

        // Edit Color button
        setItem(14, createItem(MaterialAdapter.getLimeDye(), "§aEditar Color",
                "§7Click para cambiar el color de la moneda"), unused -> {
            //player.closeInventory();
            openColorSelectionGUI();
        });

        // Edit Symbol button
        setItem(16, createItem(Material.NAME_TAG, "§eEditar Símbolo",
                "§7Click para cambiar el símbolo de la moneda"), unused -> {
            //player.closeInventory();
            openSymbolInput();
        });

        // Set Default Currency button
        setItem(28, createItem(Material.NETHER_STAR, "§bEstablecer como Predeterminada",
                "§7Click para hacer esta moneda predeterminada"), unused -> {
            //player.closeInventory();
            try {
                editCurrencyUseCase.setDefaultCurrency(currency.getSingular());
                player.sendMessage("§6[Banco] §a" + currency.getSingular() + " ahora es la moneda predeterminada.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
        });

        // Toggle Payable button
        setItem(20, createItem(
                        currency.isPayable() ? MaterialAdapter.getLimeConcrete(): MaterialAdapter.getRedConcrete(),
                        currency.isPayable() ? "§aToggle Pagable: §aActivado" : "§cToggle Pagable: §cDesactivado",
                        "§7Click para " + (currency.isPayable() ? "desactivar" : "activar") + " la opción de pago"),
                unused -> {
                    //player.closeInventory();
                    try {
                        editCurrencyUseCase.togglePayable(currency.getSingular());
                        player.sendMessage("§6[Banco] §aOpción de pago cambiada");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Edit Singular Name button
        setItem(30, createItem(Material.PAPER, "§eEditar Nombre Singular",
                "§7Click para cambiar el nombre singular"), unused -> {
            //player.closeInventory();
            openSingularNameInput();
        });

        // Edit Plural Name button
        setItem(32, createItem(Material.BOOK, "§eEditar Nombre Plural",
                "§7Click para cambiar el nombre plural"), unused -> {
            //player.closeInventory();
            openPluralNameInput();
        });

        // Toggle Decimals button
        setItem(24, createItem(
                        currency.isDecimalSupported() ?MaterialAdapter.getLimeConcrete() : MaterialAdapter.getRedConcrete(),
                        currency.isDecimalSupported() ? "§aToggle Decimales: §aActivado" : "§cToggle Decimales: §cDesactivado",
                        "§7Click para " + (currency.isDecimalSupported() ? "desactivar" : "activar") + " soporte de decimales"),
                unused -> {
                    //player.closeInventory();
                    try {
                        editCurrencyUseCase.toggleDecimals(currency.getSingular());
                        player.sendMessage("§6[Banco] §aSoporte de decimales cambiado.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Back button
        setItem(34, createItem(Material.BARRIER, "§cVolver",
                "§7Click para volver"), unused -> {
            player.openInventory(parentGUI.getInventory());
            guiService.registerGUI(player, parentGUI);
        });
    }

    private void openStartBalanceInput() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    try {
                        double startBal = Double.parseDouble(stateSnapshot.getText());
                        return List.of(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> {
                                    try {
                                        editCurrencyUseCase.editStartBal(currency.getSingular(), startBal);
                                        player.sendMessage("§6[Banco] §aSaldo inicial actualizado correctamente.");
                                        openEditCurrencyGUI();
                                    } catch (Exception e) {
                                        player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                                        openEditCurrencyGUI();
                                    }
                                })
                        );
                    } catch (NumberFormatException e) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cFormato inválido"));
                    }
                })
                .text(currency.getDefaultBalance().toString())
                .title("Saldo Inicial")
                .plugin(plugin)
                .open(player);
    }

    private void openExchangeRateInput() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    try {
                        double rate = Double.parseDouble(stateSnapshot.getText());
                        return List.of(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> {
                                    try {
                                        editCurrencyUseCase.setCurrencyRate(currency.getSingular(), rate);
                                        player.sendMessage("§6[Banco] §aTasa de cambio actualizada correctamente.");
                                        openEditCurrencyGUI();
                                    } catch (Exception e) {
                                        player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                                        openEditCurrencyGUI();
                                    }
                                })
                        );
                    } catch (NumberFormatException e) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cFormato inválido"));
                    }
                })
                .text(String.valueOf(currency.getExchangeRate()))
                .title("Tasa de Cambio")
                .plugin(plugin)
                .open(player);
    }

    private void openColorSelectionGUI() {
        ColorSelectionGUI colorGUI = new ColorSelectionGUI();
        player.openInventory(colorGUI.getInventory());
        guiService.registerGUI(player, colorGUI);
    }

    private void openSymbolInput() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String symbol = stateSnapshot.getText().trim();
                    if (symbol.isEmpty()) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cSímbolo no válido"));
                    }

                    return List.of(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {
                                try {
                                    editCurrencyUseCase.editSymbol(currency.getSingular(), symbol);
                                    player.sendMessage("§6[Banco] §aSímbolo actualizado correctamente.");
                                    openEditCurrencyGUI();
                                } catch (Exception e) {
                                    player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                                    openEditCurrencyGUI();
                                }
                            })
                    );
                })
                .text(currency.getSymbol())
                .title("Símbolo de Moneda")
                .plugin(plugin)
                .open(player);
    }

    private void openSingularNameInput() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String name = stateSnapshot.getText().trim();
                    if (name.isEmpty()) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cNombre no válido"));
                    }

                    return List.of(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {
                                try {
                                    editCurrencyUseCase.setSingularName(currency.getSingular(), name);
                                    player.sendMessage("§6[Banco] §aNombre singular actualizado correctamente.");
                                    openEditCurrencyGUI();
                                } catch (Exception e) {
                                    player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                                    openEditCurrencyGUI();
                                }
                            })
                    );
                })
                .text(currency.getSingular())
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

                    String name = stateSnapshot.getText().trim();
                    if (name.isEmpty()) {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText("§cNombre no válido"));
                    }

                    return List.of(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {
                                try {
                                    editCurrencyUseCase.setPluralName(currency.getSingular(), name);
                                    player.sendMessage("§6[Banco] §aNombre plural actualizado correctamente.");
                                    openEditCurrencyGUI();
                                } catch (Exception e) {
                                    player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
                                    openEditCurrencyGUI();
                                }
                            })
                    );
                })
                .text(currency.getPlural())
                .title("Nombre Plural")
                .plugin(plugin)
                .open(player);
    }

    private void openEditCurrencyGUI() {
        EditCurrencyGUI gui = new EditCurrencyGUI(plugin, guiService,player, currency,editCurrencyUseCase, getCurrencyUseCase,parentGUI);
        player.openInventory(gui.getInventory());
        guiService.registerGUI(player, gui);
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
                player.sendMessage("§6[Banco] §aColor actualizado correctamente a " + colorName + ".");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§6[Banco] §cError: §e" + e.getMessage());
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