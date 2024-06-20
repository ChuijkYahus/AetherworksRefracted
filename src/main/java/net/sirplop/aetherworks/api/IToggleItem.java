package net.sirplop.aetherworks.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.network.MessageToggleItem;
import net.sirplop.aetherworks.network.PacketHandler;
import net.sirplop.aetherworks.util.Utils;

public interface IToggleItem {
    default void toggleItem(ItemStack stack, Player player) {
        MessageToggleItem.toggleItem(stack, player);
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new MessageToggleItem());
    }

    default boolean isPoweredOn(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean("poweredOn");
    }
}
