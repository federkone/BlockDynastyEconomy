package BlockDynasty.BukkitImplementation.Integrations.Placeholder;


import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityPlayerAdapter;
import lib.placeholder.PlaceHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;



public class PlaceHolderExpansion extends PlaceholderExpansion {
    private final PlaceHolder placeHolder;

    public PlaceHolderExpansion(PlaceHolder placeHolder) {
        this.placeHolder = placeHolder;
    }

    @Override
    public boolean register() {
        if(!canRegister()){
            return false;
        }
        return super.register();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "BlockDynastyEconomy";
    }

    @Override
    public String getAuthor() {
        return "Nullplague";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getRequiredPlugin(){
        return "BlockDynastyEconomy";
    }

    @Override
    public String onRequest(OfflinePlayer player, String s) {
        return placeHolder.onRequest(EntityPlayerAdapter.of(player.getPlayer()), s);
    }

}