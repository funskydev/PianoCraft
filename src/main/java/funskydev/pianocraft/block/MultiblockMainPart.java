package funskydev.pianocraft.block;

import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MultiblockMainPart extends HorizontalFacingBlock {

    private final MultiblockEnum multiblockType;

    protected MultiblockMainPart(FabricBlockSettings settings, MultiblockEnum multiblockType) {

        super(settings);
        this.multiblockType = multiblockType;
        multiblockType.setMainBlock(this);

    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
