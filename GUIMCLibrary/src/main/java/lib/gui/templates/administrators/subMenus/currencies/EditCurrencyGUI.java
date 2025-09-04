package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AbstractGUI;
import lib.gui.templates.abstractions.ChatColor;

public class EditCurrencyGUI extends AbstractGUI {
    private final IPlayer player;
    private final Currency currency;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final ITextInput textInput;

    public EditCurrencyGUI(IPlayer player, Currency currency, EditCurrencyUseCase editCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super("Edit Currency: " + currency.getSingular(), 5,player, parentGUI);
        this.player = player;
        this.currency = currency;
        this.textInput = textInput;
        this.editCurrencyUseCase =editCurrencyUseCase;

        setupGUI();
    }

    private void setupGUI() {
        // Current currency info
        String color = ChatColor.stringValueOf(currency.getColor());
        setItem(4, createItem(Materials.GOLD_INGOT,
                        color + currency.getSingular() + " / " + currency.getPlural(),
                        "§7Symbol: " + color + currency.getSymbol(),
                        "§7Color: " + color + currency.getColor(),
                        "§7Beginning balance: " + color + currency.getDefaultBalance(),
                        "§7Exchange rate: " + color + currency.getExchangeRate(),
                        "§7Transferable: " + (currency.isTransferable() ? "§aYes" : "§cNo"),
                        "§7Default currency: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"),
                        "§7Supports decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo")),
                null);

        // Edit Start Balance button
        setItem(11, createItem(Materials.EMERALD_BLOCK, "§aEdit Opening Balance",
                "§7Click to modify the initial balance"), f -> {openStartBalanceInput();});

        // Set Currency Rate button
        setItem(13, createItem(Materials.GOLD_NUGGET, "§eSet Exchange Rate",
                "§7Click to modify the exchange rate"), f -> {openExchangeRateInput();});

        // Edit Color button
        setItem(15, createItem(Materials.LIME_DYE, "§aEdit Color",
                "§7Click to change the color of the currency"), f -> {openColorSelectionGUI();});

        // Edit Symbol button
        setItem(20, createItem(Materials.NAME_TAG, "§eEdit Symbol",
                "§7Click to change the currency symbol"), f -> {openSymbolInput();});

        // Set Default Currency button
        setItem(22, createItem(Materials.NETHER_STAR, "§bSet as Default",
                "§7Click to make this currency default"), f -> {
            try {
                editCurrencyUseCase.setDefaultCurrency(currency.getSingular());
                player.sendMessage("§a[Bank] §7" + currency.getSingular() + " is now the default currency.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
        });

        // Toggle Payable button
        setItem(24, createItem(
                        currency.isTransferable() ? Materials.LIME_CONCRETE: Materials.RED_CONCRETE,
                        currency.isTransferable() ? "Transferable: §aActivated" : "Transferable: §cDisabled",
                        "§7Click to " + (currency.isTransferable() ? "Disable" : "Enable"),"This option affects:","Transfer","Pay","Trade/Offers"),
                f -> {
                    try {
                        editCurrencyUseCase.togglePayable(currency.getSingular());
                        player.sendMessage("§a[Bank] §7Transfer option changed");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Edit Singular Name button
        setItem(29, createItem(Materials.PAPER, "§eEdit Singular Name",
                "§7Click to change the singular name"), f -> {openSingularNameInput();});

        // Edit Plural Name button
        setItem(31, createItem(Materials.BOOK, "§eEdit Plural Name",
                "§7Click to change the plural name"), f -> {openPluralNameInput();});

        // Toggle Decimals button
        setItem(33, createItem(
                        currency.isDecimalSupported() ?Materials.LIME_CONCRETE : Materials.RED_CONCRETE,
                        currency.isDecimalSupported() ? "Decimals support: §aActivated" : "Decimals support: §cDisabled",
                        "§7Click to " + (currency.isDecimalSupported() ? "Disable" : "Enable") + " decimal support"),
                f -> {
                    try {
                        editCurrencyUseCase.toggleDecimals(currency.getSingular());
                        player.sendMessage("§a[Bank] §7Decimal support changed.");
                        openEditCurrencyGUI();
                    } catch (Exception e) {
                        player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                        openEditCurrencyGUI();
                    }
                });

        // Back button
        setItem(40, createItem(Materials.BARRIER, "§cBack",
                "§7Click to go back"), f -> {
            GUIFactory.currencyListToEditPanel(player, this.getParent().getParent()).open();
        });
    }

    private void openEditCurrencyGUI() {
        GUIFactory.editCurrencyPanel(player, currency, this.getParent()).open();
    }

    private void openColorSelectionGUI() {
        GUIFactory.colorSelectorPanel(player,currency,this).open();
    }

    private void openStartBalanceInput(){
        textInput.open(this,player,"Initial balance:"+currency.getSingular(),currency.getDefaultBalance().toString(), s->{
            try {
                double startBal = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.editStartBal(currency.getSingular(), startBal);
                    player.sendMessage("§a[Bank] §7Initial balance updated correctly.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                    openEditCurrencyGUI();
                }
            }catch (NumberFormatException e){
                return "Invalid format";
            }
            return null;
        });
    }

    private void openExchangeRateInput(){
        textInput.open(this,player,"Exchange rate:"+currency.getSingular(),String.valueOf(currency.getExchangeRate()),s->{
            try {
                double rate = Double.parseDouble(s);
                try {
                    editCurrencyUseCase.setCurrencyRate(currency.getSingular(), rate);
                    player.sendMessage("§a[Bank] §7Exchange rate updated correctly.");
                    openEditCurrencyGUI();
                } catch (Exception e) {
                    player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                    openEditCurrencyGUI();
                }
            } catch (NumberFormatException e) {
                return "Invalid format";
            }
            return null;
        });
    }

    private void openSymbolInput(){
        textInput.open(this,player,"Symbol:"+currency.getSingular(),currency.getSymbol(),s ->{
            try {
                editCurrencyUseCase.editSymbol(currency.getSingular(), s);
                player.sendMessage("§a[Bank] §7Symbol updated successfully.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openSingularNameInput(){
        textInput.open(this,player,"Singular Name",currency.getSingular(), s ->{
            try {
                editCurrencyUseCase.setSingularName(currency.getSingular(), s);
                player.sendMessage("§a[Bank] §7Singular name updated correctly.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§a[Bank] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }

    private void openPluralNameInput() {
        textInput.open(this,player,"Plural Name", currency.getPlural(),s->{
            try {
                editCurrencyUseCase.setPluralName(currency.getSingular(), s);
                player.sendMessage("§a[Bank] §7Plural noun updated correctly.");
                openEditCurrencyGUI();
            } catch (Exception e) {
                player.sendMessage("§6[Bank] §cError: §e" + e.getMessage());
                openEditCurrencyGUI();
            }
            return null;
        });
    }
}
