package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.GUIFactory;

import java.util.Arrays;

public class OfferGUI extends PayGUI{
    private final org.bukkit.entity.Player sender;

    public OfferGUI( org.bukkit.entity.Player sender, IGUI parent){
        super(sender,parent);
        this.sender = sender;
    }

    @Override
    public void openNextSection(BlockDynasty.Economy.domain.entities.account.Player target) {
        GUIFactory.currencyListToOffer(sender, target, this.getParent()).open();
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(org.bukkit.Material.PAPER, "§aSelect Player to Offer",
                        Arrays.asList("§7Click to select the player you want to offer", "§7And before that, the Currencies")),
                unused -> {});
    }
}
