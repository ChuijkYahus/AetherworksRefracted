package net.sirplop.aetherworks.network;

import com.rekindled.embers.util.EmberInventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.sirplop.aetherworks.api.item.IToggleItem;
import net.sirplop.aetherworks.api.item.IToggleEmberItem;
import net.sirplop.aetherworks.AWConfig;

import java.util.function.Supplier;

public class MessageToggleItem {
    public byte maxValue;

    public MessageToggleItem(byte max)
    {
        maxValue = max;
    }

    public static void encode(MessageToggleItem msg, FriendlyByteBuf buf)
    {
        buf.writeInt(msg.maxValue);
    }

    public static MessageToggleItem decode(FriendlyByteBuf buf) {

        return new MessageToggleItem(buf.readByte());
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
        toggleItem(player.getMainHandItem(), player, msg.maxValue);
    }

    public static void toggleItem(ItemStack stack, Player player, int max) {
        if(!stack.isEmpty() && stack.getItem() instanceof IToggleItem) {
            if (stack.getItem() instanceof  IToggleEmberItem) { //toggle and check min ember use config.
                if (!stack.hasTag()) {
                    stack.getOrCreateTag();
                    stack.getTag().putByte(IToggleItem.KEY, (byte) (EmberInventoryUtil.getEmberTotal(player) >= AWConfig.TOOL_EMBER_USE.get() ? 1 : 0));
                } else {
                    int val = (byte) (stack.getTag().getByte(IToggleItem.KEY) + 1);
                    if (val > max || EmberInventoryUtil.getEmberTotal(player) < AWConfig.TOOL_EMBER_USE.get())
                        val = 0;
                    stack.getTag().putInt(IToggleItem.KEY, val);
                }
            } else if (!stack.hasTag()) {
                stack.getOrCreateTag();
                stack.getTag().putByte(IToggleItem.KEY, (byte)1);
            } else {
                byte val = (byte) (stack.getTag().getByte(IToggleItem.KEY) + 1);
                if (val > max)
                    val = 0;
                stack.getTag().putByte(IToggleItem.KEY, val);
            }
        }
    }
}
