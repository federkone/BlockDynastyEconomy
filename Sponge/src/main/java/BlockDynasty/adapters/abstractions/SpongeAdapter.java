package BlockDynasty.adapters.abstractions;

import BlockDynasty.adapters.GUI.adapters.InventoryAdapter;
import BlockDynasty.adapters.GUI.adapters.ItemStackAdapter;
import BlockDynasty.adapters.GUI.adapters.MaterialAdapter;
import BlockDynasty.SpongePlugin;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.Materials;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.plugin.PluginContainer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpongeAdapter implements PlatformAdapter {
    PluginContainer pluginContainer = SpongePlugin.getPlugin();

    @Override
    public IPlayer getPlayer(String name) {
        return Sponge.server().player(name).map(EntityPlayerAdapter::of).orElse(null);
    }
    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        return Sponge.server().player(uuid).map(EntityPlayerAdapter::of).orElse(null);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Sponge.server().onlinePlayers().stream()
                .map(EntityPlayerAdapter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void dispatchCommand(String command) throws Exception {
        Sponge.server().commandManager().process(command);
    }

    @Override
    public IItemStack createItemStack(Materials material) {
        ItemStack itemStack = ItemStack.of(MaterialAdapter.toItemType(material));
        return  new ItemStackAdapter(itemStack);
    }

    @Override
    public IInventory createInventory(String title, int rows) {
        ViewableInventory viewableInventory= ViewableInventory.builder()
                .type(getTypeFromRows(rows))
                .completeStructure()
                .plugin(this.pluginContainer)
                .build();

        return new InventoryAdapter(viewableInventory);
    }

    private ContainerType getTypeFromRows(int rows) {
        switch (rows) {
            case 1:
                return ContainerTypes.GENERIC_9X1.get();
            case 2:
                return ContainerTypes.GENERIC_9X2.get();
            case 3:
                return ContainerTypes.GENERIC_9X3.get();
            case 4:
                return ContainerTypes.GENERIC_9X4.get();
            case 5:
                return ContainerTypes.GENERIC_9X5.get();
            case 6:
                return ContainerTypes.GENERIC_9X6.get();
            default:
                throw new IllegalArgumentException("Invalid number of rows: " + rows);
        }
    }

}
