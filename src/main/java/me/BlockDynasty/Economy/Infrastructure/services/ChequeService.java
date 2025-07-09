
package me.BlockDynasty.Economy.Infrastructure.services;

//todo: para utilizar la feature cheque, debemos declarar como dependencia a NBTAPI para manejar items del juego
public class ChequeService {
/*
    private final BlockDynastyEconomy plugin;
    private final ItemStack chequeBaseItem;

    public ChequeManager(BlockDynastyEconomy plugin) {
        this.plugin = plugin;

        ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("cheque.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(UtilString.colorize(plugin.getConfig().getString("cheque.name")));
        meta.setLore(UtilString.colorize(plugin.getConfig().getStringList("cheque.lore")));
        item.setItemMeta(meta);
        chequeBaseItem = item;
    }

    public ItemStack write(String creatorName, Currency currency, double amount) {
        if(!currency.isPayable()) return null;

        if (creatorName.equals("CONSOLE")) {
            creatorName = UtilString.colorize(plugin.getConfig().getString("cheque.console_name"));
        }
        List<String> formatLore = new ArrayList<>();

        for (String baseLore2 : Objects.requireNonNull(chequeBaseItem.getItemMeta().getLore())) {
            formatLore.add(baseLore2.replace("{value}", currency.format(BigDecimal.valueOf(amount))).replace("{player}", creatorName));
        }
        ItemStack ret = chequeBaseItem.clone();
        ItemMeta meta = ret.getItemMeta();
        meta.setLore(formatLore);
        ChequeStorage storage = new ChequeStorage(creatorName,currency.getPlural(), amount,plugin);
        meta.getPersistentDataContainer().set(ChequeStorage.key, ChequeStorageType.INSTANCE,storage);
        ChequeUpdater.tryApplyFallback(ret, storage); //Backward compatibility
        ret.setItemMeta(meta);
        return ret;
    }

    public boolean isValid(ItemStack itemstack) {
        ChequeStorage storage = ChequeStorage.read(itemstack);
        return storage != null && StringUtils.isNotBlank(storage.getCurrency())&& StringUtils.isNotBlank(storage.getIssuer());
    }

    public double getValue(ItemStack itemstack) {
        ChequeStorage storage = ChequeStorage.read(itemstack);
        if(storage != null){
           return storage.getValue();
        }
        return 0;
    }

    /**
     *
     * @param itemstack - The Cheque.
     * @return - Currency it represents.
     */
    /*
    public Currency getCurrency(ItemStack itemstack) {
        ChequeStorage storage = ChequeStorage.read(itemstack);
        if(storage != null){
            return plugin.getCurrencyManager().getCurrency(storage.getCurrency());
        }
        return plugin.getCurrencyManager().getDefaultCurrency();
    }*/
}
