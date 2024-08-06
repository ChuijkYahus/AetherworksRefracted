package net.sirplop.aetherworks.datagen;

import com.rekindled.embers.RegistryManager;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
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
                .filter((block) -> Aetherworks.MODID.equals(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace()))
                .collect(Collectors.toList());
    }

    @Override
    protected void generate() {
        add(AWRegistry.AETHERIUM_ORE.get(), (block) -> createAetherOreDrops(block, AWRegistry.AETHER_SHARD.get()));

        dropOther(AWRegistry.SUEVITE.get(), AWRegistry.SUEVITE_COBBLE.get());
        dropSelf(AWRegistry.SUEVITE_COBBLE.get());
        decoDrops(AWRegistry.SUEVITE_COBBLE_DECO);
        dropSelf(AWRegistry.SUEVITE_BRICKS.get());
        decoDrops(AWRegistry.SUEVITE_BRICKS_DECO);
        dropSelf(AWRegistry.GLASS_AETHERIUM.get());

        dropSelf(AWRegistry.AETHERIUM_SHARD_BLOCK.get());
        dropSelf(AWRegistry.AETHERIUM_BLOCK.get());
        dropSelf(AWRegistry.PRISM_SUPPORT.get());
        dropSelf(AWRegistry.PRISM.get());
        dropSelf(AWRegistry.MOONLIGHT_AMPLIFIER.get());
        dropSelf(AWRegistry.CONTROL_MATRIX.get());
        dropSelf(AWRegistry.FORGE_CORE.get());
        dropSelf(AWRegistry.FORGE_COOLER.get());
        dropSelf(AWRegistry.FORGE_HEATER.get());
        dropSelf(AWRegistry.FORGE_VENT.get());
        dropSelf(AWRegistry.FORGE_ANVIL.get());
        dropSelf(AWRegistry.FORGE_METAL_FORMER.get());
        dropSelf(AWRegistry.FORGE_TOOL_STATION.get());
        dropOther(AWRegistry.FORGE_BLOCK.get(), RegistryManager.DAWNSTONE_BLOCK_ITEM.get());
        dropSelf(AWRegistry.HEAT_DIAL.get());
    }

    protected LootTable.Builder createAetherOreDrops(Block pBlock, ItemLike item) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    public void decoDrops(AWRegistry.StoneDecoBlocks deco) {
        if (deco.stairs != null)
            dropSelf(deco.stairs.get());
        if (deco.slab != null)
            add(deco.slab.get(), this::createSlabItemTable);
        if (deco.wall != null)
            dropSelf(deco.wall.get());
    }
}
