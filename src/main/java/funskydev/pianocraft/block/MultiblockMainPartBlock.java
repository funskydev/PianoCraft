package funskydev.pianocraft.block;

import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import funskydev.pianocraft.util.VoxelShapeUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MultiblockMainPartBlock extends HorizontalFacingBlock {

    private final MultiblockEnum multiblockType;

    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;

    protected MultiblockMainPartBlock(FabricBlockSettings settings, MultiblockEnum multiblockType) {

        super(settings);

        this.multiblockType = multiblockType;
        this.northShape = multiblockType.getMainBlockShape();
        this.eastShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.EAST);
        this.southShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.SOUTH);
        this.westShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.WEST);

        multiblockType.setMainBlock(this);

    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient()) placeMultiblockParts(world, pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (!state.isOf(newState.getBlock())) destroyMultiblockParts(world, pos, state);
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

    public void placeMultiblockParts(World world, BlockPos pos, BlockState state) {

        Direction facing = state.get(FACING);
        MultiblockUtil.placeBlockMap(world, MultiblockUtil.getMultBlocks(pos, facing, multiblockType), facing, multiblockType);

    }

    public void destroyMultiblockParts(World world, BlockPos pos, BlockState state) {

        world.breakBlock(pos, true);

        Direction facing = state.get(FACING);
        MultiblockUtil.attemptDestruction(world, MultiblockUtil.getMultBlocks(pos, facing, multiblockType));

    }

    public MultiblockEnum getMultiblockType() {
        return this.multiblockType;
    }

}
