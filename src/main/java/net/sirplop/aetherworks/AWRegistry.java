package net.sirplop.aetherworks;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.sirplop.aetherworks.block.AetherBlock;
import net.sirplop.aetherworks.block.AetherOre;
import net.sirplop.aetherworks.block.MoonlightAmplifier;
import net.sirplop.aetherworks.block.Prism;
import net.sirplop.aetherworks.blockentity.PrismBlockEntity;
import net.sirplop.aetherworks.enchantment.AethericEnchantment;
import net.sirplop.aetherworks.fluid.GasFluidType;
import net.sirplop.aetherworks.fluid.AetherworksFluidType;
import net.sirplop.aetherworks.item.AetherPickaxe;
import net.sirplop.aetherworks.item.EmberPickaxe;
import org.jetbrains.annotations.NotNull;

public class AWRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Aetherworks.MODID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Aetherworks.MODID);
    public static final DeferredRegister<FluidType> FLUIDTYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Aetherworks.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aetherworks.MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Aetherworks.MODID);

    public static List<FluidStuff> fluidList = new ArrayList<FluidStuff>();

    public static FluidStuff addFluid(AetherworksFluidType.FluidInfo info,
                                      BiFunction<FluidType.Properties, AetherworksFluidType.FluidInfo, FluidType> type,
                                      BiFunction<Supplier<? extends FlowingFluid>, BlockBehaviour.Properties, LiquidBlock> block,
                                      Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Source> source,
                                      Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Flowing> flowing,
                                      @Nullable Consumer<ForgeFlowingFluid.Properties> fluidProperties, FluidType.Properties prop) {
        FluidStuff fluid = new FluidStuff(info.name, info.color, type.apply(prop, info), block, fluidProperties, source, flowing);
        fluidList.add(fluid);
        return fluid;
    }

    public static FluidStuff addFluid(AetherworksFluidType.FluidInfo info,
                                      BiFunction<FluidType.Properties, AetherworksFluidType.FluidInfo, FluidType> type,
                                      BiFunction<Supplier<? extends FlowingFluid>, BlockBehaviour.Properties, LiquidBlock> block,
                                      @Nullable Consumer<ForgeFlowingFluid.Properties> fluidProperties, FluidType.Properties prop) {
        return addFluid(info, type, block, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, fluidProperties, prop);
    }



    //Items
    public static final RegistryObject<Item> AETHER_SHARD = ITEMS.register("aether_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FOCUS_CRYSTAL = ITEMS.register("focus_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHERIUM_LENS = ITEMS.register("aetherium_lens", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATE_AETHER = ITEMS.register("plate_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INGOT_AETHER = ITEMS.register("ingot_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEM_AETHER = ITEMS.register("gem_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOOL_ROD_CRUDE = ITEMS.register("tool_rod_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOOL_ROD = ITEMS.register("tool_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOOL_ROD_INFUSED = ITEMS.register("tool_rod_infused", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> PICKAXE_HEAD_CRUDE = ITEMS.register("pickaxe_head_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PICKAXE_HEAD = ITEMS.register("pickaxe_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PICKAXE_HEAD_AETHER = ITEMS.register("pickaxe_head_aether", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> PICKAXE_HEAD_EMBER = ITEMS.register("pickaxe_head_ember", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> AXE_HEAD_CRUDE = ITEMS.register("axe_head_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AXE_HEAD = ITEMS.register("axe_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AXE_HEAD_PRISMARINE = ITEMS.register("axe_head_prismarine", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> AXE_HEAD_ENDER = ITEMS.register("axe_head_ender", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> SHOVEL_HEAD_CRUDE = ITEMS.register("shovel_head_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHOVEL_HEAD = ITEMS.register("shovel_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHOVEL_HEAD_REDSTONE = ITEMS.register("shovel_head_redstone", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> SHOVEL_HEAD_SLIME = ITEMS.register("shovel_head_slime", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> AETHER_CROWN_CRUDE = ITEMS.register("aether_crown_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHER_CROWN_MUNDANE = ITEMS.register("aether_crown_mundane", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_FRAME_CRUDE = ITEMS.register("crossbow_frame_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_FRAME = ITEMS.register("crossbow_frame", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_FRAME_INFUSED = ITEMS.register("crossbow_frame_infused", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_LIMBS_CRUDE = ITEMS.register("crossbow_limbs_crude", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_LIMBS = ITEMS.register("crossbow_limbs", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_LIMBS_QUARTZ = ITEMS.register("crossbow_limbs_quartz", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_LIMBS_MAGMA = ITEMS.register("crossbow_limbs_magma", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_END = ITEMS.register("geode_end", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_NETHER = ITEMS.register("geode_nether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_HOT = ITEMS.register("geode_hot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_COLD = ITEMS.register("geode_cold", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_MAGIC = ITEMS.register("geode_magical", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_OCEAN = ITEMS.register("geode_ocean", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_DEEP = ITEMS.register("geode_deep", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEODE_BASIC = ITEMS.register("geode_basic", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PICKAXE_EMBER = ITEMS.register("pickaxe_ember", () -> new EmberPickaxe(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PICKAXE_AETHER = ITEMS.register("pickaxe_aether", () -> new AetherPickaxe(new Item.Properties().rarity(Rarity.RARE)));

    //Blocks
    public static final RegistryObject<Block> AETHERIUM_ORE = registerBlock("ore_aether", () -> new AetherOre(BlockBehaviour.Properties.copy(Blocks.STONE).requiresCorrectToolForDrops().strength(5, 12), UniformInt.of(4, 8)));
    public static final RegistryObject<Block> AETHERIUM_BLOCK = registerBlock("block_aether", () -> new AetherBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(10, 40)));
    public static final RegistryObject<Block> PRISM_SUPPORT = registerBlock("prism_support", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(3, 6).requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<Block> PRISM = registerBlock("prism", () -> new Prism(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(4, 8).requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<Block> MOONLIGHT_AMPLIFIER = registerBlock("moonlight_amplifier", () -> new MoonlightAmplifier(BlockBehaviour.Properties.copy(Blocks.STONE).requiresCorrectToolForDrops().noOcclusion().strength(3, 6).sound(SoundType.GLASS)));
    public static final RegistryObject<Block> CONTROL_MATRIX = registerBlock("aether_prism_controller_matrix", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(3, 6).requiresCorrectToolForDrops().noOcclusion()));


    //Fluids
    public static final FluidStuff AETHERIUM_GAS_IMPURE = addFluid(new AetherworksFluidType.FluidInfo("aether_gas_impure", 0xff6c829f, 0.1F, 1.5F),
            GasFluidType::new, LiquidBlock::new,
            prop -> prop.explosionResistance(1000F).tickRate(3),
            FluidType.Properties.create()
                    .canSwim(false)
                    .canDrown(true)
                    .pathType(BlockPathTypes.WATER)
                    .adjacentPathType(null)
                    .motionScale(0.05D)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .density(3000)
                    .viscosity(20)
                    .temperature(1020)
                    .lightLevel(6));
    public static final FluidStuff AETHERIUM_GAS = addFluid(new AetherworksFluidType.FluidInfo("aether_gas", 0xff00b8ff, 0.1F, 1.5F),
            GasFluidType::new, LiquidBlock::new,
            prop -> prop.explosionResistance(1000F).tickRate(3),
            FluidType.Properties.create()
                    .canSwim(false)
                    .canDrown(true)
                    .pathType(BlockPathTypes.WATER)
                    .adjacentPathType(null)
                    .motionScale(0.0005D)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .density(-1000)
                    .viscosity(10)
                    .temperature(1020)
                    .lightLevel(10));

    //Block Entities
    public static final RegistryObject<BlockEntityType<PrismBlockEntity>> PRISM_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("prism_tile", () -> BlockEntityType.Builder.of(PrismBlockEntity::new, PRISM.get()).build(null));

    //Creative Tabs
    public static final RegistryObject<CreativeModeTab> AW_TAB = CREATIVE_MODE_TAB.register("aetherworks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AWRegistry.AETHER_SHARD.get()))
                    .title(Component.translatable("itemgroup." + Aetherworks.MODID))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((params, output) -> {
                        for (RegistryObject<Item> item : ITEMS.getEntries()) {
                            output.accept(item.get());
                        }
                        //output.accept(AWRegistry.AETHER_SHARD.get());
                    })
                    .build());

    //Enchantments
    public static final RegistryObject<Enchantment> AETHERIC_ENCHANTMENT =
            ENCHANTMENTS.register("aetheric", () -> new AethericEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    //Entities

    //Spawn Eggs

    //Particle Types

    //Recipe Types

    //Recipe Serializers

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenseItemBehavior dispenseBucket = new DefaultDispenseItemBehavior() {
                private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

                @Override
                public @NotNull ItemStack execute(BlockSource source, ItemStack stack) {
                    DispensibleContainerItem container = (DispensibleContainerItem)stack.getItem();
                    BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                    Level level = source.getLevel();
                    if (container.emptyContents(null, level, blockpos, null)) {
                        container.checkExtraContent(null, level, stack, blockpos);
                        return new ItemStack(Items.BUCKET);
                    } else {
                        return this.defaultDispenseItemBehavior.dispense(source, stack);
                    }
                }
            };
            for (FluidStuff fluid : fluidList) {
                DispenserBlock.registerBehavior(fluid.FLUID_BUCKET.get(), dispenseBucket);
            }
        });
    }

    public static <T extends ParticleOptions> RegistryObject<ParticleType<T>> registerParticle(String name, boolean overrideLimiter, ParticleOptions.Deserializer<T> deserializer, Codec<T> codec) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter, deserializer) {
            public @NotNull Codec<T> codec() {
                return codec;
            }
        });
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> ret = BLOCKS.register(name, block);
        registerBlockItem(name, ret);
        return ret;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
    {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static class FluidStuff {

        public final ForgeFlowingFluid.Properties PROPERTIES;

        public final RegistryObject<ForgeFlowingFluid.Source> FLUID;
        public final RegistryObject<ForgeFlowingFluid.Flowing> FLUID_FLOW;
        public final RegistryObject<FluidType> TYPE;

        public final RegistryObject<LiquidBlock> FLUID_BLOCK;

        public final RegistryObject<BucketItem> FLUID_BUCKET;

        public final String name;
        public final int color;

        public FluidStuff(String name, int color, FluidType type,
                          BiFunction<Supplier<? extends FlowingFluid>, BlockBehaviour.Properties, LiquidBlock> block,
                          @Nullable Consumer<ForgeFlowingFluid.Properties> fluidProperties,
                          Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Source> source,
                          Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Flowing> flowing) {
            this.name = name;
            this.color = color;

            FLUID = FLUIDS.register(name, () -> source.apply(getFluidProperties()));
            FLUID_FLOW = FLUIDS.register("flowing_" + name, () -> flowing.apply(getFluidProperties()));
            TYPE = FLUIDTYPES.register(name, () -> type);

            PROPERTIES = new ForgeFlowingFluid.Properties(TYPE, FLUID, FLUID_FLOW);
            if (fluidProperties != null)
                fluidProperties.accept(PROPERTIES);

            FLUID_BLOCK = BLOCKS.register(name + "_block", () -> block.apply(FLUID, Block.Properties.of().liquid().pushReaction(PushReaction.DESTROY).lightLevel((state) -> { return type.getLightLevel(); }).randomTicks().replaceable().strength(100.0F).noLootTable()));
            FLUID_BUCKET = ITEMS.register(name + "_bucket", () -> new BucketItem(FLUID, new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

            PROPERTIES.bucket(FLUID_BUCKET).block(FLUID_BLOCK);
        }

        public ForgeFlowingFluid.Properties getFluidProperties() {
            return PROPERTIES;
        }
    }
}
