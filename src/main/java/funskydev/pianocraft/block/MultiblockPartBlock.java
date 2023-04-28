package funskydev.pianocraft.block;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class MultiblockPartBlock extends HorizontalFacingBlock {

    private BlockPosEnum multiblockPos;
    private MultiblockMainPart mainBlock;
    private VoxelShape bounding;

    public MultiblockPartBlock(BlockPosEnum pos, MultiblockMainPart mainBlock, VoxelShape bounding) {

        super(FabricBlockSettings.copyOf(mainBlock).dropsNothing().nonOpaque().noBlockBreakParticles());

        this.multiblockPos = pos;
        this.mainBlock = mainBlock;
        this.bounding = bounding;

    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (!state.isOf(newState.getBlock())) {
            destroyMultiblock(world, pos, state);
        }
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(mainBlock);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        PCMain.LOGGER.info("MultiblockPartBlock.getStateForNeighborUpdate()");
        PCMain.LOGGER.info("direction: " + direction);
        PCMain.LOGGER.info("neighborState: " + neighborState.getBlock().getName().getString());

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public void destroyMultiblock(World world, BlockPos pos, BlockState state) {

        Direction facing = state.get(FACING);
        BlockPos targetPos = MultiblockUtil.getMainBlock(pos, multiblockPos, facing);

        if(world.getBlockState(targetPos).isOf(mainBlock)) mainBlock.destroyMultiblockParts(world, targetPos, state);

    }

}
