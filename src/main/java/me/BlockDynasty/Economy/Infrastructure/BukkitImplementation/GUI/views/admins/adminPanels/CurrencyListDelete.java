package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.views.admins.adminPanels;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import org.bukkit.entity.Player;


public class CurrencyListDelete extends AbstractCurrenciesList {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public CurrencyListDelete(BlockDynastyEconomy plugin, Player player) {
            super(plugin, player);
            this.deleteCurrencyUseCase = plugin.getUsesCase().deleteCurrencyUseCase();
    }

    @Override
    public void openSubMenu(Currency currency,Player player) {
        try {
            deleteCurrencyUseCase.deleteCurrency(currency.getSingular());
            player.sendMessage(F.getPrefix() + "§7Deleted currency: §a" + currency.getSingular());
        } catch (CurrencyNotFoundException e) {
            player.sendMessage(F.getPrefix()+"§7"+ e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
        } catch (TransactionException e) {
            player.sendMessage(F.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
        }
    }
}