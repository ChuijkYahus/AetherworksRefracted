package net.sirplop.aetherworks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.compat.curios.CuriosCompat;

public class AWItemModels extends ItemModelProvider {
    public AWItemModels(PackOutput gen, ExistingFileHelper existingFileHelper) {
        super(gen, Aetherworks.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (AWRegistry.FluidStuff fluid : AWRegistry.fluidList) {
            bucketModel(fluid.FLUID_BUCKET, fluid.FLUID.get());
        }

        itemWithTexture(AWRegistry.AETHER_SHARD, "aether_shard");
        itemWithTexture(AWRegistry.AETHER_AMALGAM, "aether_amalgam");
        itemWithTexture(AWRegistry.AETHER_PEARL, "aether_pearl");
        itemWithTexture(AWRegistry.FOCUS_CRYSTAL, "focus_crystal");
        itemWithTexture(AWRegistry.AETHERIUM_LENS, "aetherium_lens");
        itemWithTexture(AWRegistry.PLATE_AETHER, "plate_aether");
        itemWithTexture(AWRegistry.INGOT_AETHER, "ingot_aether");
        itemWithTexture(AWRegistry.GEM_AETHER, "gem_aether");
        itemWithTexture(AWRegistry.TOOL_ROD_CRUDE, "tool_rod_crude");
        itemWithTexture(AWRegistry.TOOL_ROD, "tool_rod");
        itemWithTexture(AWRegistry.TOOL_ROD_INFUSED, "tool_rod");
        itemWithTexture(AWRegistry.PICKAXE_HEAD_CRUDE, "generated", "tool/head/pickaxe_head_center", "tool/part/pickaxe_head_crude");
        itemWithTexture(AWRegistry.PICKAXE_HEAD, "generated", "tool/head/pickaxe_head_center", "tool/part/pickaxe_head");
        itemWithTexture(AWRegistry.PICKAXE_HEAD_AETHER, "generated", "tool/head/pickaxe_head_center", "tool/head/pickaxe_head_aether");
        itemWithTexture(AWRegistry.PICKAXE_HEAD_EMBER, "generated", "tool/head/pickaxe_head_center", "tool/head/pickaxe_head_ember");
        itemWithTexture(AWRegistry.AXE_HEAD_CRUDE, "generated", "tool/head/axe_head_center", "tool/part/axe_head_crude");
        itemWithTexture(AWRegistry.AXE_HEAD, "generated", "tool/head/axe_head_center", "tool/part/axe_head");
        itemWithTexture(AWRegistry.AXE_HEAD_SCULK, "generated", "tool/head/axe_head_center", "tool/head/axe_head_sculk");
        itemWithTexture(AWRegistry.AXE_HEAD_ENDER, "generated", "tool/head/axe_head_center", "tool/head/axe_head_ender");
        itemWithTexture(AWRegistry.SHOVEL_HEAD_CRUDE, "generated", "tool/head/shovel_head_center", "tool/part/shovel_head_crude");
        itemWithTexture(AWRegistry.SHOVEL_HEAD, "generated", "tool/head/shovel_head_center", "tool/part/shovel_head");
        itemWithTexture(AWRegistry.SHOVEL_HEAD_PRISMARINE, "generated", "tool/head/shovel_head_center", "tool/head/shovel_head_prismarine");
        itemWithTexture(AWRegistry.SHOVEL_HEAD_SLIME, "generated", "tool/head/shovel_head_center", "tool/head/shovel_head_slime");
        itemWithTexture(AWRegistry.HOE_HEAD_CRUDE, "generated", "tool/head/hoe_head_center", "tool/part/hoe_head_crude");
        itemWithTexture(AWRegistry.HOE_HEAD, "generated", "tool/head/hoe_head_center", "tool/part/hoe_head");
        itemWithTexture(AWRegistry.HOE_HEAD_HONEY, "generated", "tool/head/hoe_head_center", "tool/head/hoe_head_honey");
        itemWithTexture(AWRegistry.HOE_HEAD_AMETHYST, "generated", "tool/head/hoe_head_center", "tool/head/hoe_head_amethyst");
        itemWithTexture(AWRegistry.AETHER_CROWN_CRUDE, "aether_crown_crude");
        itemWithTexture(AWRegistry.AETHER_CROWN_MUNDANE, "aether_crown_mundane");
        itemWithTexture(AWRegistry.CROSSBOW_FRAME_CRUDE, "crossbow_frame_crude");
        itemWithTexture(AWRegistry.CROSSBOW_FRAME, "crossbow_frame");
        itemWithTexture(AWRegistry.CROSSBOW_FRAME_INFUSED, "crossbow_frame");
        itemWithTexture(AWRegistry.CROSSBOW_LIMBS_CRUDE, "crossbow_limbs_crude");
        itemWithTexture(AWRegistry.CROSSBOW_LIMBS, "crossbow_limbs");
        itemWithTexture(AWRegistry.CROSSBOW_LIMBS_QUARTZ, "generated", "crossbow_limbs", "crossbow_limbs_quartz");
        itemWithTexture(AWRegistry.CROSSBOW_LIMBS_MAGMA, "generated", "crossbow_limbs", "crossbow_limbs_magma");

        itemWithTexture(AWRegistry.AETHER_ASPECTUS, "aspectus_aetherium");
        itemWithTexture(AWRegistry.AETHERIOMETER, "aetheriometer");

        layeredItem(AWRegistry.AETHER_EMBER_JAR, "item/generated", "aether_ember_jar_glass", "aether_ember_jar_shine", "aether_ember_jar");
        layeredItem(AWRegistry.AETHER_EMBER_CARTRIDGE, "item/generated", "aether_ember_cartridge_glass", "aether_ember_cartridge_shine", "aether_ember_cartridge");
        layeredItem(CuriosCompat.AETHER_EMBER_BULB, "item/generated", "aether_ember_bulb_glass", "aether_ember_bulb_shine", "aether_ember_bulb");

        itemWithTexture(AWRegistry.TUNING_CYLINDER, "tuning_cylinder");
        itemWithTexture(AWRegistry.VOLANT_CALCIFIER, "volant_calcifier");
        itemWithTexture(AWRegistry.AGRARIAN_LINERS, "agrarian_liners");

        itemWithTexture(AWRegistry.GEODE_END, "geode_end");
        itemWithTexture(AWRegistry.GEODE_NETHER, "geode_nether");
        itemWithTexture(AWRegistry.GEODE_HOT, "geode_hot");
        itemWithTexture(AWRegistry.GEODE_COLD, "geode_cold");
        itemWithTexture(AWRegistry.GEODE_OCEAN, "geode_ocean");
        itemWithTexture(AWRegistry.GEODE_MAGIC, "geode_magic");
        itemWithTexture(AWRegistry.GEODE_DEEP, "geode_deep");
        itemWithTexture(AWRegistry.GEODE_BASIC, "geode_basic");

        toolWithTexture(AWRegistry.PICKAXE_EMBER, "tool/tool_base", "tool/pickaxe_ember");
        toolWithTexture(AWRegistry.PICKAXE_AETHER, "tool/tool_base", "tool/pickaxe_aether");
        toolWithTexture(AWRegistry.AXE_ENDER, "tool/tool_base", "tool/axe_ender");
        toolWithTexture(AWRegistry.AXE_SCULK, "tool/tool_base", "tool/axe_sculk");
        toolWithTexture(AWRegistry.SHOVEL_SLIME, "tool/tool_base_shovel", "tool/shovel_slime");
        toolWithTexture(AWRegistry.SHOVEL_PRISMARINE, "tool/tool_base_shovel", "tool/shovel_prismarine");
        toolWithTexture(AWRegistry.HOE_HONEY, "tool/tool_base", "tool/hoe_honey");
        toolWithTexture(AWRegistry.HOE_AMETHYST, "tool/tool_base", "tool/hoe_amethyst");

        itemWithTexture(AWRegistry.AETHER_CROWN, "generated", "aether_crown", "aether_crown_overlay");
        //potion gem now in own model file because overrides
        //itemWithTexture(AWRegistry.POTION_GEM, "item/generated","potion_gem_overlay", "potion_gem");
    }

    public void itemWithModel(RegistryObject<? extends Item> registryObject, String model) {
        ResourceLocation id = registryObject.getId();
        ResourceLocation textureLocation = new ResourceLocation(id.getNamespace(), "item/" + id.getPath());
        singleTexture(id.getPath(), new ResourceLocation(model), "layer0", textureLocation);
    }

    public void itemWithTexture(RegistryObject<? extends Item> registryObject, String texture) {
        itemWithTexture(registryObject, "item/generated", texture);
    }
    public void itemWithTexture(RegistryObject<? extends Item> registryObject, String model, String... textures) {
        ResourceLocation id = registryObject.getId();
        for (int i = 0; i < textures.length; i++) {
            ResourceLocation textureLocation = new ResourceLocation(id.getNamespace(), "item/" + textures[i]);
            singleTexture(id.getPath(), new ResourceLocation(model), "layer"+i, textureLocation);
        }
    }
    public void toolWithTexture(RegistryObject<? extends Item> registryObject, String... textures) {
        itemWithTexture(registryObject, "item/handheld", textures);
    }
    public void bucketModel(RegistryObject<? extends BucketItem> registryObject, Fluid fluid) {
        ModelBuilder<ItemModelBuilder> builder = withExistingParent(registryObject.getId().getPath(), new ResourceLocation(Aetherworks.MODID, "item/bucket_fluid"));
        builder.customLoader(DynamicFluidContainerModelBuilder::begin).fluid(fluid).coverIsMask(false).flipGas(true).end();
    }
    public void layeredItem(RegistryObject<? extends Item> registryObject, String model, String... textures) {
        ResourceLocation id = registryObject.getId();

        ModelBuilder<?> builder = withExistingParent(id.getPath(), new ResourceLocation(model));
        for (int i = 0; i < textures.length; i ++) {
            builder.texture("layer" + i, new ResourceLocation(id.getNamespace(), "item/" + textures[i]));
        }
    }
}
