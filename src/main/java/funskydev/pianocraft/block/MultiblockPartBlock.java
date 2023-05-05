package funskydev.pianocraft.block;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import funskydev.pianocraft.util.VoxelShapeUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class MultiblockPartBlock extends HorizontalFacingBlock {

    private BlockPosEnum multiblockPartPos;
    private MultiblockMainPart mainBlock;

    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;

    public MultiblockPartBlock(BlockPosEnum pos, MultiblockMainPart mainBlock, VoxelShape shape) {

        super(FabricBlockSettings.copyOf(mainBlock).dropsNothing().nonOpaque().noBlockBreakParticles());

        this.multiblockPartPos = pos;
        this.mainBlock = mainBlock;
        this.northShape = shape;
        this.eastShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.EAST);
        this.southShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.SOUTH);
        this.westShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.WEST);

    }

    // Behaviors
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return mainBlock.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (!state.isOf(newState.getBlock())) {
            destroyMultiblock(world, pos, state);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        switch (state.get(FACING)) {
            case EAST:
                return eastShape;
            case SOUTH:
                return southShape;
            case WEST:
                return westShape;
            default:
                return northShape;
        }

    }

    // Rendering

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
    public MutableText getName() {
        return mainBlock.getName();
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        PCMain.LOGGER.info("MultiblockPartBlock.getStateForNeighborUpdate()");
        PCMain.LOGGER.info("direction: " + direction);
        PCMain.LOGGER.info("neighborState: " + neighborState.getBlock().getName().getString());

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    // Facing

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // Multiblock part methods

    public void destroyMultiblock(World world, BlockPos pos, BlockState state) {

        Direction facing = state.get(FACING);
        BlockPos targetPos = MultiblockUtil.getMainBlock(pos, multiblockPartPos, facing);

        if(world.getBlockState(targetPos).isOf(mainBlock)) mainBlock.destroyMultiblockParts(world, targetPos, state);

    }

    public BlockPosEnum getMultiblockPartPos() {
        return multiblockPartPos;
    }

}
