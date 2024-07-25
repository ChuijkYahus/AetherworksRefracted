package net.sirplop.aetherworks.api.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IAetheriometerCap extends ICapabilitySerializable<CompoundTag> {
    int getData();
    Level getLevel();
    ChunkPos getChunk();
    void adjustData(int change);
}
