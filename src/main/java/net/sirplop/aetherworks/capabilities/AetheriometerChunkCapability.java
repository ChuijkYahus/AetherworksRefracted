package net.sirplop.aetherworks.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.api.capabilities.IAetheriometerCap;
import net.sirplop.aetherworks.api.capabilities.SerializableCapabilityProvider;
import net.sirplop.aetherworks.network.MessageSyncAetheriometer;
import net.sirplop.aetherworks.network.PacketHandler;
import net.sirplop.aetherworks.worldgen.MeteorPlacer;

import static net.sirplop.aetherworks.capabilities.AWCapabilities.AETHERIOMETER_CAPABILITY;

public final class AetheriometerChunkCapability {

    /**
     * The ID of this capability.
     */
    public static final ResourceLocation ID = new ResourceLocation(Aetherworks.MODID, "aw.aether");

    /**
     * Get the {@link IAetheriometerCap} for the {@link Level} and chunk position.
     *
     * @param level    The level
     * @param chunkPos The chunk position
     * @return A lazy optional containing the IAetheriometerCap, if any
     */
    public static LazyOptional<IAetheriometerCap> getData(final Level level, final ChunkPos chunkPos) {
        return getData(level.getChunk(chunkPos.x, chunkPos.z));
    }

    /**
     * Get the {@link IAetheriometerCap} for the chunk.
     *
     * @param chunk The chunk
     * @return A lazy optional containing the IAetheriometerCap, if any
     */
    public static LazyOptional<IAetheriometerCap> getData(final LevelChunk chunk) {
        return chunk.getCapability(AETHERIOMETER_CAPABILITY, null);
    }

    @Mod.EventBusSubscriber(modid = Aetherworks.MODID)
    @SuppressWarnings("unused")
    private static class EventHandler {
        @SubscribeEvent
        public static void attachChunkCapabilities(final AttachCapabilitiesEvent<LevelChunk> event) {
            final var chunk = event.getObject();

            final var level = chunk.getLevel();
            final var chunkPos = chunk.getPos();

            int set = 0;
            if (MeteorPlacer.map.containsKey(chunkPos)) {
                set = MeteorPlacer.map.get(chunkPos);
                MeteorPlacer.map.remove(chunkPos);
            }

            final var data = AetheriometerChunk.createDefault(level, chunkPos, set);
            final var codec = AetheriometerChunk.codec(level, chunkPos);
            event.addCapability(ID, new SerializableCapabilityProvider<>(AETHERIOMETER_CAPABILITY, null, data) { });
        }

        /**
         * Send the {@link IAetheriometerCap} to the client when a player starts watching the chunk.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void chunkWatch(final ChunkWatchEvent.Watch event) {

            final var player = event.getPlayer();
            final int data = getData(event.getLevel(), event.getPos()).orElseThrow(UnsupportedOperationException::new).getData();

            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageSyncAetheriometer(event.getPos(), data));
        }
    }
}
