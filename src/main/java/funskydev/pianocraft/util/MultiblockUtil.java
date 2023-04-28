package funskydev.pianocraft.util;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockMainPart;
import funskydev.pianocraft.block.MultiblockPartBlock;
import net.minecraft.block.Block;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MultiblockUtil {

    public static BlockPos getMainBlock(BlockPos source, BlockPosEnum position, Direction facing) {

        BlockPos newPos = source;

        switch(position) {

            case TOP:
                newPos = source.down();
                break;

            case EAST:
                newPos = translateWithRotation(source, Direction.WEST, facing);
                break;

            case WEST:
                newPos = translateWithRotation(source, Direction.EAST, facing);
                break;

            case TOP_WEST:
                newPos = translateWithRotation(source, Direction.EAST, facing).down();
                break;

            case TOP_EAST:
                newPos = translateWithRotation(source, Direction.WEST, facing).down();
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

            newPos = translatePos(source, direction.rotateClockwise(Direction.Axis.Y));

        } else if (facing == Direction.WEST) {

            newPos = translatePos(source, direction.rotateClockwise(Direction.Axis.Y).rotateClockwise(Direction.Axis.Y).rotateClockwise(Direction.Axis.Y));

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

        Map<BlockPos, BlockPosEnum> blocks = new HashMap<BlockPos, BlockPosEnum>();

        for(BlockPosEnum pos : multiblock.getBlocksPos()) {

            switch(pos) {
                case WEST:
                    blocks.put(translateWithRotation(source, Direction.WEST, mainBlockFacing), pos);
                    break;
                case EAST:
                    blocks.put(translateWithRotation(source, Direction.EAST, mainBlockFacing), pos);
                    break;
                case TOP:
                    blocks.put(source.up(), pos);
                    break;
                case TOP_WEST:
                    blocks.put(translateWithRotation(source, Direction.WEST, mainBlockFacing).up(), pos);
                    break;
                case TOP_EAST:
                    blocks.put(translateWithRotation(source, Direction.EAST, mainBlockFacing).up(), pos);
                    break;
            }

        }

        return blocks;

    }

    public static void placeBlockMap(World world, Map<BlockPos, BlockPosEnum> blocks, Direction facing, MultiblockEnum multiBlockEnum) {

        for(Map.Entry<BlockPos, BlockPosEnum> entry : blocks.entrySet()) {

            //Block newBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Main.MODID, multiBlockEnum.getName(entry.getValue())));
            Block newBlock = Registries.BLOCK.get(new Identifier(PCMain.MOD_ID, multiBlockEnum.getName(entry.getValue())));
            if(newBlock == null) {
                //System.out.println("CANT FIND THE BLOCK");
                return;
            }
            if(world.isAir(entry.getKey())) world.setBlockState(entry.getKey(), newBlock.getDefaultState().with(HorizontalFacingBlock.FACING, facing));

        }

    }

    public static boolean checkAround(World world, Map<BlockPos, BlockPosEnum> blocks) {

        for(Map.Entry<BlockPos, BlockPosEnum> entry : blocks.entrySet()) {

            if(!world.isAir(entry.getKey())) return false;

        }

        return true;

    }

    public static void attemptDestruction(World world, Map<BlockPos, BlockPosEnum> blocks) {

        for(Map.Entry<BlockPos, BlockPosEnum> entry : blocks.entrySet()) {

            if(world.getBlockState(entry.getKey()).getBlock() instanceof MultiblockPartBlock) {

                world.breakBlock(entry.getKey(), false);

            }

        }

    }

}
