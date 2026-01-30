package funskydev.pianocraft.util;

import funskydev.pianocraft.block.MultiblockMainPartBlock;
import java.util.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum MultiblockEnum {

    PIANO(new LinkedHashMap<>() {{
            put(BlockPosEnum.EAST,Shapes.join(Block.box(0,0,1,8,15,16),Block.box(0,15,9,8,16,16),BooleanOp.OR));
            put(BlockPosEnum.WEST,Shapes.join(Block.box(8,0,1,16,15,16),Block.box(8,15,9,16,16,16),BooleanOp.OR));
            put(BlockPosEnum.TOP,Block.box(0,0,9,16,9,16));
            put(BlockPosEnum.TOP_EAST,Block.box(0,0,9,8,9,16));
            put(BlockPosEnum.TOP_WEST,Block.box(8,0,9,16,9,16));
    }}, Shapes.join(Block.box(0, 0, 1, 16, 15, 16), Block.box(0, 15, 9, 16, 16, 16), BooleanOp.OR));

    private Map<BlockPosEnum, VoxelShape> blocks;
    private MultiblockMainPartBlock mainBlock;
    private VoxelShape mainBlockShape = Shapes.block();

    MultiblockEnum(LinkedHashMap<BlockPosEnum, VoxelShape> blocks, VoxelShape mainBlockShape) {
        this.blocks = blocks;
        this.mainBlockShape = mainBlockShape;
    }

    MultiblockEnum(LinkedHashMap<BlockPosEnum, VoxelShape> blocks) {
        this.blocks = blocks;
    }

    MultiblockEnum(List<BlockPosEnum> blocks) {
        this.blocks = new LinkedHashMap<>();
        for (BlockPosEnum pos : blocks) this.blocks.put(pos, Shapes.block());
    }

    public Map<BlockPosEnum, VoxelShape> getBlocks() {
        return blocks;
    }

    public List<BlockPosEnum> getBlocksPosList() {
        return blocks.keySet().stream().toList();
    }

    public MultiblockMainPartBlock getMainBlock() {
        return this.mainBlock;
    }

    public VoxelShape getMainBlockShape() {
        return this.mainBlockShape;
    }

    public VoxelShape getShapeForBlock(BlockPosEnum pos) {
        return blocks.get(pos);
    }

    public void setMainBlock(MultiblockMainPartBlock block) {
        this.mainBlock = block;
    }

    public String getName(BlockPosEnum pos) {
        return name().toLowerCase() + "_mult_" + pos.name().toLowerCase();
    }

}
