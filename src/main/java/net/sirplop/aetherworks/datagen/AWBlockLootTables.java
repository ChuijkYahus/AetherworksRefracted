package net.sirplop.aetherworks.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AWBlockLootTables extends BlockLootSubProvider {

    public AWBlockLootTables() {
        super(Set.of(), FeatureFlags.VANILLA_SET);
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter((block) -> Aetherworks.MODID.equals(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getNamespace()))
                .collect(Collectors.toList());
    }

    @Override
    protected void generate() {
        add(AWRegistry.AETHERIUM_ORE.get(), (block) -> {
            return createOreDrop(block, AWRegistry.AETHER_SHARD.get());
        });

        dropSelf(AWRegistry.AETHERIUM_BLOCK.get());
        dropSelf(AWRegistry.PRISM_SUPPORT.get());
        dropSelf(AWRegistry.PRISM.get());
        dropSelf(AWRegistry.MOONLIGHT_AMPLIFIER.get());
        dropSelf(AWRegistry.CONTROL_MATRIX.get());
    }
}
