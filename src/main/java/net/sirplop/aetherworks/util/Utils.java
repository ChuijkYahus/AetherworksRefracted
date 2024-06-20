package net.sirplop.aetherworks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Utils {

    public static void dropItemIntoWorld(Level level, BlockPos pos, ItemStack item) {
        Random rand = new Random();

        if (item != null && item.getCount() > 0) {
            float rx = rand.nextFloat() * 0.8F + 0.1F;
            float ry = rand.nextFloat() * 0.8F + 0.1F;
            float rz = rand.nextFloat() * 0.8F + 0.1F;

            ItemEntity entityItem = new ItemEntity(level,
                    pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
                    item.copy());
            entityItem.setDefaultPickUpDelay();;

            if (item.hasTag()) {
                entityItem.getItem().setTag(item.getTag().copy());
            }

            float factor = 0.05F;
            entityItem.lerpMotion(
                    rand.nextGaussian() * factor,
                    rand.nextGaussian() * factor + 0.2F,
                    rand.nextGaussian() * factor);
            level.addFreshEntity(entityItem);
            item.setCount(0);
        }
    }
    public static boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }
    public static Vector3f colorIntToVector(int r, int g, int b) {
        return new Vector3f(r / 255f, g / 255f, b / 255f);
    }

    /**
     * Tries to harvest the block at the given location using the given item.
     * @return  True if successful
     */
    public static boolean breakAndHarvestBlock(ServerLevel level, BlockPos pos, ServerPlayer player, ItemStack itemInHand,
                                               Direction dropDirection, @Nullable Predicate<BlockState> matchBlock, boolean allowBreakBlockEntities) {
        if (level.isClientSide() || !level.isLoaded(pos))
            return false; //clients can't mine, and unloaded chunks can't have stuff broken in them!

        BlockState targetState = level.getBlockState(pos);
        if (targetState.isAir())
            return false; //can't harvest air!

        if (!allowBreakBlockEntities && level.getBlockEntity(pos) != null)
            return false; //don't break block entities if we don't want to!

        boolean canHarvest = targetState.getBlock().canHarvestBlock(targetState, level, pos, player);
        boolean match = matchBlock == null || matchBlock.test(targetState);

        if (!match || !canHarvest)
            return false;

        level.destroyBlock(pos, false, player);
        if (!player.isCreative()) {
            boolean correctToolForDrops = itemInHand.isCorrectToolForDrops(targetState);
            if (correctToolForDrops) {
                targetState.spawnAfterBreak(level, pos, itemInHand, true);
                List<ItemStack> drops = Block.getDrops(targetState, level, pos, level.getBlockEntity(pos), player, itemInHand);
                drops.forEach(e -> Block.popResourceFromFace(level, pos, dropDirection, e));
            }
        }
        return true;
    }

    public static boolean sendPlayerMessage(ServerPlayer player, String message) {
        if (player == null)
            return false;
        PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(player.getUUID(), message);
        player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false, ChatType.bind(ChatType.CHAT, player));
        return true;
    }
}
