package net.sirplop.aetherworks.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MoonlightRepair {

    public static void tryRepair(ItemStack stack, Level level, Entity entity, int effectLevel) {
        if (!level.isClientSide() && stack.getDamageValue() > 0
                && level.getDayTime() >= 15000 && level.getDayTime() <= 21000
                && level.canSeeSky(entity.getOnPos().above(2))
                && level.random.nextFloat() <= 0.025f * effectLevel)
        {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }
}
