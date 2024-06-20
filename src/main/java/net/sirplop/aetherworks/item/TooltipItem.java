package net.sirplop.aetherworks.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipItem extends Item {
    protected final String tooltipLocation;
    protected final ChatFormatting format;
    public TooltipItem(Properties pProperties, String tooltipLocation, ChatFormatting format) {
        super(pProperties);
        this.tooltipLocation = tooltipLocation;
        this.format = format;
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(tooltipLocation).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
