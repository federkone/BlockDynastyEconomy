package BlockDynasty.BukkitImplementation.adapters.platformAdapter;

import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.ItemStackProvider;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.ItemSerialization;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.NbtData;
import org.bukkit.inventory.ItemStack;

public class ItemStackCurrencyAdapter implements ItemStackCurrency {
    private final ItemStack itemStack;

    public ItemStackCurrencyAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public NbtData getNbtData() {
        if (itemStack == null) {
            return new NbtData();
        }
        return ItemStackProvider.getNBTData(itemStack);
    }

    @Override
    public  int getCantity() {
        return itemStack.getAmount();
    }

    @Override
    public void setCantity(int amount) {
        itemStack.setAmount(amount);
    }

    @Override
    public  String getMaterial() {
        return itemStack.getType().toString();
    }

    @Override
    public String asBase64() {
        return ItemSerialization.toBase64(itemStack);
    }

    @Override
    public int maxStackSize() {
        return itemStack.getType().getMaxStackSize();
    }

    @Override
    public boolean isNull() {
        return itemStack == null || itemStack.getType() == org.bukkit.Material.AIR;
    }

    @Override
    public boolean isSimilar(ItemStackCurrency itemStackCurrency) {
        return this.itemStack.isSimilar((ItemStack) itemStackCurrency.getRoot());
    }

    @Override
    public Object getRoot() {
        return itemStack;
    }
}
