package BlockDynasty.Economy.domain.entities.cheque;

//import de.tr7zw.nbtapi.NBTItem;

public class ChequeUpdater {
   /* private static boolean nbtApiInstalled;
    private static BlockDynastyEconomy plugin=BlockDynastyEconomy.getInstance();

    public ChequeUpdater(){
        // Constructor is not needed, but can be used for initialization if required
    }
    static {
        nbtApiInstalled = Bukkit.getPluginManager().getPlugin("NBTAPI") != null;
    }

    public static void tryApplyFallback(ItemStack stack, ChequeStorage storage){
        if (!nbtApiInstalled) return;
            NBTItem nbtItem = new NBTItem(stack);
            nbtItem.setString("issuer", storage.getIssuer());
            nbtItem.setString("value", String.valueOf(storage.getValue()));
            nbtItem.setString("currency", storage.getCurrency());
            nbtItem.applyNBT(stack);
    }

    public static ChequeStorage tryUpdate(ItemStack stack) {
        if (!nbtApiInstalled) return null;
        try {
            NBTItem nbtItem = new NBTItem(stack);
            String issuer = nbtItem.getString("issuer");
            String value = nbtItem.getString("value");
            String currency = nbtItem.getString("currency");

            if (StringUtils.isEmpty(issuer) && StringUtils.isEmpty(value) && StringUtils.isEmpty(currency)) {
                return null;
            }

            if (StringUtils.isEmpty(issuer)) {
                issuer = UtilString.colorize(plugin.getConfig().getString("cheque.console_name"));
            }
            if (StringUtils.isEmpty(value)) {
                value = "0.0";
            }
            if (StringUtils.isEmpty(currency)) {
                currency = plugin.getCurrencyManager().getDefaultCurrency().getPlural();
            }
            return new ChequeStorage(issuer, currency, Double.parseDouble(value),plugin);
        } catch (Throwable throwable) {
            return null;
        }
    }*/
}
