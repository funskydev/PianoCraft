package funskydev.pianocraft;

import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;

public class MultiblockItem extends BlockItem {

    private final MultiblockEnum multiblockType;

    public MultiblockItem(Block block, Settings settings, MultiblockEnum multiblockType) {

        super(block, settings);
        this.multiblockType = multiblockType;

    }

    @Override
    protected boolean canPlace(ItemPlacementContext ctx, BlockState state) {

        PCMain.LOGGER.info("MultiblockItem.canPlace() ctx.getBlockPos(): " + ctx.getBlockPos());

        // check for plants etc

        boolean result = MultiblockUtil.checkAround(ctx.getWorld(), MultiblockUtil.getMultBlocks(ctx.getBlockPos(), ctx.getHorizontalPlayerFacing().getOpposite(), multiblockType));
        return result ? super.canPlace(ctx, state) : false;

    }

}
