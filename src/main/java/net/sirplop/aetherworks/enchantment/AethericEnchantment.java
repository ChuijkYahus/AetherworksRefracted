package net.sirplop.aetherworks.enchantment;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.sirplop.aetherworks.util.MoonlightRepair;


/**
 * Might come back to this and change the aetheric items to use this instead.
 */
public class AethericEnchantment extends Enchantment {
    public AethericEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.is(ItemTags.TOOLS) || pStack.is(Tags.Items.ARMORS);  //a bit broad? who cares!
    }                               // if you can move the enchantment, then have fun.

    public boolean isTreasureOnly() {
        return true;
    }
    /**
     * @return Whether the enchantment can go in loot tables.
     */
    @Override
    public boolean isDiscoverable() {
        return false;
    }

    /**
     * @return Whether the enchantment can go in villager trades.
     */
    @Override
    public boolean isTradeable() {
        return false;
    }
}
