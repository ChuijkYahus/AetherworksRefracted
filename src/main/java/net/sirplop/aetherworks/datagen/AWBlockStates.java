package net.sirplop.aetherworks.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersBlockStates;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.lib.OctDirection;
import net.sirplop.aetherworks.lib.OctFacingHorizontalProperty;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.function.Function;

public class AWBlockStates extends BlockStateProvider {

    public AWBlockStates(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, Aetherworks.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //this is just to give them proper particles
        for (AWRegistry.FluidStuff fluid : AWRegistry.fluidList) {
            fluid(fluid.FLUID_BLOCK, fluid.name);
        }
        blockWithItem(AWRegistry.SUEVITE);
        blockWithItem(AWRegistry.SUEVITE_COBBLE);
        decoBlocks(AWRegistry.SUEVITE_COBBLE_DECO);
        blockWithItem(AWRegistry.SUEVITE_BRICKS);
        decoBlocks(AWRegistry.SUEVITE_BRICKS_DECO);
        blockWithItem(AWRegistry.AETHERIUM_ORE, "ore_aether");
        blockWithItem(AWRegistry.AETHERIUM_SHARD_BLOCK, "block_shards_raw");
        blockWithItem(AWRegistry.AETHERIUM_BLOCK);
        blockWithItem(AWRegistry.PRISM_SUPPORT, "prism_support");
        blockWithItem(AWRegistry.PRISM, "prism");

        ModelFile.ExistingModelFile moonlightAmplifier = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "moonlight_amplifier"));
        horizontalBlock(AWRegistry.MOONLIGHT_AMPLIFIER.get(), moonlightAmplifier);
        blockItemWithAdjustment(AWRegistry.MOONLIGHT_AMPLIFIER, moonlightAmplifier,
                new Vector3f(30f, 130f, 0f), new Vector3f(0, 0f, 0),.6f);

        ModelFile.ExistingModelFile controlMatrix = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "aether_prism_control_matrix"));
        simpleBlock(AWRegistry.CONTROL_MATRIX.get(), controlMatrix);
        blockItemWithAdjustment(AWRegistry.CONTROL_MATRIX, controlMatrix,
                new Vector3f(30f, 40f, 0f), new Vector3f(0, -0.15f, 0),.6f);

        blockWithItem(AWRegistry.FORGE_CORE, "forge_core");
        horizontalblockWithItem(AWRegistry.FORGE_HEATER, "forge_heater");
        horizontalblockWithItem(AWRegistry.FORGE_COOLER, "forge_cooler");
        horizontalblockWithItem(AWRegistry.FORGE_ANVIL, "anvil");
        horizontalblockWithItem(AWRegistry.FORGE_METAL_FORMER, "metal_former");
        horizontalblockWithItem(AWRegistry.FORGE_TOOL_STATION, "tool_station");
        ModelFile.ExistingModelFile forgeVent = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "forge_vent"));
        horizontalBlock(AWRegistry.FORGE_VENT.get(), forgeVent);
        blockItemWithAdjustment(AWRegistry.FORGE_VENT, forgeVent,
               new Vector3f(30f, -40f, 0f), new Vector3f(-3f, -2f, 0),.7f);

        ModelFile.ExistingModelFile forgeCenter = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "forge_center"));
        ModelFile.ExistingModelFile forgeSide = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "forge_side"));
        ModelFile.ExistingModelFile forgeCorner = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "forge_corner"));

        //forge block has no inventory option.
        forgeStructure(AWRegistry.FORGE_BLOCK.get(), state -> {
            OctDirection dir = state.getValue(OctFacingHorizontalProperty.OCT_DIRECTIONS);
            switch (dir) {
                case LEFT, RIGHT, FRONT, BACK -> {
                    return forgeSide;
                }
                case LEFT_FRONT, RIGHT_FRONT, LEFT_BACK, RIGHT_BACK -> {
                    return forgeCorner;
                }
                default -> {
                    return forgeCenter;
                }
            }
        });

        dial(AWRegistry.HEAT_DIAL, "heat_dial");
    }
    public void blockWithItem(RegistryObject<? extends Block> registryObject) {
        //block model
        simpleBlock(registryObject.get());
        //itemblock model
        simpleBlockItem(registryObject.get(), cubeAll(registryObject.get()));
    }
    public void blockWithItem(RegistryObject<? extends Block> registryObject, String model) {
        ModelFile.ExistingModelFile modelFile = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, model));
        //block model
        simpleBlock(registryObject.get(), modelFile);
        //itemblock model
        simpleBlockItem(registryObject.get(), modelFile);
    }

    public void blockWithItemTexture(RegistryObject<? extends Block> registryObject, String texture) {
        ModelFile modelFile = models().cubeAll(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(registryObject.get())).getPath(),
                new ResourceLocation(Aetherworks.MODID, "block/" + texture));
        //block model
        simpleBlock(registryObject.get(), modelFile);
        //itemblock model
        simpleBlockItem(registryObject.get(), modelFile);
    }
    public void horizontalblockWithItem(RegistryObject<? extends Block> registryObject, String model) {
        ModelFile.ExistingModelFile modelFile = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, model));
        //block model
        horizontalBlock(registryObject.get(), modelFile);
        //itemblock model
        simpleBlockItem(registryObject.get(), modelFile);
    }
    public void decoBlocks(AWRegistry.StoneDecoBlocks deco) {
        ResourceLocation resourceLocation = this.blockTexture(deco.block.get());

        if (deco.stairs != null) {
            this.stairsBlock(deco.stairs.get(), resourceLocation);
            this.itemModels().stairs(deco.stairs.getId().getPath(), resourceLocation, resourceLocation, resourceLocation);
        }
        if (deco.slab != null) {
            this.slabBlock(deco.slab.get(), deco.block.getId(), resourceLocation);
            this.itemModels().slab(deco.slab.getId().getPath(), resourceLocation, resourceLocation, resourceLocation);
        }
        if (deco.wall != null) {
            this.wallBlock(deco.wall.get(), resourceLocation);
            this.itemModels().wallInventory(deco.wall.getId().getPath(), resourceLocation);
        }
    }

    public void dial(RegistryObject<? extends Block> registryObject, String texture) {
        //block model
        ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(registryObject.get());
        ModelFile model = models().withExistingParent(loc.toString(), new ResourceLocation(Aetherworks.MODID, "dial"))
                .texture("dial", new ResourceLocation(Aetherworks.MODID, "block/" + texture))
                .texture("particle", new ResourceLocation(Aetherworks.MODID, "block/" + texture));
        directionalBlock(registryObject.get(), model);

        //item model
        flatItem(registryObject, texture);
    }

    public void flatItem(RegistryObject<? extends Block> registryObject, String texture) {
        ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(registryObject.get());
        itemModels().getBuilder(loc.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(loc.getNamespace(), "item/" + texture));
    }

    public void fluid(RegistryObject<? extends Block> fluid, String name) {
        simpleBlock(fluid.get(), models().cubeAll(name, new ResourceLocation(Aetherworks.MODID, ModelProvider.BLOCK_FOLDER + "/fluid/" + name + "_still")));
    }

    public void blockItemWithAdjustment(RegistryObject<? extends Block> registryObject, ModelFile model,
                                        Vector3f rotationInv, Vector3f translationInv, float scaleInv)
    {
        itemModels().getBuilder(ForgeRegistries.BLOCKS.getKey(registryObject.get())
                .getPath()).parent(model)
                .transforms().transform(ItemDisplayContext.GUI)
                .rotation(rotationInv.x, rotationInv.y, rotationInv.z)
                .scale(scaleInv)
                .translation(translationInv.x, translationInv.y, translationInv.z)
                .end();
    }

    private void forgeStructure(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationY(state.getValue(OctFacingHorizontalProperty.OCT_DIRECTIONS).toBlockRot() % 360)
                        .build()
                );
    }
}
