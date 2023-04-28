package funskydev.pianocraft.util;

import funskydev.pianocraft.block.MultiblockMainPart;
import net.minecraft.block.Block;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.*;

public enum MultiblockEnum {

    PIANO(new LinkedHashMap<>() {{
            put(BlockPosEnum.EAST,VoxelShapes.combineAndSimplify(Block.createCuboidShape(0,0,1,8,15,16),Block.createCuboidShape(0,15,9,8,16,16),BooleanBiFunction.OR));
            put(BlockPosEnum.WEST,VoxelShapes.combineAndSimplify(Block.createCuboidShape(8,0,1,16,15,16),Block.createCuboidShape(8,15,9,16,16,16),BooleanBiFunction.OR));
            put(BlockPosEnum.TOP,Block.createCuboidShape(0,0,9,16,9,16));
            put(BlockPosEnum.TOP_EAST,Block.createCuboidShape(0,0,9,8,9,16));
            put(BlockPosEnum.TOP_WEST,Block.createCuboidShape(8,0,9,16,9,16));
    }}, VoxelShapes.combineAndSimplify(Block.createCuboidShape(0, 0, 1, 16, 15, 16), Block.createCuboidShape(0, 15, 9, 16, 16, 16), BooleanBiFunction.OR));

    private Map<BlockPosEnum, VoxelShape> blocks;
    private MultiblockMainPart mainBlock;
    private VoxelShape mainBlockShape = VoxelShapes.fullCube();

    MultiblockEnum(LinkedHashMap<BlockPosEnum, VoxelShape> blocks, VoxelShape mainBlockShape) {
        this.blocks = blocks;
        this.mainBlockShape = mainBlockShape;
    }

    MultiblockEnum(LinkedHashMap<BlockPosEnum, VoxelShape> blocks) {
        this.blocks = blocks;
    }

    MultiblockEnum(List<BlockPosEnum> blocks) {
        this.blocks = new LinkedHashMap<>();
        for (BlockPosEnum pos : blocks) this.blocks.put(pos, VoxelShapes.fullCube());
    }

    public Map<BlockPosEnum, VoxelShape> getBlocks() {
        return blocks;
    }

    public List<BlockPosEnum> getBlocksPosList() {
        return blocks.keySet().stream().toList();
    }

    public MultiblockMainPart getMainBlock() {
        return this.mainBlock;
    }

    public VoxelShape getMainBlockShape() {
        return this.mainBlockShape;
    }

    public VoxelShape getShapeForBlock(BlockPosEnum pos) {
        return blocks.get(pos);
    }

    public void setMainBlock(MultiblockMainPart block) {
        this.mainBlock = block;
    }

    public String getName(BlockPosEnum pos) {
        return name().toLowerCase() + "_mult_" + pos.name().toLowerCase();
    }

}
