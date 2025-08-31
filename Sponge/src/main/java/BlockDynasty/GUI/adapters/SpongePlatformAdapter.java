package BlockDynasty.GUI.adapters;

import BlockDynasty.SpongePlugin;
import lib.components.IInventory;
import lib.components.IItemStack;
import lib.components.IPlayer;
import lib.components.Materials;
import lib.templates.abstractions.AbstractGUI;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.*;

import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.plugin.PluginContainer;

import java.util.*;
import java.util.stream.Collectors;

public class SpongePlatformAdapter implements AbstractGUI.PlatformAdapter {
    PluginContainer pluginContainer = SpongePlugin.getPlugin();

    @Override
    public IItemStack createItemStack(Materials material, String name, String... lore) {
        ItemStack itemStack = ItemStack.of(MaterialAdapter.toItemType(material));

        SpongeItemStack spongeItemStack = new SpongeItemStack(itemStack);
        spongeItemStack.setDisplayName(name);
        if (lore != null) {
            spongeItemStack.setLore(Arrays.asList(lore));
        }
        return spongeItemStack;
    }

    @Override
    public IInventory createInventory(AbstractGUI gui) {
        ViewableInventory viewableInventory= ViewableInventory.builder()
                .type(getTypeFromRows(gui.getRows()))
                .completeStructure()
                .plugin(this.pluginContainer)
                .build();

        return new SpongeInventory(viewableInventory);
    }

    @Override
    public Optional<IPlayer> getPlayerOnlineByUUID(UUID uuid) {
        return Sponge.server().player(uuid)
                .map(SpongePlayer::new);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return Sponge.server().onlinePlayers().stream()
                .map(SpongePlayer::new)
                .collect(Collectors.toList());
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
