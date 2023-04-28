package funskydev.pianocraft.util;

import funskydev.pianocraft.block.MultiblockMainPart;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

import java.util.Arrays;
import java.util.List;

public enum MultiblockEnum {

    PIANO(Arrays.asList(BlockPosEnum.EAST, BlockPosEnum.WEST, BlockPosEnum.TOP, BlockPosEnum.TOP_EAST, BlockPosEnum.TOP_WEST));

    private List<BlockPosEnum> blocksPos;
    private MultiblockMainPart mainBlock;

    MultiblockEnum(List<BlockPosEnum> pos) {
        this.blocksPos = pos;
    }

    public List<BlockPosEnum> getBlocksPos() {
        return blocksPos;
    }

    public MultiblockMainPart getMainBlock() {
        return this.mainBlock;
    }

    public void setMainBlock(MultiblockMainPart block) {
        this.mainBlock = block;
    }

    public String getName(BlockPosEnum pos) {
        return name().toLowerCase() + "_mult_" + pos.name().toLowerCase();
    }

}
