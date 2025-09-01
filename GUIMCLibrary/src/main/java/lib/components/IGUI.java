package lib.components;

public interface IGUI {
    void open();
    void close();
    void refresh();
    void handleLeftClick(int slot, IPlayer player);
    void handleRightClick(int slot, IPlayer player);
    int getRows();
    String getTitle();
    IInventory getInventory();
    boolean hasParent();
    IGUI getParent();
    void openParent();
    void fill();
}