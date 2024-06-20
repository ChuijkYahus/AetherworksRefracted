package net.sirplop.aetherworks;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sirplop.aetherworks.blockentity.render.RenderPrism;
import net.sirplop.aetherworks.datagen.*;
import net.sirplop.aetherworks.lib.AWHarvestHelper;
import net.sirplop.aetherworks.network.PacketHandler;
import org.slf4j.Logger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.CompletableFuture;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Aetherworks.MODID)
public class Aetherworks
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "aetherworks";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Aetherworks()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);

        AWRegistry.BLOCKS.register(modEventBus);
        AWRegistry.ITEMS.register(modEventBus);
        AWRegistry.FLUIDTYPES.register(modEventBus);
        AWRegistry.FLUIDS.register(modEventBus);
        AWRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        AWRegistry.CREATIVE_MODE_TAB.register(modEventBus);
        //AWRegistry.ENCHANTMENTS.register(modEventBus); //we're ignoring Aetheric for now - aetherium items will just self-repair.
        //AWRegistry.PARTICLE_TYPES.register(modEventBus);
        //AWRegistry.SOUND_EVENTS.register(modEventBus);
        //AWRegistry.RECIPE_TYPES.register(modEventBus);
        //AWRegistry.RECIPE_SERIALIZERS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        PacketHandler.init();
        AWRegistry.init(event);

        MinecraftForge.EVENT_BUS.addListener(AWHarvestHelper::onServerTick);
        MinecraftForge.EVENT_BUS.addListener(AWHarvestHelper::onLevelUnload);
        MinecraftForge.EVENT_BUS.addListener(AWHarvestHelper::onPlayerLeave);
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeClient()) {
            gen.addProvider(true, new AWItemModels(output, existingFileHelper));
            gen.addProvider(true, new AWBlockStates(output, existingFileHelper));
            //gen.addProvider(true, new EmbersSounds(output, existingFileHelper));
        } if (event.includeServer()) {
            gen.addProvider(true, new AWLootTables(output));
            //gen.addProvider(true, new EmbersRecipes(output));
            BlockTagsProvider blockTags = new AWBlockTags(output, lookupProvider, existingFileHelper);
            gen.addProvider(true, blockTags);
            gen.addProvider(true, new AWItemTags(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
            //gen.addProvider(true, new EmbersFluidTags(output, lookupProvider, existingFileHelper));
            /*gen.addProvider(true, new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder()
                    .add(Registries.CONFIGURED_FEATURE, bootstrap -> EmbersConfiguredFeatures.generate(bootstrap)) //it doesn't like this one for some reason
                    .add(Registries.PLACED_FEATURE, EmbersPlacedFeatures::generate)
                    .add(ForgeRegistries.Keys.BIOME_MODIFIERS, EmbersBiomeModifiers::generate)
                    .add(Registries.DAMAGE_TYPE, EmbersDamageTypes::generate)
                    .add(Registries.PROCESSOR_LIST, EmbersStructures::generateProcessors)
                    .add(Registries.TEMPLATE_POOL, EmbersStructures::generatePools)
                    .add(Registries.STRUCTURE, EmbersStructures::generateStructures)
                    .add(Registries.STRUCTURE_SET, EmbersStructures::generateSets),
                    Set.of(MODID)));
            */
            //gen.addProvider(true, new EmbersDamageTypeTags(output, lookupProvider, existingFileHelper));
            //gen.addProvider(true, new EmbersLootModifiers(output));
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(AWRegistry.AETHERIUM_GAS.FLUID.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(AWRegistry.AETHERIUM_GAS.FLUID_FLOW.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(AWRegistry.AETHERIUM_GAS_IMPURE.FLUID.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(AWRegistry.AETHERIUM_GAS_IMPURE.FLUID_FLOW.get(), RenderType.translucent());
            });
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(AWRegistry.PRISM_BLOCK_ENTITY.get(), RenderPrism::new);
        }
    }
}
