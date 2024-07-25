package net.sirplop.aetherworks.api.damage;

import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.entity.EmberProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.sirplop.aetherworks.AWConfig;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.item.AetherCrossbowMagma;
import net.sirplop.aetherworks.util.Utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class EffectDamageCrossbowMagma extends EffectDamagePotion{
    public EffectDamageCrossbowMagma(float damage, float knockback, Function<Entity, DamageSource> source,
                                     int fire, double invinciblityMultiplier, List<MobEffectInstance> effectInstances,
                                     AetherCrossbowMagma sourceItem, int groupID, ItemStack sourceStack) {
        super(damage, knockback, source, fire, invinciblityMultiplier, effectInstances);
        this.sourceItem = sourceItem;
        this.groupID = groupID;
        this.sourceStack = sourceStack;
    }

    AetherCrossbowMagma sourceItem;
    ItemStack sourceStack;
    int groupID;

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Function<Entity, DamageSource> getSource() {
        return this.source;
    }

    public void setSource(Function<Entity, DamageSource> source) {
        this.source = source;
    }

    public int getFire() {
        return this.fire;
    }

    public void setFire(int seconds) {
        this.fire = seconds;
    }

    public double getInvinciblityMultiplier() {
        return this.invinciblityMultiplier;
    }

    public void setInvinciblityMultiplier(double multiplier) {
        this.invinciblityMultiplier = multiplier;
    }

    public void onEntityImpact(Entity entity, @Nullable IProjectilePreset projectile) {
        Entity shooter = projectile != null ? projectile.getShooter() : null;
        Entity projectileEntity = projectile != null ? projectile.getEntity() : null;
        if (entity == projectileEntity || entity == null)
            return;
        if (entity.hurt(this.source.apply(projectileEntity), damage)) {
            entity.setSecondsOnFire(this.fire);
        }
        if (entity instanceof LivingEntity livingTarget) {
            livingTarget.setLastHurtMob(shooter);
            livingTarget.hurtDuration = (int)((double)livingTarget.hurtDuration * this.invinciblityMultiplier);

            if (this.knockback > 0 && projectileEntity != null) {
                double d0 = Math.max(0.0D, 1.0D - livingTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                //because deltaMovement is always (0,0,0), we need to create a vector to send our victim
                Vec3 delta = livingTarget.position().subtract(new Vec3(projectileEntity.xOld, projectileEntity.yOld, projectileEntity.zOld));
                Vec3 vec3 = delta.multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D * d0);
                if (vec3.lengthSqr() > 0.0D) {
                    livingTarget.push(vec3.x, 0.1D, vec3.z);
                }
            }

            if (projectileEntity != null && !projectileEntity.level().isClientSide && shooter instanceof LivingEntity livingShooter) {
                EnchantmentHelper.doPostHurtEffects(livingTarget, shooter);
                EnchantmentHelper.doPostDamageEffects(livingShooter, livingTarget);
            }
            if (livingTarget.isAffectedByPotions()) {
                for(MobEffectInstance mobeffectinstance : effectInstances) {
                    MobEffect mobeffect = mobeffectinstance.getEffect();
                    if (mobeffect.isInstantenous()) {
                        mobeffect.applyInstantenousEffect(projectileEntity, shooter, livingTarget, mobeffectinstance.getAmplifier(), 1);
                    } else {
                        MobEffectInstance mobeffectinstance1 = new MobEffectInstance(mobeffect, mobeffectinstance.getDuration(), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible());
                        livingTarget.addEffect(mobeffectinstance1, entity);
                    }
                }
            }
        }
        //spawn more rays here.
        if (shooter instanceof LivingEntity liveShooter) {
            sourceItem.projectileGroups.get(this.groupID).add(entity.getUUID());
            if (sourceItem.projectileGroups.get(this.groupID).size() > AWConfig.CROSSBOW_MAGMA_CHAIN_LIMIT.get()) {
                return;
            }
            List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(AWConfig.CROSSBOW_MAGMA_CHAIN_RANGE.get()));
            LivingEntity closest = Utils.getClosestEntity(entities, entity.position(), (ent) -> {
                if (ent.equals(shooter) || sourceItem.projectileGroups.get(this.groupID).contains(ent.getUUID()))
                    return false;

                if (!ent.canBeSeenByAnyone())
                    return false;
                if (ent instanceof Mob mob) {
                    return mob.getSensing().hasLineOfSight(entity);
                }
                return true;
            });

            if (closest != null)
                sourceItem.createRay(entity.level(), this, liveShooter, entity.getBoundingBox().getCenter(), closest.getBoundingBox().getCenter(), sourceStack);
        }
    }
}
