package net.sirplop.aetherworks.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.block.MoonlightAmplifier;
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
        blockWithItem(AWRegistry.AETHERIUM_ORE, "ore_aether");
        blockWithItem(AWRegistry.AETHERIUM_BLOCK);
        blockWithItem(AWRegistry.PRISM_SUPPORT, "prism_support");
        blockWithItem(AWRegistry.PRISM, "prism");

        ModelFile.ExistingModelFile moonlightAmplifier = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "moonlight_amplifier"));
        horizontalBlock(AWRegistry.MOONLIGHT_AMPLIFIER.get(), moonlightAmplifier);
        //simpleBlockItem(AWRegistry.MOONLIGHT_AMPLIFIER.get(), moonlightAmplifier);
        blockItemWithAdjustment(AWRegistry.MOONLIGHT_AMPLIFIER, moonlightAmplifier,
                new Vector3f(30f, 130f, 0f), new Vector3f(0, 0f, 0),.6f);

        ModelFile.ExistingModelFile controlMatrix = models().getExistingFile(new ResourceLocation(Aetherworks.MODID, "aether_prism_control_matrix"));
        simpleBlock(AWRegistry.CONTROL_MATRIX.get(), controlMatrix);
        blockItemWithAdjustment(AWRegistry.CONTROL_MATRIX, controlMatrix,
                new Vector3f(30f, 40f, 0f), new Vector3f(0, -0.15f, 0),.6f);
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
}
