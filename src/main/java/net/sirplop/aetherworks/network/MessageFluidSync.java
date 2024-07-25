package net.sirplop.aetherworks.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageFluidSync {

    public ItemStack held;
    public FluidStack fluid;
    public int capacity;

    public MessageFluidSync(ItemStack held, FluidStack focus, int capacity)
    {
        this.held = held;
        this.fluid = focus;
        this.capacity = capacity;
    }

    public static void encode(MessageFluidSync msg, FriendlyByteBuf buf)
    {
        buf.writeItem(msg.held);
        buf.writeFluidStack(msg.fluid);
        buf.writeInt(msg.capacity);
    }

    public static MessageFluidSync decode(FriendlyByteBuf buf) {

        return new MessageFluidSync(buf.readItem(), buf.readFluidStack(), buf.capacity());
    }

    public static void handle(MessageFluidSync msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                FluidHandlerItemStack stack = new FluidHandlerItemStack(msg.held, msg.capacity);
                setFluid(stack, msg.fluid);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public static void setFluid(FluidHandlerItemStack held, FluidStack focus) {
        if(!held.getContainer().isEmpty()) {
            if (focus.isEmpty()) {
                held.drain(held.getTankCapacity(1), IFluidHandler.FluidAction.EXECUTE);
            }
            else {
                held.drain(held.getFluidInTank(1).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                held.fill(focus, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
}
