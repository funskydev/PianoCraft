package funskydev.pianocraft.block;

import com.mojang.serialization.MapCodec;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import funskydev.pianocraft.util.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class MultiblockMainPartBlock extends HorizontalDirectionalBlock {

    private final MultiblockEnum multiblockType;

    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;

    protected MultiblockMainPartBlock(BlockBehaviour.Properties settings, MultiblockEnum multiblockType) {

        super(settings.pushReaction(PushReaction.BLOCK));

        this.multiblockType = multiblockType;
        this.northShape = multiblockType.getMainBlockShape();
        this.eastShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.EAST);
        this.southShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.SOUTH);
        this.westShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.WEST);

        multiblockType.setMainBlock(this);

    }

    // Behaviors

    @Override
    public abstract InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit);

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);

        if (!world.isClientSide()) placeMultiblockParts(world, pos, state);
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);

        PCMain.LOGGER.info("Blockstate MAIN removed: " + state);
        PCMain.LOGGER.info("Blockstate at this pos: " + level.getBlockState(pos));

        //if (!state.is(newState.getBlock())) destroyMultiblockParts(world, pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        return switch (state.getValue(FACING)) {
            case EAST -> eastShape;
            case SOUTH -> southShape;
            case WEST -> westShape;
            default -> northShape;
        };

    }

    // Facing

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // Registry

    @Override
    public abstract MapCodec<? extends HorizontalDirectionalBlock> codec();

    // Multiblock main part methods

    public void placeMultiblockParts(Level world, BlockPos pos, BlockState state) {

        Direction facing = state.getValue(FACING);
        MultiblockUtil.placeBlockMap(world, MultiblockUtil.getMultBlocks(pos, facing, multiblockType), facing, multiblockType);

    }

    public void destroyMultiblockParts(Level world, BlockPos pos, BlockState state) {

        world.destroyBlock(pos, true);

        Direction facing = state.getValue(FACING);
        MultiblockUtil.attemptDestruction(world, MultiblockUtil.getMultBlocks(pos, facing, multiblockType));

    }

    public MultiblockEnum getMultiblockType() {
        return this.multiblockType;
    }

}
