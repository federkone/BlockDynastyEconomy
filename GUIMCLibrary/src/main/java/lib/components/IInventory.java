package lib.components;

public interface IInventory {
    void set(int slot, IItemStack item);
    void setRows(int rows);
    int getRows();
    void setTitle(String title);
    String getTitle();
    int getSize();
    Object getHandle(); // Returns native Inventory
}