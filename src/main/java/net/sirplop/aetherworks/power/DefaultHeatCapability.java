package net.sirplop.aetherworks.power;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.sirplop.aetherworks.api.capabilities.AetherworksCapabilities;
import net.sirplop.aetherworks.api.power.IHeatCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultHeatCapability implements IHeatCapability {

    private double heat = 0;
    private double capacity = 0;

    private final LazyOptional<IHeatCapability> holder;

    public DefaultHeatCapability() {
        holder = LazyOptional.of(() -> this);
    }

    public DefaultHeatCapability(IHeatCapability capability) {
        holder = LazyOptional.of(() -> capability);
    }


    @Override
    public double getHeat() {
        return heat;
    }

    @Override
    public double getHeatCapacity() {
        return capacity;
    }

    @Override
    public void setHeat(double value) {
        heat = value;
    }

    @Override
    public void setHeatCapacity(double value) {
        capacity = value;
    }

    @Override
    public double addAmount(double value, boolean doAdd) {
        double added = Math.min(capacity - heat, value);
        double newEmber = heat + added;
        if (doAdd){
            if(newEmber != heat)
                onContentsChanged();
            heat += added;
        }
        return added;
    }

    @Override
    public double removeAmount(double value, boolean doRemove) {
        double removed = Math.min(heat, value);
        double newEmber = heat - removed;
        if (doRemove){
            if(newEmber != heat)
                onContentsChanged();
            heat -= removed;
        }
        return removed;
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        nbt.putDouble(HEAT, heat);
        nbt.putDouble(HEAT_CAPACITY, capacity);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains(HEAT)){
            heat = nbt.getDouble(HEAT);
        }
        if (nbt.contains(HEAT_CAPACITY)){
            capacity = nbt.getDouble(HEAT_CAPACITY);
        }
    }

    @Override
    public void onContentsChanged() {

    }

    @Override
    public void invalidate() {
        holder.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == AetherworksCapabilities.HEAT_CAPABILITY)
            return AetherworksCapabilities.HEAT_CAPABILITY.orEmpty(cap, holder);
        return LazyOptional.empty();
    }
}
