package net.sirplop.aetherworks.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AWBlockTags  extends BlockTagsProvider {
    public AWBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Aetherworks.MODID, existingFileHelper);
    }
    public static final TagKey<Block> NEEDS_AETHERIUM_TOOL = BlockTags.create(new ResourceLocation(Aetherworks.MODID, "needs_aetherium_tool"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        decoTags(AWRegistry.SUEVITE_COBBLE_DECO);
        decoTags(AWRegistry.SUEVITE_BRICKS_DECO);

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                AWRegistry.AETHERIUM_ORE.get(),
                AWRegistry.AETHERIUM_SHARD_BLOCK.get(),
                AWRegistry.AETHERIUM_BLOCK.get(),
                AWRegistry.PRISM_SUPPORT.get(),
                AWRegistry.PRISM.get(),
                AWRegistry.MOONLIGHT_AMPLIFIER.get(),
                AWRegistry.CONTROL_MATRIX.get(),
                AWRegistry.FORGE_BLOCK.get(),
                AWRegistry.FORGE_CORE.get(),
                AWRegistry.FORGE_HEATER.get(),
                AWRegistry.FORGE_COOLER.get(),
                AWRegistry.FORGE_VENT.get(),
                AWRegistry.FORGE_ANVIL.get(),
                AWRegistry.FORGE_METAL_FORMER.get(),
                AWRegistry.FORGE_TOOL_STATION.get(),
                AWRegistry.SUEVITE.get(),
                AWRegistry.SUEVITE_COBBLE.get(),
                AWRegistry.SUEVITE_BRICKS.get()
        );
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                AWRegistry.AETHERIUM_ORE.get(),
                AWRegistry.AETHERIUM_SHARD_BLOCK.get(),
                AWRegistry.AETHERIUM_BLOCK.get()
        );
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                AWRegistry.PRISM_SUPPORT.get(),
                AWRegistry.PRISM.get(),
                AWRegistry.MOONLIGHT_AMPLIFIER.get(),
                AWRegistry.CONTROL_MATRIX.get(),
                AWRegistry.FORGE_BLOCK.get(),
                AWRegistry.FORGE_CORE.get(),
                AWRegistry.FORGE_HEATER.get(),
                AWRegistry.FORGE_COOLER.get(),
                AWRegistry.FORGE_VENT.get(),
                AWRegistry.FORGE_ANVIL.get(),
                AWRegistry.FORGE_METAL_FORMER.get(),
                AWRegistry.FORGE_TOOL_STATION.get()
        );

        //individual tags
        getTag("forge:cobblestone").add(AWRegistry.SUEVITE_COBBLE.get());
        getTag("minecraft:sculk_replaceable").add(AWRegistry.SUEVITE.get());
        getTag("forge:stone").add(AWRegistry.SUEVITE.get());
        getTag("minecraft:snaps_goat_horn").add(AWRegistry.SUEVITE.get());
        getTag("minecraft:moss_replaceable").add(AWRegistry.SUEVITE.get());

        getTag("forge:ores").add(AWRegistry.AETHERIUM_ORE.get());
    }

    public IntrinsicTagAppender<Block> getTag(String name) {
        return tag(BlockTags.create(new ResourceLocation(name)));
    }


    public void decoTags(AWRegistry.StoneDecoBlocks deco) {
        if (deco.stairs != null) {
            tag(BlockTags.STAIRS).add(deco.stairs.get());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(deco.stairs.get());
        }
        if (deco.slab != null) {
            tag(BlockTags.SLABS).add(deco.slab.get());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(deco.slab.get());
        }
        if (deco.wall != null) {
            tag(BlockTags.WALLS).add(deco.wall.get());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(deco.wall.get());
        }
    }
}
