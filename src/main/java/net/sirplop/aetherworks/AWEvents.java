package net.sirplop.aetherworks;

import com.rekindled.embers.RegistryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.jfr.event.ChunkGenerationEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.sirplop.aetherworks.capabilities.AetheriometerChunkCapability;
import net.sirplop.aetherworks.lib.OctDirection;
import net.sirplop.aetherworks.lib.OctFacingHorizontalProperty;
import net.sirplop.aetherworks.network.MessageSyncAetheriometer;
import net.sirplop.aetherworks.network.PacketHandler;
import net.sirplop.aetherworks.util.AetheriometerUtil;
import net.sirplop.aetherworks.worldgen.MeteorPlacer;

@Mod.EventBusSubscriber
public class AWEvents {
    public static final int[][] FORGE_OFFSETS = {
            {-1, -1},
            {-1,  1},
            {1,  -1},
            {1,   1},
            {1,   0},
            {-1,  0},
            {0,  -1},
            {0,   1},
            {0,   0}
    };
    //Aetherium Forge construction - kinda scuffed, but ho hum this is how it was originally.
    @SubscribeEvent
    public static void onPlayerClickedBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.isCanceled() && event.getEntity() != null)
        {
            ItemStack stack = event.getItemStack();
            if (RegistryManager.TINKER_HAMMER.get().equals(stack.getItem())) {
                Level w = event.getLevel();
                BlockPos pos = event.getPos();
                Block dawnstoneBlock = RegistryManager.DAWNSTONE_BLOCK.get();
                if (w.getBlockState(pos).getBlock() != dawnstoneBlock)
                    return;
                for (int[] ints : FORGE_OFFSETS) {
                    BlockPos offset = pos.offset(ints[0], 0, ints[1]);
                    if (w.getBlockState(offset).getBlock() != dawnstoneBlock) {
                        return;
                    }
                }

                w.playSound(event.getEntity(), pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.25f, 0.1f + w.random.nextFloat() * 0.25f);
                event.getEntity().swing(event.getHand());
                if (event.getEntity() instanceof ServerPlayer) {
                    for (int[] ints : FORGE_OFFSETS) {
                        BlockPos offset = pos.offset(ints[0], 0, ints[1]);
                        w.setBlock(offset, AWRegistry.FORGE_BLOCK.get().defaultBlockState()
                                .setValue(OctFacingHorizontalProperty.OCT_DIRECTIONS, OctDirection.getFromOffset(ints[0], ints[1])), 1 | 2);
                    }
                }
                event.setCanceled(true);
            }
        }
    }
}
