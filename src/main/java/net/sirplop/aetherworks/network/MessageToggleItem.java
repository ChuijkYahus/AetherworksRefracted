package net.sirplop.aetherworks.network;

import com.rekindled.embers.util.EmberInventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.sirplop.aetherworks.api.IToggleItem;
import net.sirplop.aetherworks.util.AWConfig;

import java.util.function.Supplier;

public class MessageToggleItem {
    public MessageToggleItem() { }

    public static void encode(MessageToggleItem msg, FriendlyByteBuf buf) { }

    public static MessageToggleItem decode(FriendlyByteBuf buf) {
        return new MessageToggleItem();
    }

    public static void handle(MessageToggleItem msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                handleClient(msg);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(MessageToggleItem msg) {
        Player player = Minecraft.getInstance().player;
        toggleItem(player.getMainHandItem(), player);
    }

    public static void toggleItem(ItemStack stack, Player player) {
        if(stack.getItem() instanceof IToggleItem){
            if (!stack.hasTag()) {
                stack.getOrCreateTag();
                stack.getTag().putBoolean("poweredOn", EmberInventoryUtil.getEmberTotal(player) > AWConfig.TOOL_EMBER_USE.get());
            } else {
                stack.getTag().putBoolean("poweredOn",
                        !stack.getTag().getBoolean("poweredOn"));
            }
        }
    }
}
