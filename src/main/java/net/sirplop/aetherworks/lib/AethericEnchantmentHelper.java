package net.sirplop.aetherworks.lib;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.TickEvent;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.enchantment.AethericEnchantment;
import net.sirplop.aetherworks.util.MoonlightRepair;

import java.util.Map;

public class AethericEnchantmentHelper {

    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        //check if they have any aetheric items in their inventory.
        ServerPlayer player = (ServerPlayer) event.player;
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.items) {
            check(item, player);
        }
        for (ItemStack item : player.getArmorSlots())
            check(item, player);
        check(player.getOffhandItem(), player);
    }
    private static void check(ItemStack item, ServerPlayer player) {
        if (item.isDamageableItem()) { //quick n dirty filter, speeds things up.
            Map<Enchantment, Integer> enchants = item.getAllEnchantments();
            if (enchants.containsKey(AWRegistry.AETHERIC_ENCHANTMENT.get())) {
                MoonlightRepair.tryRepair(item, player.level(), player, enchants.get(AWRegistry.AETHERIC_ENCHANTMENT.get()));
            }
        }
    }
}
