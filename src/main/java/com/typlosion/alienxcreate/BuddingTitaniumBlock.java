package com.typlosion.alienxcreate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BuddingTitaniumBlock extends Block {
    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingTitaniumBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) { // chance de pousser
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos targetPos = pos.relative(direction);
            BlockState targetState = level.getBlockState(targetPos);

            Block block = null;
            boolean canGrow = canGrowInto(targetState);
            if (canGrow) {
                block = AlienXCreateMod.SMALL_TITANIUM_BUD.get();
            } else if (targetState.is(AlienXCreateMod.SMALL_TITANIUM_BUD.get())
                    && targetState.getValue(AmethystClusterBlock.FACING) == direction) {
                block = AlienXCreateMod.MEDIUM_TITANIUM_BUD.get();
            } else if (targetState.is(AlienXCreateMod.MEDIUM_TITANIUM_BUD.get())
                    && targetState.getValue(AmethystClusterBlock.FACING) == direction) {
                block = AlienXCreateMod.LARGE_TITANIUM_BUD.get();
            } else if (targetState.is(AlienXCreateMod.LARGE_TITANIUM_BUD.get())
                    && targetState.getValue(AmethystClusterBlock.FACING) == direction) {
                block = AlienXCreateMod.TITANIUM_CLUSTER.get();
            }

            if (block != null) {
                level.setBlockAndUpdate(targetPos,
                        block.defaultBlockState()
                                .setValue(AmethystClusterBlock.FACING, direction)
                                .setValue(AmethystClusterBlock.WATERLOGGED, false)
                );
            }
        }
    }

    private boolean canGrowInto(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }
}