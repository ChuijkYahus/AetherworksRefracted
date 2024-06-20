package net.sirplop.aetherworks.item;

import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.EmberInventoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sirplop.aetherworks.Aetherworks;
import net.sirplop.aetherworks.Config;
import net.sirplop.aetherworks.api.IToggleItem;
import net.sirplop.aetherworks.util.MoonlightRepair;
import net.sirplop.aetherworks.util.Utils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AOEEmberDiggerItem extends DiggerItem implements IToggleItem {

    public final TagKey<Block> blocks;

    public AOEEmberDiggerItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, TagKey<Block> pBlocks, Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, pBlocks, pProperties);
        blocks = pBlocks;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        if (!player.level().isClientSide() && isPoweredOn(stack)) {
            causeAoe((ServerLevel) player.level(), pos, player.level().getBlockState(pos), stack, player);
        }
        return super.onBlockStartBreak(stack, pos, player);
    }

    // modified from the JustHammers hammer code:
    // https://github.com/nanite/JustHammers/
    public void causeAoe(ServerLevel level, BlockPos pos, BlockState state, ItemStack stack, Player player) {
        if (!(player instanceof ServerPlayer)) return;

        if (level.isClientSide || state.getDestroySpeed(level, pos) == 0.0F) {
            return;
        }

        HitResult pick = player.pick(20D, 0.0F, false);

        // Hit something that wasn't a block.
        if (!(pick instanceof BlockHitResult blockHitResult)) {
            return;
        }

        //only break blocks that this can actually break.
        if (level.getBlockState(pos).getTags().anyMatch(blockTagKey -> blockTagKey == blocks))
            this.aoeMine(blockHitResult, pos, stack, level, player);
    }
    public void aoeMine(BlockHitResult pick, BlockPos blockPos, ItemStack stack, ServerLevel level, Player player) {
        if (!(player instanceof ServerPlayer)) return;

        Direction direction = pick.getDirection();
        var boundingBox = getAreaOfEffect(blockPos, direction, 2, 1);

        // Don't let the tool break if they don't have enough durability.
        if (!player.isCreative() && (stack.getDamageValue() >= stack.getMaxDamage() - 1)) {
            return;
        }

        GlowParticleOptions glow = getParticle();
        int damage = 0;
        BlockState targetState = level.getBlockState(blockPos);
        Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(boundingBox).iterator();
        Set<BlockPos> removedPos = new HashSet<>();
        while (iterator.hasNext()) {
            var pos = iterator.next();
            // Stop breaking if too much damage has accrued.
            if (!player.isCreative() && (stack.getDamageValue() + (damage + 1)) >= stack.getMaxDamage() - 1) {
                break;
            }
            if (pos.equals(blockPos)) {
                level.sendParticles(glow,
                        pos.getX() + 0.5f,
                        pos.getY() + 0.5f,
                        pos.getZ() + 0.5f,
                        10, 0.25f, 0.25f, 0.25f, 0.1f);
                continue;
            }

            if (removedPos.contains(pos) || !level.getBlockState(pos).is(targetState.getBlock())
                    || !canDestroy(targetState, level, pos)) {
                continue;
            }
            //use embers every block
            if (!consumeEmbers(player, Config.aetherToolEmberUse)) {
                toggleItem(stack, player);
                break;
            }

            removedPos.add(pos);
            if (stack.getItem() instanceof AOEEmberDiggerItem tool)
                tool.aoeMineTrigger(stack, pos, player);

            level.sendParticles(glow,
                    pos.getX() + 0.5f,
                    pos.getY() + 0.5f,
                    pos.getZ() + 0.5f,
                    10, 0.25f, 0.25f, 0.25f, 0.1f);
            Utils.breakAndHarvestBlock(level, pos, (ServerPlayer)player, stack, direction, (state) -> state == targetState, false);
            damage++;
        }

        if (damage != 0 && !player.isCreative()) {
            stack.hurt(damage, level.random, (ServerPlayer) player);
        }
    }
    public static BoundingBox getAreaOfEffect(BlockPos blockPos, Direction direction, int radius, int depth) {
        int size = (radius / 2);
        int offset = size - 1;

        return switch (direction) {
            case DOWN, UP -> new BoundingBox(blockPos.getX() - size, blockPos.getY() - (direction == Direction.UP ? depth - 1 : 0), blockPos.getZ() - size, blockPos.getX() + size, blockPos.getY() + (direction == Direction.DOWN ? depth - 1 : 0), blockPos.getZ() + size);
            case NORTH, SOUTH -> new BoundingBox(blockPos.getX() - size, blockPos.getY() - size + offset, blockPos.getZ() - (direction == Direction.SOUTH ? depth - 1 : 0), blockPos.getX() + size, blockPos.getY() + size + offset, blockPos.getZ() + (direction == Direction.NORTH ? depth - 1 : 0));
            case WEST, EAST -> new BoundingBox(blockPos.getX() - (direction == Direction.EAST ? depth - 1 : 0), blockPos.getY() - size + offset, blockPos.getZ() - size, blockPos.getX() + (direction == Direction.WEST ? depth - 1 : 0), blockPos.getY() + size + offset, blockPos.getZ() + size);
        };
    }
    private boolean canDestroy(BlockState targetState, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) == null && targetState.getDestroySpeed(level, pos) > 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (isPoweredOn(pStack)) {
            pTooltipComponents.add(Component.translatable("tooltip.aetherworks.aoe_on").withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.aetherworks.aoe_off").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private GlowParticleOptions getParticle() {
        GlowParticleOptions glow = new GlowParticleOptions(getParticleColor(), 1f, 20);
        return glow;
    }
    public abstract Vector3f getParticleColor();

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

        ItemStack stack = playerIn.getItemInHand(handIn);
        return useDelegate(stack, playerIn, handIn) ? InteractionResultHolder.success(stack) : InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return useDelegate(stack, context.getPlayer(), context.getHand()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        MoonlightRepair.tryRepair(stack, world, entity, Config.aethericRepairStrength);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private boolean useDelegate(ItemStack stack, Player player, InteractionHand hand) {
        if (Utils.isFakePlayer(player))
            return false;
        if (hand != InteractionHand.MAIN_HAND || !player.isShiftKeyDown())
            return false;

        Level level = player.getCommandSenderWorld();
        if (!level.isClientSide()) {
            //send toggle message to client and play sounds.
            boolean powerOn = isPoweredOn(stack);
            if (powerOn || EmberInventoryUtil.getEmberTotal(player) > Config.aetherToolEmberUse) { //powered on, play sound
                toggleItem(stack, player);
            }
        } else
        {
            boolean powerOn = !isPoweredOn(stack); //we're flipping modes, so opposite
            boolean ember = EmberInventoryUtil.getEmberTotal(player) > Config.aetherToolEmberUse;
            if (powerOn && ember) { //power on successfully
                Aetherworks.LOGGER.atDebug().log("Toggle On");
                level.playSound(player, player.getOnPos(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS, 0.6f, 0.9F);
            } else if (powerOn) { //power on fail
                Aetherworks.LOGGER.atDebug().log("Toggle Fail");
                level.playSound(player, player.getOnPos(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS, 0.6f, 0.0F);
            } else { //power off
                Aetherworks.LOGGER.atDebug().log("Toggle Off");
                level.playSound(player, player.getOnPos(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS, 0.6f, 0.6F);
            }
        }
        return true;
    }

    public boolean consumeEmbers(Player player, double value)
    {
        if (EmberInventoryUtil.getEmberTotal(player) >= value)
        {
            EmberInventoryUtil.removeEmber(player, value);
            return true;
        }
        return false;
    }
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        if (!newStack.is(oldStack.getItem()))
            return true;

        if (!newStack.isDamageableItem() || !oldStack.isDamageableItem())
            return !ItemStack.isSameItemSameTags(newStack, oldStack);

        CompoundTag newTag = newStack.getTag();
        CompoundTag oldTag = oldStack.getTag();

        if (newTag == null || oldTag == null)
            return !(newTag == null && oldTag == null);

        Set<String> newKeys = new HashSet<>(newTag.getAllKeys());
        Set<String> oldKeys = new HashSet<>(oldTag.getAllKeys());

        newKeys.remove(ItemStack.TAG_DAMAGE);
        oldKeys.remove(ItemStack.TAG_DAMAGE);

        return !newKeys.equals(oldKeys); //something that's not damage changed.
    }
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (oldStack.hasTag() && newStack.hasTag()) {
            return slotChanged || oldStack.getTag().getBoolean("poweredOn") != newStack.getTag().getBoolean("poweredOn") || newStack.getItem() != oldStack.getItem();
        }
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    public abstract void aoeMineTrigger(ItemStack stack, BlockPos pos, Player player);

    //original implementation, preserved forevermore
     /*private void tryPowerMine(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {
        if (!consumeEmbers(player, Config.aetherPickEmberUse))
        { //they're out of ember
            stack.getTag().putBoolean("poweredOn", false);
            return;
        }
        Direction facing = player.getDirection();
        for (int dx = -1; dx <= 1; ++dx)
        {
            for (int dy = -1; dy <= 1; ++dy)
            {
                if (dx == 0 && dy == 0)
                {
                    continue;
                }

                int oX = facing.getStepX() == 0 ? dx : 0;
                int oY = facing.getStepY() == 0 ? dy : 0;
                int oZ = facing.getStepZ() == 0 ? facing.getStepY() == 0 ? dx : dy : 0;
                BlockPos at = pos.offset(oX, oY, oZ);
                BlockState stateAt = level.getBlockState(at);
                if (!stateAt.equals(state))
                {
                    continue;
                }

                //spawn particles here

                //harvest block
            }
        }
    }*/
}
