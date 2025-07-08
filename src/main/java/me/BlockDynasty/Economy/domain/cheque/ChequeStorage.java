package me.BlockDynasty.Economy.domain.cheque;

public class ChequeStorage {
    /*private String issuer;
    private String currency;
    private double value;
    public static BlockDynastyEconomy plugin;

    public ChequeStorage(String issuer, String currency, double value,BlockDynastyEconomy plugin) {
        this.issuer = issuer;
        this.currency = currency;
        this.value = value;
        ChequeStorage.plugin = plugin;
    }
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChequeStorage that = (ChequeStorage) o;
        return Objects.equals(issuer, that.issuer) && Objects.equals(currency, that.currency) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, currency, value);
    }
    public static final NamespacedKey key = new NamespacedKey(plugin, "cheque");
    public static ChequeStorage read(ItemStack itemStack){
       ChequeStorage storage = itemStack.getItemMeta().getPersistentDataContainer().get(key,ChequeStorageType.INSTANCE);
       if(storage == null)
           storage = ChequeUpdater.tryUpdate(itemStack);
       return storage;
    }*/

}
