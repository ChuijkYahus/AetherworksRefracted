package net.sirplop.aetherworks.augment;

import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.augment.AugmentBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.AWConfig;
import net.sirplop.aetherworks.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TuningCylinderAugment extends AugmentBase {
    public TuningCylinderAugment(ResourceLocation name) {
        super(name, 5.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.isCreative())
            return; //creative players don't drop anything!

        Level world = (Level)event.getLevel();
        ItemStack heldStack = player.getMainHandItem();
        BlockPos pos = event.getPos();
        if (AugmentUtil.hasHeat(heldStack)) {
            int level = AugmentUtil.getAugmentLevel(heldStack, this);
            if (!world.isClientSide() && level > 0 && world.getBlockState(pos).getTags().anyMatch(
                    blockTagKey -> blockTagKey == BlockTags.MINEABLE_WITH_PICKAXE)) {
                double resonance = EmbersAPI.getEmberResonance(heldStack);
                if (world.random.nextInt(getChance(level, resonance)) == 0)
                    spawnGeode(world, pos);
            }
        }
    }

    private int getChance(int level, double resonance) {
        double bonus = Math.max(1, 1 + (resonance - 1) * 0.5);
        return Math.max(1, (int)Math.floor(AWConfig.AUGMENT_TUNING_CYLINDER_CHANCE.get() / (level * bonus)));
    }

    private void spawnGeode(Level level, BlockPos pos) {
        Holder<Biome> currentBiome = level.getBiome(pos);
        boolean isEnd = currentBiome.is(BiomeTags.IS_END);
        boolean isNether = currentBiome.is(BiomeTags.IS_NETHER);
        boolean isOcean = currentBiome.is(BiomeTags.IS_OCEAN);
        boolean isHot = currentBiome.is(Tags.Biomes.IS_HOT);
        boolean isCold = currentBiome.is(Tags.Biomes.IS_COLD);
        boolean isMagic = currentBiome.is(Tags.Biomes.IS_MAGICAL);
        boolean isDeep = pos.getY() < 0;
        //EITHER end OR nether OR (Hot Cold Magic Deep) OR Deep OR Basic

        ItemStack geode;
        if (isEnd){
            geode = new ItemStack(AWRegistry.GEODE_END.get(), 1);
        } else if (isNether) {
            geode = new ItemStack(AWRegistry.GEODE_NETHER.get(), 1);
        } else if (isOcean || isHot || isCold || isMagic) {
            List<Item> options = new ArrayList<>();
            if (isHot) options.add(AWRegistry.GEODE_HOT.get());
            if (isCold) options.add(AWRegistry.GEODE_COLD.get());
            if (isOcean) options.add(AWRegistry.GEODE_OCEAN.get());
            if (isMagic) options.add(AWRegistry.GEODE_MAGIC.get());
            geode = new ItemStack(isDeep && level.random.nextFloat() < 0.5f ?
                    AWRegistry.GEODE_DEEP.get() : options.get(level.random.nextInt(options.size())), 1);
        } else if (isDeep) {
            geode = new ItemStack(AWRegistry.GEODE_DEEP.get(), 1);
        } else {
            geode = new ItemStack(AWRegistry.GEODE_BASIC.get(), 1);
        }

        Utils.dropItemIntoWorld(level, pos, geode);
    }
}
