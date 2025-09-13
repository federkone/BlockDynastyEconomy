package lib.gui.abstractions;

public interface IGUI {
    void open();
    void close();
    void refresh();
    void handleLeftClick(int slot, IEntityGUI player);
    void handleRightClick(int slot, IEntityGUI player);
    int getRows();
    String getTitle();
    IInventory getInventory();
    boolean hasParent();
    IGUI getParent();
    void openParent();
    void fill();
}