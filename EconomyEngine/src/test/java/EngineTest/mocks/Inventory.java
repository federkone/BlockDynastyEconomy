package EngineTest.mocks;

import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;

public class Inventory implements IInventory {
    //matriz de items [][]
    private String title="Inventory";
    private final int columns=9;
    private IItemStack[][] inventory;


    public Inventory(String title, int rows) {
        this.title = title;
        inventory = new IItemStack[columns][rows];
    }

    @Override
    public void set(int slot, IItemStack item) {
        int column= slot % columns;
        int row= slot / columns;
        this.inventory[column][row] = item;
        //System.out.println(this.inventory[column][row].toString());
    }

    @Override
    public void setRows(int rows) {
        inventory = new IItemStack[columns][rows];
    }

    @Override
    public int getRows() {
        return inventory[0].length;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public int getSize() {
        return columns*getRows();
    }

    @Override
    public Object getHandle() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int slotWidth = 16;
        int slotHeight = 2; // Doble altura
        int titlePadding = (columns * (slotWidth + 1) + 1 - title.length() - 12) / 2;

        // Título centrado dinámicamente
        sb.append("+");
        sb.append("-".repeat(Math.max(0, titlePadding)));
        sb.append(" ").append(title).append(" ");
        sb.append("-".repeat(Math.max(0, titlePadding)));
        sb.append("+\n");

        for (int r = 0; r < getRows(); r++) {
            // Borde superior más ancho
            sb.append("|");
            for (int c = 0; c < columns; c++) {
                sb.append("-".repeat(slotWidth)).append("|");
            }
            sb.append("\n");

            // PRIMERA LÍNEA de la celda (doble altura)
            sb.append("|");
            for (int c = 0; c < columns; c++) {
                IItemStack item = inventory[c][r];
                String cellContent;

                if (item != null) {
                    String materialName = item.toString();
                    // Primera línea: mostrar hasta slotWidth - 2 caracteres
                    String firstLine = materialName.length() > slotWidth - 2 ?
                            materialName.substring(0, slotWidth - 2) : materialName;
                    cellContent = String.format(" %-" + (slotWidth - 2) + "s ", firstLine);
                } else {
                    cellContent = " ".repeat(slotWidth - 2);
                    cellContent = " " + cellContent + " ";
                }
                sb.append(cellContent).append("|");
            }
            sb.append("\n");

            // SEGUNDA LÍNEA de la celda (doble altura)
            sb.append("|");
            for (int c = 0; c < columns; c++) {
                IItemStack item = inventory[c][r];
                String cellContent;

                if (item != null) {
                    String materialName = item.toString();
                    // Segunda línea: mostrar el resto del texto si hay más contenido
                    if (materialName.length() > slotWidth - 2) {
                        int startIndex = slotWidth - 2;
                        String secondLine = materialName.length() > (slotWidth - 2) * 2 ?
                                materialName.substring(startIndex, startIndex + slotWidth - 2) :
                                materialName.substring(startIndex);
                        cellContent = String.format(" %-" + (slotWidth - 2) + "s ", secondLine);
                    } else {
                        // Si no hay más texto, dejar vacío
                        cellContent = " ".repeat(slotWidth - 2);
                        cellContent = " " + cellContent + " ";
                    }
                } else {
                    cellContent = " ".repeat(slotWidth - 2);
                    cellContent = " " + cellContent + " ";
                }
                sb.append(cellContent).append("|");
            }
            sb.append("\n");
        }

        // Borde inferior más ancho
        sb.append("|");
        for (int c = 0; c < columns; c++) {
            sb.append("-".repeat(slotWidth)).append("|");
        }
        sb.append("\n");

        return sb.toString();
    }
}
