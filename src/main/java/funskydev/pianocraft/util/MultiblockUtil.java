package funskydev.pianocraft.util;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockPartBlock;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class MultiblockUtil {

    public static BlockPos getMainBlock(BlockPos source, BlockPosEnum position, Direction facing) {

        BlockPos newPos = source;

        switch(position) {

            case TOP:
                newPos = source.below();
                break;

            case EAST:
                newPos = translateWithRotation(source, Direction.WEST, facing);
                break;

            case WEST:
                newPos = translateWithRotation(source, Direction.EAST, facing);
                break;

            case TOP_WEST:
                newPos = translateWithRotation(source, Direction.EAST, facing).below();
                break;

            case TOP_EAST:
                newPos = translateWithRotation(source, Direction.WEST, facing).below();
                break;

        }

        return newPos;

    }

    public static BlockPos translateWithRotation(BlockPos source, Direction direction, Direction facing) {

        BlockPos newPos = source;

        if(facing == Direction.NORTH) newPos = translatePos(source, direction);

        if(facing == Direction.SOUTH) {

            newPos = translatePos(source, direction.getOpposite());

        } else if (facing == Direction.EAST) {

            newPos = translatePos(source, direction.getClockWise(Direction.Axis.Y));

        } else if (facing == Direction.WEST) {

            newPos = translatePos(source, direction.getClockWise(Direction.Axis.Y).getClockWise(Direction.Axis.Y).getClockWise(Direction.Axis.Y));

        }

        return newPos;

    }

    public static BlockPos translatePos(BlockPos source, Direction direction) {

        BlockPos newPos = source;

        switch(direction) {

            case NORTH:
                newPos = source.north();
                break;

            case EAST:
                newPos = source.east();
                break;

            case SOUTH:
                newPos = source.south();
                break;

            case WEST:
                newPos = source.west();
                break;

        }

        return newPos;

    }

    public static Map<BlockPos, BlockPosEnum> getMultBlocks(BlockPos source, Direction mainBlockFacing, MultiblockEnum multiblock) {

        Map<BlockPos, BlockPosEnum> blocks = new HashMap<>();

        for(BlockPosEnum pos : multiblock.getBlocksPosList()) {

            switch(pos) {
                case WEST:
                    blocks.put(translateWithRotation(source, Direction.WEST, mainBlockFacing), pos);
                    break;
                case EAST:
                    blocks.put(translateWithRotation(source, Direction.EAST, mainBlockFacing), pos);
                    break;
                case TOP:
                    blocks.put(source.above(), pos);
                    break;
                case TOP_WEST:
                    blocks.put(translateWithRotation(source, Direction.WEST, mainBlockFacing).above(), pos);
                    break;
                case TOP_EAST:
                    blocks.put(translateWithRotation(source, Direction.EAST, mainBlockFacing).above(), pos);
                    break;
            }

        }

        return blocks;

    }

    public static void placeBlockMap(Level world, Map<BlockPos, BlockPosEnum> blocks, Direction facing, MultiblockEnum multiBlockEnum) {

        for(Map.Entry<BlockPos, BlockPosEnum> entry : blocks.entrySet()) {

            Block newBlock = BuiltInRegistries.BLOCK.getValue(new Identifier(PCMain.MOD_ID, multiBlockEnum.getName(entry.getValue())));
            if(newBlock == null) return;
            if(world.isEmptyBlock(entry.getKey())) world.setBlockAndUpdate(entry.getKey(), newBlock.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing));

        }

    }

    public static boolean checkAround(Level world, Map<BlockPos, BlockPosEnum> blocks) {

        for(Map.Entry<BlockPos, BlockPosEnum> entry : blocks.entrySet()) {

            if(!world.isEmptyBlock(entry.getKey())) return false;

        }

        return true;

    }

    public static void attemptDestruction(Level world, Map<BlockPos, BlockPosEnum> blocks) {

        for(Map.Entry<BlockPos, BlockPosEnum> entry : blocks.entrySet()) {

            if(world.getBlockState(entry.getKey()).getBlock() instanceof MultiblockPartBlock) {

                world.destroyBlock(entry.getKey(), false);

            }

        }

    }

}
