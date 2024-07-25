package net.sirplop.aetherworks.item;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.event.EmberProjectileEvent;
import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.api.projectile.ProjectileRay;
import com.rekindled.embers.datagen.EmbersSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.api.damage.EffectDamageCrossbowMagma;
import net.sirplop.aetherworks.damage.DamageMoonEmber;
import net.sirplop.aetherworks.datagen.AWDamageTypes;
import net.sirplop.aetherworks.util.Utils;

import java.util.*;

public class AetherCrossbowMagma extends AetherCrossbow{
    public AetherCrossbowMagma(Properties pProperties) {
        super(pProperties);
    }

    public int shotID = 0;
    public final Map<Integer, Set<UUID>> projectileGroups = new HashMap<>();

    @Override
    public void shootProjectile(Level level, LivingEntity entity, ItemStack stack, InteractionHand hand, float pSoundPitch, float rotation) {
        if (level.isClientSide)
            return;

        double posX = entity.getX() + entity.getLookAngle().x;
        double y = entity.getY() + entity.getEyeHeight() - 0.2 + entity.getLookAngle().y;
        double posZ = entity.getZ() + entity.getLookAngle().z;

        double delta = Math.toRadians(rotation); //handle multishot
        double x = posX * Math.cos(delta) - posZ * Math.sin(delta);
        double z = posZ * Math.sin(delta) + posZ * Math.cos(delta);

        double targX = entity.getX() + entity.getLookAngle().x * ConfigManager.BLAZING_RAY_MAX_DISTANCE.get();
        double targY = entity.getY() + 2 + entity.getLookAngle().y * ConfigManager.BLAZING_RAY_MAX_DISTANCE.get();
        double targZ = entity.getZ() + entity.getLookAngle().z * ConfigManager.BLAZING_RAY_MAX_DISTANCE.get();

        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, entity);
        int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, entity);
        int fire = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, entity) > 0 ? 100 : 0;

        float damage = ConfigManager.BLAZING_RAY_DAMAGE.get().floatValue() + (powerLevel * 0.5f);

        createRay(level, damage, knockback, fire, entity, new Vec3(x, y, z), new Vec3(targX, targY, targZ), stack, getNextID());

        level.playSound(null, entity, EmbersSounds.BLAZING_RAY_FIRE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);

        if (entity instanceof Player player && player.getAbilities().instabuild)
            return;
        stack.hurtAndBreak(1, entity, (ent) -> {
            ent.broadcastBreakEvent(hand);
        });
    }

    public void createRay(Level level, EffectDamageCrossbowMagma effect, LivingEntity shooter, Vec3 start, Vec3 targetPos, ItemStack stack) {
        ProjectileRay ray = new ProjectileRay(shooter, start, targetPos, false, effect);
        ray.setColor(Utils.AETHERIUM_PROJECTILE_COLOR);

        EmberProjectileEvent event = new EmberProjectileEvent(shooter, stack, 1, ray);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            for (IProjectilePreset projectile : event.getProjectiles()) {
                projectile.shoot(level);
            }
        }
    }

    public void createRay(Level level, float damage, float knockback, int fire, LivingEntity shooter, Vec3 start, Vec3 targetPos, ItemStack stack, int id) {
        DamageSource dam = new DamageMoonEmber(level.registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(AWDamageTypes.MOON_EMBER_KEY), shooter, true);
        EffectDamageCrossbowMagma effect = new EffectDamageCrossbowMagma(damage, knockback, e -> dam, fire, 1.0f,
                List.of(new MobEffectInstance(AWRegistry.EFFECT_MOONFIRE.get(), 200, 0, false, true, true)),
                this, id, stack);
        createRay(level, effect, shooter, start, targetPos, stack);
    }

    public int getNextID() {
        int currentID = shotID;

        if (!projectileGroups.containsKey(currentID))
            projectileGroups.put(currentID, new HashSet<>());
        projectileGroups.get(currentID).clear();

        if (++shotID >= 8)
            shotID = 0; //limit it to 8 groups

        return currentID;
    }
}
