package net.sirplop.aetherworks.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Config;
import net.sirplop.aetherworks.util.AetheriumTiers;
import net.sirplop.aetherworks.util.Utils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EmberPickaxe  extends AOEEmberDiggerItem{
    public EmberPickaxe(Properties properties) {
        super(1, -2.8f, AetheriumTiers.AETHERIUM, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public void aoeMineTrigger(ItemStack stack, BlockPos pos, Player player) {
        if (!Utils.isFakePlayer(player) && !player.level().isClientSide()) {
            spawnGeode(stack, player.level(), pos, player);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        if (!Utils.isFakePlayer(player) && !player.level().isClientSide()) {
            spawnGeode(stack, player.level(), pos, player);
        }
        return super.onBlockStartBreak(stack, pos, player);
    }
    private final static Vector3f particleColor = Utils.colorIntToVector(255, 64, 16);
    @Override
    public Vector3f getParticleColor() {
        return particleColor;
    }

    private void spawnGeode(ItemStack stack, Level level, BlockPos pos, Player player){
        if (level.random.nextInt(Config.emberPickChance) != 0)
            return;
        if (!level.getBlockState(pos).getTags().anyMatch(
                blockTagKey -> blockTagKey == BlockTags.MINEABLE_WITH_PICKAXE))
            return;

        Biome currentBiome = level.getBiome(pos).get();
        boolean isEnd = tagHasBiome(currentBiome, BiomeTags.IS_END);
        boolean isNether = tagHasBiome(currentBiome, BiomeTags.IS_NETHER);
        boolean isOcean = tagHasBiome(currentBiome, BiomeTags.IS_OCEAN);
        boolean isHot = tagHasBiome(currentBiome, Tags.Biomes.IS_HOT);
        boolean isCold = tagHasBiome(currentBiome, Tags.Biomes.IS_COLD);
        boolean isMagic = tagHasBiome(currentBiome, Tags.Biomes.IS_MAGICAL);
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

    private boolean tagHasBiome(Biome biome, TagKey<Biome> tag)
    {
        try
        {
            return ForgeRegistries.BIOMES.tags().getTag(tag).contains(biome);
        } catch (Exception e)
        { //might be an invalid tag.
            return false;
        }
    }
}
