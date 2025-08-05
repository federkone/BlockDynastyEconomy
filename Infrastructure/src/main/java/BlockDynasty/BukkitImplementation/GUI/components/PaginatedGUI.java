package BlockDynasty.BukkitImplementation.GUI.components;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PaginatedGUI<T> extends AbstractGUI {
    protected int currentPage = 0;
    protected final int itemsPerPage;
    protected final int rows;

    public PaginatedGUI(String title, int rows, Player player, IGUI parent, int itemsPerPage) {
        super(title, Math.max(3, rows), player, parent);
        this.rows = Math.max(3, rows);

        // Calculate maximum safe items per page based on available slots
        // Subtract 1 row (9 slots) for navigation buttons
        int availableRows = this.rows - 1;
        int maxSafeItems = availableRows * 7; // 7 slots per row (avoiding edge columns)

        // Ensure itemsPerPage doesn't exceed available space
        this.itemsPerPage = Math.min(itemsPerPage, maxSafeItems);
    }

    /**
     * Display items with pagination
     */
    protected void showItemsPage(List<T> items) {
        // Calculate pagination
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());

        clearGui();

        if (items.isEmpty()) {
            setItem(getEmptyMessageSlot(), createEmptyMessage(), null);
            setItem(getBackButtonSlot(), createBackButton(), unused -> this.openParent());
            return;
        }

        // Add items to GUI
        int slot = getStartingSlot();
        for (int i = startIndex; i < endIndex; i++) {
            T item = items.get(i);
            final T finalItem = item; // Make final for lambda
            setItem(slot, createItemFor(item), unused -> handleItemClick(finalItem));
            slot = getNextSlot(slot);
        }

        // Navigation buttons
        if (currentPage > 0) {
            setItem(getPreviousButtonSlot(), createPreviousButton(), unused -> {
                currentPage--;
                showItemsPage(items);
            });
        }

        if (endIndex < items.size()) {
            setItem(getNextButtonSlot(), createNextButton(), unused -> {
                currentPage++;
                showItemsPage(items);
            });
        }

        // Back button
        setItem(getBackButtonSlot(), createBackButton(), unused -> this.openParent());

        // Add additional custom buttons
        addCustomButtons();
    }

    // Abstract methods
    protected abstract ItemStack createItemFor(T item);
    protected void handleItemClick(T item){

    };
    protected  int getStartingSlot(){
        // Default starting slot is 10, can be overridden
        return 10;
    };
    protected int getNextSlot(int currentSlot){
        // Adjust slot position (skip edge columns)
        currentSlot++;
        if (currentSlot % 9 == 8) currentSlot += 2;
        return currentSlot;
    };

    // Methods with dynamic positions based on inventory size
    protected int getEmptyMessageSlot() {
        return (rows * 9) / 2; // Center of the inventory
    }

    protected int getPreviousButtonSlot() {
        return (rows - 1) * 9 + 2; // Bottom row, left side
    }

    protected int getNextButtonSlot() {
        return (rows - 1) * 9 + 6; // Bottom row, right side
    }

    protected int getBackButtonSlot() {
        return (rows - 1) * 9 + 4; // Bottom row, center
    }

    protected ItemStack createEmptyMessage() {
        return createItem(Material.BARRIER, "§cNo Elements", "§7There are no items to display");
    }

    protected ItemStack createPreviousButton() {
        return createItem(Material.ARROW, "§aPrevious page", "§7Click to see previous items");
    }

    protected ItemStack createNextButton() {
        return createItem(Material.ARROW, "§aNext Page", "§7Click to see more items");
    }

    protected ItemStack createBackButton() {
        return createItem(Material.BARRIER, "§cBack", "§7Click to go back");
    }

    protected void addCustomButtons() {
        // Default implementation does nothing
    }
}
