package net.sirplop.aetherworks.api.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.sirplop.aetherworks.network.MessageToggleItem;
import net.sirplop.aetherworks.network.PacketHandler;

public interface IToggleItem {
    String KEY = "toggleState";

    default void toggleItem(ItemStack stack, Player player, byte max) {
        MessageToggleItem.toggleItem(stack, player, max);
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new MessageToggleItem(max));
    }

    default int getToggled(ItemStack stack) {
        if (stack != null && stack.getOrCreateTag().contains(KEY))
            return stack.getOrCreateTag().getInt(KEY);
        return 0;
    }
}
