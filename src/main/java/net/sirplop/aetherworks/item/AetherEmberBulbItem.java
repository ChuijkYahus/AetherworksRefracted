package net.sirplop.aetherworks.item;


import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.item.EmberStorageItem;
import com.rekindled.embers.item.IEmbersCurioItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.sirplop.aetherworks.AWConfig;
import net.sirplop.aetherworks.util.MoonlightRepair;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class AetherEmberBulbItem extends EmberStorageItem implements IEmbersCurioItem {

    public static final double CAPACITY = 750.0;

    public AetherEmberBulbItem(Properties properties) {
        super(properties);
    }

    @Override
    public double getCapacity() {
        return CAPACITY;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        var cap = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY, null);
        if (cap.isPresent())
            MoonlightRepair.tryFillWithEmber(cap.resolve().get(), world, entity, AWConfig.MOONSNARE_STRENGTH.get());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new AetherEmberJarItem.EmberJarCapability(stack, getCapacity());
    }
}