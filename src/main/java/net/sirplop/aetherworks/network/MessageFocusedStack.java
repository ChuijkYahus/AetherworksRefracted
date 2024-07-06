package net.sirplop.aetherworks.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.sirplop.aetherworks.Aetherworks;

import java.util.function.Supplier;

public class MessageFocusedStack {
    public static final String FOCUS_TAG = "aw.focus";

    public ItemStack held;
    public ItemStack focus;
    public MessageFocusedStack(ItemStack held, ItemStack focus)
    {
        this.held = held;
        this.focus = focus;
    }

    public static void encode(MessageFocusedStack msg, FriendlyByteBuf buf)
    {
        buf.writeItem(msg.held);
        buf.writeItem(msg.focus);
    }

    public static MessageFocusedStack decode(FriendlyByteBuf buf) {

        return new MessageFocusedStack(buf.readItem(), buf.readItem());
    }

    public static void handle(MessageFocusedStack msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                setFocus(msg.held, msg.focus);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public static void setFocus(ItemStack held, ItemStack focus) {
        if(!held.isEmpty()) {
            if (focus.isEmpty())
                held.getOrCreateTag().remove(FOCUS_TAG);
            else
                held.getOrCreateTag().put(FOCUS_TAG, focus.serializeNBT());
        }
    }
}
