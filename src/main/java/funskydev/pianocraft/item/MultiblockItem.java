package funskydev.pianocraft.item;

import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockItem extends BlockItem {

    private final MultiblockEnum multiblockType;

    public MultiblockItem(Block block, Properties settings, MultiblockEnum multiblockType) {

        super(block, settings);
        this.multiblockType = multiblockType;

    }

    @Override
    protected boolean canPlace(BlockPlaceContext ctx, BlockState state) {

        // TODO: check for plants etc

        boolean result = MultiblockUtil.checkAround(ctx.getLevel(), MultiblockUtil.getMultBlocks(ctx.getClickedPos(), ctx.getHorizontalDirection().getOpposite(), multiblockType));
        return result && super.canPlace(ctx, state);

    }

}
