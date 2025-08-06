package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.GUIFactory;

public class OfferGUI extends PayGUI{
    private final org.bukkit.entity.Player sender;

    public OfferGUI( org.bukkit.entity.Player sender, IGUI parent){
        super(sender,parent);
        this.sender = sender;
    }

    //tengo jugador voy a lista de monedas
    @Override
    public void openNextSection(BlockDynasty.Economy.domain.entities.account.Player target) {
        GUIFactory.currencyListToOffer(sender, target, this.getParent()).open();  //-> manda a buscar las monedas con sus montos
    }
}
