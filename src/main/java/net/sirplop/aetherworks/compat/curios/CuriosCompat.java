package net.sirplop.aetherworks.compat.curios;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.item.AetherEmberBulbItem;

public class CuriosCompat {

    public static final RegistryObject<Item> AETHER_EMBER_BULB = AWRegistry.ITEMS.register("aether_ember_bulb", () -> new AetherEmberBulbItem(new Item.Properties().stacksTo(1)));

    public static void init() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerColorHandler(RegisterColorHandlersEvent.Item event, ItemColor itemColor) {
        event.register(itemColor, AETHER_EMBER_BULB.get());
    }
}
