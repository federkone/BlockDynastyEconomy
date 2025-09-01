package BlockDynasty.GUI.adapters;

import lib.components.IItemStack;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ItemStackAdapter implements IItemStack {
    ItemStack itemStack;

    public ItemStackAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public IItemStack setDisplayName(String name) {
        if(this.itemStack.type().equals( ItemTypes.PLAYER_HEAD.get())){
            Optional<ServerPlayer> optional= Sponge.server().player(name);
            optional.ifPresent(serverPlayer -> this.itemStack.offer(Keys.GAME_PROFILE, serverPlayer.profile()));
        }
        this.itemStack.offer(Keys.CUSTOM_NAME, Component.text(name));
        return this;
    }

    @Override
    public IItemStack setLore(List<String> lore) {
        List<Component> loreComponents = lore.stream()
                .map(Component::text)
                .collect(Collectors.toList());
        this.itemStack.offer(Keys.LORE, loreComponents);
        return this;
    }

    @Override
    public Object getHandle() {
        return itemStack;
    }
}
