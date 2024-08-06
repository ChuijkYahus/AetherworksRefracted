package net.sirplop.aetherworks;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.augment.IAugment;
import com.rekindled.embers.block.AtmosphericGaugeBlock;
import com.rekindled.embers.fluidtypes.EmbersFluidType;
import com.rekindled.embers.item.EmberStorageItem;
import com.rekindled.embers.util.AshenArmorMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.sirplop.aetherworks.augment.TuningCylinderAugment;
import net.sirplop.aetherworks.block.*;
import net.sirplop.aetherworks.block.forge.*;
import net.sirplop.aetherworks.blockentity.*;
import net.sirplop.aetherworks.effect.MoonfireEffect;
import net.sirplop.aetherworks.enchantment.AethericEnchantment;
import net.sirplop.aetherworks.entity.DummyArmorLoaderEntity;
import net.sirplop.aetherworks.fluid.AWViscousFluidType;
import net.sirplop.aetherworks.fluid.AWMoltenMetalFluidType;
import net.sirplop.aetherworks.item.*;
import net.sirplop.aetherworks.recipe.*;
import net.sirplop.aetherworks.util.AetheriumTiers;
import net.sirplop.aetherworks.util.Utils;
import net.sirplop.aetherworks.worldgen.MeteorStructure;
import net.sirplop.aetherworks.worldgen.MeteorStructurePiece;
import org.jetbrains.annotations.NotNull;

