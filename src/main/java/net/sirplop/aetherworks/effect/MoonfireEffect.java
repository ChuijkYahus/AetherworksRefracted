package net.sirplop.aetherworks.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.damage.DamageMoonEmber;
import net.sirplop.aetherworks.datagen.AWDamageTypes;

public class MoonfireEffect extends MobEffect {
    public MoonfireEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            DamageSource source = new DamageMoonEmber(pLivingEntity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get()
                    .getHolderOrThrow(AWDamageTypes.MOON_EMBER_KEY), pLivingEntity, null);
            pLivingEntity.hurt(source, 4.0F);
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int i = 40 >> pAmplifier;
        if (i > 0) {
            return pDuration % i == 0;
        }
        return true;
    }
}
