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
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                AWRegistry.AETHERIUM_ORE.get(),
                AWRegistry.AETHERIUM_BLOCK.get(),
                AWRegistry.PRISM_SUPPORT.get(),
                AWRegistry.PRISM.get(),
                AWRegistry.MOONLIGHT_AMPLIFIER.get(),
                AWRegistry.CONTROL_MATRIX.get()
        );
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                AWRegistry.AETHERIUM_ORE.get(),
                AWRegistry.AETHERIUM_BLOCK.get()
        );
    }
}