public class AWRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Aetherworks.MODID);
    public static final DeferredRegister<Block> BLOCKS =  DeferredRegister.create(ForgeRegistries.BLOCKS, Aetherworks.MODID);
    public static final DeferredRegister<FluidType> FLUIDTYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Aetherworks.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aetherworks.MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Aetherworks.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =  DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Aetherworks.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Aetherworks.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Aetherworks.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Aetherworks.MODID);
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Aetherworks.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Aetherworks.MODID);

    public static List<FluidStuff> fluidList = new ArrayList<>();

    public static FluidStuff addFluid(EmbersFluidType.FluidInfo info,
                                      BiFunction<FluidType.Properties, EmbersFluidType.FluidInfo, FluidType> type,
                                      BiFunction<Supplier<? extends FlowingFluid>,Properties, LiquidBlock> block,
                                      Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Source> source,
                                      Function<ForgeFlowingFluid.Properties, ForgeFlowingFluid.Flowing> flowing,
                                      @Nullable Consumer<ForgeFlowingFluid.Properties> fluidProperties, FluidType.Properties prop) {
        FluidStuff fluid = new FluidStuff(info.name, info.color, type.apply(prop, info), block, fluidProperties, source, flowing);
        fluidList.add(fluid);
        return fluid;
    }

    public static FluidStuff addFluid(EmbersFluidType.FluidInfo info,
                                      BiFunction<FluidType.Properties, EmbersFluidType.FluidInfo, FluidType> type,
                                      BiFunction<Supplier<? extends FlowingFluid>, Properties, LiquidBlock> block,
                                      @Nullable Consumer<ForgeFlowingFluid.Properties> fluidProperties, FluidType.Properties prop) {
        return addFluid(info, type, block, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, fluidProperties, prop);
    }

    //Items & Blocks
    public static final RegistryObject<Item> PICKAXE_EMBER = ITEMS.register("pickaxe_ember", () -> new EmberPickaxe(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PICKAXE_AETHER = ITEMS.register("pickaxe_aether", () -> new AetherPickaxe(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> AXE_ENDER = ITEMS.register("axe_ender", () -> new EnderAxe(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> AXE_SCULK = ITEMS.register("axe_sculk", () -> new SculkAxe(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SHOVEL_SLIME = ITEMS.register("shovel_slime", () -> new SlimeShovel(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SHOVEL_PRISMARINE = ITEMS.register("shovel_prismarine", () -> new PrismarineShovel(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> CROSSBOW_QUARTZ = ITEMS.register("crossbow_quartz", () -> new AetherCrossbowQuartz(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(AetheriumTiers.AETHERIUM.getUses()).defaultDurability(AetheriumTiers.AETHERIUM.getUses())));
    public static final RegistryObject<Item> CROSSBOW_MAGMA = ITEMS.register("crossbow_magma", () -> new AetherCrossbowMagma(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(AetheriumTiers.AETHERIUM.getUses()).defaultDurability(AetheriumTiers.AETHERIUM.getUses())));

    public static final RegistryObject<Item> AETHER_CROWN = ITEMS.register("aether_crown", () -> new AetherCrownItem(AshenArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> POTION_GEM = ITEMS.register("potion_gem", () -> new PotionGemItem(new Item.Properties()));

    public static final RegistryObject<Item> AETHER_EMBER_JAR = ITEMS.register("aether_ember_jar", () -> new AetherEmberJarItem(new Item.Properties()));
    public static final RegistryObject<Item> AETHER_EMBER_CARTRIDGE = ITEMS.register("aether_ember_cartridge", () -> new AetherEmberCartridgeItem(new Item.Properties()));
    public static final RegistryObject<Item> AETHERIOMETER = ITEMS.register("aetheriometer", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Block> HEAT_DIAL = registerBlock("heat_dial", () -> new HeatDialBlock(Properties.copy(RegistryManager.EMBER_DIAL.get())));
    public static final RegistryObject<Block> PRISM = registerBlock("prism", () -> new PrismBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(4, 8).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> PRISM_SUPPORT = registerBlock("prism_support", () -> new PrismSupportBlock(Properties.copy(RegistryManager.CAMINITE_BRICKS.get()).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOONLIGHT_AMPLIFIER = registerBlock("moonlight_amplifier", () -> new MoonlightAmplifierBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).requiresCorrectToolForDrops().strength(3, 6).sound(SoundType.GLASS)));
    public static final RegistryObject<Block> CONTROL_MATRIX = registerBlock("aether_prism_controller_matrix", () -> new ControlMatrixBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(3, 6).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_CORE = registerBlock("forge_core", () -> new ForgeCoreBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(3, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_VENT = registerBlock("forge_vent", () -> new ForgeVentBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(3, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_HEATER = registerBlock("forge_heater", () -> new ForgeHeaterBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(3, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_COOLER = registerBlock("forge_cooler", () -> new ForgeCoolerBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(3, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_ANVIL = registerBlock("forge_anvil", () -> new AetheriumAnvilBlock(Properties.copy(RegistryManager.DAWNSTONE_ANVIL.get()).strength(3, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_METAL_FORMER = registerBlock("forge_metal_former", () -> new MetalFormerBlock(Properties.copy(RegistryManager.FLUID_VESSEL.get()).strength(3, 10).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FORGE_TOOL_STATION = registerBlock("forge_tool_station", () -> new ForgeToolStation(Properties.copy(RegistryManager.FLUID_VESSEL.get()).requiresCorrectToolForDrops().strength(3, 6)));

    public static final RegistryObject<Block> FORGE_BLOCK = registerBlock("forge_block", () -> new ForgeStructureBlock(Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> SUEVITE = registerBlock("suevite", () -> new Block(Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(2.5f)));
    public static final RegistryObject<Block> SUEVITE_COBBLE = registerBlock("suevite_cobble", () -> new Block(Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(1.6f)));;
    public static final StoneDecoBlocks SUEVITE_COBBLE_DECO = new StoneDecoBlocks("suevite_cobble", SUEVITE_COBBLE, Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(1.6f));
    public static final RegistryObject<Block> SUEVITE_BRICKS = registerBlock("suevite_bricks", () -> new Block(Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(1.6f)));;
    public static final StoneDecoBlocks SUEVITE_BRICKS_DECO = new StoneDecoBlocks("suevite_bricks", SUEVITE_BRICKS, Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(1.6f));
    public static final RegistryObject<Block> GLASS_AETHERIUM = registerBlock("glass_aetherium", () -> new StainedGlassBlock(DyeColor.LIGHT_BLUE, Properties.copy(Blocks.LIGHT_BLUE_STAINED_GLASS).explosionResistance(1800000).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> AETHERIUM_ORE = registerBlock("ore_aether", () -> new AetherOreBlock(Properties.copy(Blocks.STONE).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(5, 12), UniformInt.of(4, 8)));
    public static final RegistryObject<Block> AETHERIUM_SHARD_BLOCK = registerBlock("block_shards_raw", () -> new AetherBlock(Properties.copy(Blocks.STONE).sound(SoundType.ANCIENT_DEBRIS).requiresCorrectToolForDrops().strength(5, 12)));
    public static final RegistryObject<Block> AETHERIUM_BLOCK = registerBlock("block_aether", () -> new AetherBlock(Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(10, 40)));

    public static final RegistryObject<Item> AETHER_SHARD = ITEMS.register("aether_shard", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.shard", false));
    public static final RegistryObject<Item> AETHER_AMALGAM = ITEMS.register("aether_amalgam", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHER_PEARL = ITEMS.register("aether_pearl", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FOCUS_CRYSTAL = ITEMS.register("focus_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHERIUM_LENS = ITEMS.register("aetherium_lens", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATE_AETHER = ITEMS.register("plate_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INGOT_AETHER = ITEMS.register("ingot_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEM_AETHER = ITEMS.register("gem_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOOL_ROD_CRUDE = ITEMS.register("tool_rod_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> TOOL_ROD = ITEMS.register("tool_rod", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> TOOL_ROD_INFUSED = ITEMS.register("tool_rod_infused", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> PICKAXE_HEAD_CRUDE = ITEMS.register("pickaxe_head_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> PICKAXE_HEAD = ITEMS.register("pickaxe_head", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> PICKAXE_HEAD_AETHER = ITEMS.register("pickaxe_head_aether", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.pickaxe_head_aether", false));
    public static final RegistryObject<Item> PICKAXE_HEAD_EMBER = ITEMS.register("pickaxe_head_ember", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.pickaxe_head_ember", false));
    public static final RegistryObject<Item> AXE_HEAD_CRUDE = ITEMS.register("axe_head_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> AXE_HEAD = ITEMS.register("axe_head", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> AXE_HEAD_SCULK = ITEMS.register("axe_head_sculk", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.axe_head_sculk", false));
    public static final RegistryObject<Item> AXE_HEAD_ENDER = ITEMS.register("axe_head_ender", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.axe_head_ender", false));
    public static final RegistryObject<Item> SHOVEL_HEAD_CRUDE = ITEMS.register("shovel_head_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> SHOVEL_HEAD = ITEMS.register("shovel_head", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> SHOVEL_HEAD_PRISMARINE = ITEMS.register("shovel_head_prismarine", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.shovel_head_prismarine", false));
    public static final RegistryObject<Item> SHOVEL_HEAD_SLIME = ITEMS.register("shovel_head_slime", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.shovel_head_slime", false));
    public static final RegistryObject<Item> AETHER_CROWN_CRUDE = ITEMS.register("aether_crown_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> AETHER_CROWN_MUNDANE = ITEMS.register("aether_crown_mundane", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> CROSSBOW_FRAME_CRUDE = ITEMS.register("crossbow_frame_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> CROSSBOW_FRAME = ITEMS.register("crossbow_frame", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> CROSSBOW_FRAME_INFUSED = ITEMS.register("crossbow_frame_infused", () -> new SimpleFoiledItem(new Item.Properties()));
    public static final RegistryObject<Item> CROSSBOW_LIMBS_CRUDE = ITEMS.register("crossbow_limbs_crude", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_crude", false));
    public static final RegistryObject<Item> CROSSBOW_LIMBS = ITEMS.register("crossbow_limbs", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.must_work_uninfused", false));
    public static final RegistryObject<Item> CROSSBOW_LIMBS_QUARTZ = ITEMS.register("crossbow_limbs_quartz", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.crossbow_limbs_quartz", false));
    public static final RegistryObject<Item> CROSSBOW_LIMBS_MAGMA = ITEMS.register("crossbow_limbs_magma", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.crossbow_limbs_magma", false));
    public static final RegistryObject<Item> GEODE_END = ITEMS.register("geode_end", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_NETHER = ITEMS.register("geode_nether", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_HOT = ITEMS.register("geode_hot", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_COLD = ITEMS.register("geode_cold", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_MAGIC = ITEMS.register("geode_magical", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_OCEAN = ITEMS.register("geode_ocean", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_DEEP = ITEMS.register("geode_deep", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> GEODE_BASIC = ITEMS.register("geode_basic", () -> new TooltipItem(new Item.Properties(), "aetherworks.tooltip.geode", false));
    public static final RegistryObject<Item> AETHER_ASPECTUS = ITEMS.register("aspectus_aetherium", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNING_CYLINDER = ITEMS.register("tuning_cylinder", () -> new Item(new Item.Properties()));


    //Augments
    public static final IAugment TUNING_CYLINDER_AUGMENT = AugmentUtil.registerAugment(new TuningCylinderAugment(new ResourceLocation(Aetherworks.MODID, "tuning_cylinder")));

    //Fluids
    public static final FluidStuff AETHERIUM_GAS_IMPURE = addFluid(new EmbersFluidType.FluidInfo("aether_gas_impure", 0xff6c829f, 0.1F, 1.5F),
            AWViscousFluidType::new, LiquidBlock::new,
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
                    .density(800)
                    .viscosity(20)
                    .temperature(1020)
                    .lightLevel(6));
    public static final FluidStuff AETHERIUM_GAS = addFluid(new EmbersFluidType.FluidInfo("aether_gas", 0xff00b8ff, 0.1F, 1.5F),
            AWViscousFluidType::new, LiquidBlock::new,
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
                    .density(1)
                    .viscosity(10)
                    .temperature(1020)
                    .lightLevel(10));
    public static final FluidStuff ALCHEMIC_PRECURSOR = addFluid(new EmbersFluidType.FluidInfo("alchemic_precursor", 0x9a5e45, 0.1F, 1.5F),
            AWMoltenMetalFluidType::new, LiquidBlock::new,
            prop -> prop.explosionResistance(1000F).tickRate(3),
            FluidType.Properties.create()
                    .canSwim(true)
                    .canDrown(true)
                    .pathType(BlockPathTypes.LAVA)
                    .adjacentPathType(null)
                    .motionScale(0.00233)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .density(2000)
                    .viscosity(6000)
                    .temperature(1100)
                    .lightLevel(12));

    //Block Entities
    public static final RegistryObject<BlockEntityType<PrismBlockEntity>> PRISM_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("prism_block_entity", () -> BlockEntityType.Builder.of(PrismBlockEntity::new, PRISM.get()).build(null));
    public static final RegistryObject<BlockEntityType<ForgeCoreBlockEntity>> FORGE_CORE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("forge_core_block_entity", () -> BlockEntityType.Builder.of(ForgeCoreBlockEntity::new, FORGE_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ForgeHeatVentBlockEntity>> FORGE_VENT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("forge_vent_block_entity", () -> BlockEntityType.Builder.of(ForgeHeatVentBlockEntity::new, FORGE_VENT.get()).build(null));
    public static final RegistryObject<BlockEntityType<ForgeHeaterBlockEntity>> FORGE_HEATER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("forge_heater_block_entity", () -> BlockEntityType.Builder.of(ForgeHeaterBlockEntity::new, FORGE_HEATER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ForgeCoolerBlockEntity>> FORGE_COOLER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("forge_cooler_block_entity", () -> BlockEntityType.Builder.of(ForgeCoolerBlockEntity::new, FORGE_COOLER.get()).build(null));
    public static final RegistryObject<BlockEntityType<HeatDialBlockEntity>> HEAT_DIAL_ENTITY = BLOCK_ENTITY_TYPES.register("heat_dial_block_entity", () -> BlockEntityType.Builder.of(HeatDialBlockEntity::new, HEAT_DIAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<MetalFormerBlockEntity>> METAL_FORMER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("metal_former_block_entity", () -> BlockEntityType.Builder.of(MetalFormerBlockEntity::new, FORGE_METAL_FORMER.get()).build(null));
    public static final RegistryObject<BlockEntityType<AetheriumAnvilBlockEntity>> AETHERIUM_ANVIL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("aetherium_anvil_block_entity", () -> BlockEntityType.Builder.of(AetheriumAnvilBlockEntity::new, FORGE_ANVIL.get()).build(null));
    public static final RegistryObject<BlockEntityType<ToolStationBlockEntity>> TOOL_STATION_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("tool_station_block_entity", () -> BlockEntityType.Builder.of(ToolStationBlockEntity::new, FORGE_TOOL_STATION.get()).build(null));

    //Creative Tabs
    public static final RegistryObject<CreativeModeTab> AW_TAB = CREATIVE_MODE_TAB.register("aetherworks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AWRegistry.AETHER_SHARD.get()))
                    .title(Component.translatable("itemgroup." + Aetherworks.MODID))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((params, output) -> {
                        for (RegistryObject<Item> item : ITEMS.getEntries()) {
                            if (item.get().equals(FORGE_BLOCK.get().asItem()))
                                continue;
                            output.accept(item.get());

                            if (item.get() instanceof EmberStorageItem)
                                output.accept(EmberStorageItem.withFill(item.get(), ((EmberStorageItem) item.get()).getCapacity()));
                        }
                        PotionGemItem.getAllPotionGems(output);
                    })
                    .build());

    //Enchantments
    public static final RegistryObject<Enchantment> AETHERIC_ENCHANTMENT =
            ENCHANTMENTS.register("aetheric", () -> new AethericEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    //Entities
    public static final RegistryObject<EntityType<DummyArmorLoaderEntity>> DUMMY_LOADER = registerEntity("dummy_loader", EntityType.Builder.<DummyArmorLoaderEntity>of(DummyArmorLoaderEntity::new, MobCategory.MISC).sized(0.0F, 0F));

    //Spawn Eggs

    //Particle Types

    //Mob Effects & Potions
    public static final RegistryObject<MobEffect> EFFECT_MOONFIRE = MOB_EFFECTS.register("moonfire", () -> new MoonfireEffect(MobEffectCategory.HARMFUL, Utils.AETHERIUM_PROJECTILE_COLOR.getRGB()));

    //Recipe Types
    public static final RegistryObject<RecipeType<IMetalFormerRecipe>> METAL_FORMING = registerRecipeType("metal_forming");
    public static final RegistryObject<RecipeType<IAetheriumAnvilRecipe>> AETHERIUM_ANVIL = registerRecipeType("aetherium_anvil");
    public static final RegistryObject<RecipeType<IToolStationRecipe>> TOOL_STATION_RECIPE = registerRecipeType("tool_station");
    public static final RegistryObject<RecipeSerializer<PotionGemSocketRecipe>> GEM_SOCKET_SERIALIZER = RECIPE_SERIALIZERS.register("potion_gem_socket", () -> PotionGemSocketRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<PotionGemUnsocketRecipe>> GEM_UNSOCKET_SERIALIZER = RECIPE_SERIALIZERS.register("potion_gem_unsocket", () -> PotionGemUnsocketRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<PotionGemImbueRecipe>> GEM_IMBUE_SERIALIZER = RECIPE_SERIALIZERS.register("potion_gem_imbue", () -> PotionGemImbueRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<DrainRecipe>> DRAIN_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("drain_shovel", () -> DrainRecipe.SERIALIZER);

    //Recipe Serializers
    public static final RegistryObject<RecipeSerializer<MetalFormerRecipe>> METAL_FORMING_SERIALIZER = RECIPE_SERIALIZERS.register("metal_forming", () -> MetalFormerRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<AetheriumAnvilRecipe>> AETHERIUM_ANVIL_SERIALIZER = RECIPE_SERIALIZERS.register("aetherium_anvil", () -> AetheriumAnvilRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<ToolStationRecipe>> TOOL_STATION_SERIALIZER = RECIPE_SERIALIZERS.register("tool_station", () -> ToolStationRecipe.SERIALIZER);

    //Structures
    public static final RegistryObject<StructureType<MeteorStructure>> METEOR_STRUCTURE = STRUCTURE_TYPES.register("meteor", () -> MeteorStructure.TYPE);

    //Structure Pieces
    public static final RegistryObject<?> METEOR_STRUCTURE_PIECE = STRUCTURE_PIECES.register("meteor", () -> MeteorStructurePiece.TYPE);

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

            EmbersAPI.registerEmberResonance(Ingredient.of(PICKAXE_EMBER.get(), PICKAXE_AETHER.get(), AXE_ENDER.get(), AXE_SCULK.get(), SHOVEL_PRISMARINE.get(), SHOVEL_SLIME.get(), CROSSBOW_MAGMA.get(), CROSSBOW_QUARTZ.get()), 2.5);
            EmbersAPI.registerEmberResonance(Ingredient.of(AETHER_CROWN.get()), 2.5);
            EmbersAPI.registerWearableLens(Ingredient.of(AETHER_CROWN.get())); //of course it's a lens!

            FluidInteractionRegistry.addInteraction(AETHERIUM_GAS.FLUID.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(ForgeMod.WATER_TYPE.get(),
                    fluidState -> fluidState.isSource() ? SUEVITE.get().defaultBlockState() : SUEVITE_COBBLE.get().defaultBlockState()));
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
                          BiFunction<Supplier<? extends FlowingFluid>, Properties, LiquidBlock> block,
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

    public static class StoneDecoBlocks {
        public String name;
        public RegistryObject<Block> block;
        public RegistryObject<StairBlock> stairs;
        public RegistryObject<Item> stairsItem;
        public RegistryObject<SlabBlock> slab;
        public RegistryObject<Item> slabItem;
        public RegistryObject<WallBlock> wall;
        public RegistryObject<Item> wallItem;

        public StoneDecoBlocks(String name, RegistryObject<Block> block, Properties properties, boolean stairs, boolean slab, boolean wall) {
            this.stairs = null;
            this.stairsItem = null;
            this.slab = null;
            this.slabItem = null;
            this.wall = null;
            this.wallItem = null;
            this.name = name;
            this.block = block;
            if (stairs) {
                this.stairs = BLOCKS.register(name + "_stairs", () ->
                        new StairBlock(() -> block.get().defaultBlockState(), properties));
                this.stairsItem = registerBlockItem(name + "_stairs", this.stairs);
            }

            if (slab) {
                this.slab = BLOCKS.register(name + "_slab", () -> new SlabBlock(properties));
                this.slabItem = registerBlockItem(name + "_slab", this.slab);
            }

            if (wall) {
                this.wall = BLOCKS.register(name + "_wall", () -> new WallBlock(properties));
                this.slabItem = registerBlockItem(name + "_wall", this.wall);
            }

        }

        public StoneDecoBlocks(String name, RegistryObject<Block> block, Properties properties) {
            this(name, block, properties, true, true, true);
        }
    }

    public static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerRecipeType(final String identifier) {
        return RECIPE_TYPES.register(identifier, () -> new RecipeType<T>() {
            public String toString() {
                return Aetherworks.MODID + ":" + identifier;
            }
        });
    }
    public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(Aetherworks.MODID + ":" + name));
    }
}
