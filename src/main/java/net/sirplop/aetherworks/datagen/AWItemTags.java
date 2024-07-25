package net.sirplop.aetherworks.datagen;

import com.rekindled.embers.Embers;
import com.rekindled.embers.datagen.EmbersItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;


public class AWItemTags extends ItemTagsProvider {
    public AWItemTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, Aetherworks.MODID, existingFileHelper);
    }
    public static final TagKey<Item> RAW_AETHERIUM = ItemTags.create(new ResourceLocation("forge", "raw_materials/aetherium"));
    public static final TagKey<Item> AETHERIUM_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/aetherium"));
    public static final TagKey<Item> AETHERIUM_PLATE = ItemTags.create(new ResourceLocation("forge", "plates/aetherium"));
    public static final TagKey<Item> AETHERIUM_ASPECTUS = ItemTags.create(new ResourceLocation(Embers.MODID, "aspectus/aetherium"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RAW_AETHERIUM).add(AWRegistry.AETHER_SHARD.get());
        tag(AETHERIUM_INGOT).add(AWRegistry.INGOT_AETHER.get());
        tag(AETHERIUM_PLATE).add(AWRegistry.PLATE_AETHER.get());
        tag(AETHERIUM_ASPECTUS).add(AWRegistry.AETHER_ASPECTUS.get());
        tag(EmbersItemTags.ASPECTUS).add(AWRegistry.AETHER_ASPECTUS.get());

        tag(ItemTags.PICKAXES).add(AWRegistry.PICKAXE_EMBER.get(), AWRegistry.PICKAXE_AETHER.get());
        tag(ItemTags.AXES).add(AWRegistry.AXE_ENDER.get(), AWRegistry.AXE_SCULK.get());
        tag(ItemTags.SHOVELS).add(AWRegistry.SHOVEL_SLIME.get(), AWRegistry.SHOVEL_PRISMARINE.get());

        tag(EmbersItemTags.NORMAL_WALK_SPEED_TOOL).add(AWRegistry.CROSSBOW_QUARTZ.get(), AWRegistry.CROSSBOW_MAGMA.get());
        tag(EmbersItemTags.AUGMENTABLE_PROJECTILE_WEAPONS).add(AWRegistry.CROSSBOW_QUARTZ.get(), AWRegistry.CROSSBOW_MAGMA.get());

        tag(EmbersItemTags.TINKER_LENS_HELMETS).add(AWRegistry.AETHER_CROWN.get());
        tag(Tags.Items.ARMORS_HELMETS).add(AWRegistry.AETHER_CROWN.get());
    }
}
