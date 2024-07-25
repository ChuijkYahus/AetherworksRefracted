package net.sirplop.aetherworks.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sirplop.aetherworks.Aetherworks;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Aetherworks.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, MessageHarvestNode.class, MessageHarvestNode::encode, MessageHarvestNode::decode, MessageHarvestNode::handle);
        INSTANCE.registerMessage(id++, MessageToggleItem.class, MessageToggleItem::encode, MessageToggleItem::decode, MessageToggleItem::handle);
        INSTANCE.registerMessage(id++, MessageSyncItemEntityTag.class, MessageSyncItemEntityTag::encode, MessageSyncItemEntityTag::decode, MessageSyncItemEntityTag::handle);
        INSTANCE.registerMessage(id++, MessageFocusedStack.class, MessageFocusedStack::encode, MessageFocusedStack::decode, MessageFocusedStack::handle);
        INSTANCE.registerMessage(id++, MessageSurroundWIthParticles.class, MessageSurroundWIthParticles::encode, MessageSurroundWIthParticles::decode, MessageSurroundWIthParticles::handle);
        INSTANCE.registerMessage(id++, MessageFluidSync.class, MessageFluidSync::encode, MessageFluidSync::decode, MessageFluidSync::handle);
        INSTANCE.registerMessage(id++, MessageSyncAetheriometer.class, MessageSyncAetheriometer::encode, MessageSyncAetheriometer::decode, MessageSyncAetheriometer::handle);
    }
}
