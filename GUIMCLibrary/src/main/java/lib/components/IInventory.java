package lib.components;

public interface IInventory {
    void set(int slot, IItemStack item);
    int getSize();
    Object getHandle(); // Returns native Inventory
}