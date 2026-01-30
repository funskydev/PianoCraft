package funskydev.pianocraft.block;

import com.mojang.serialization.MapCodec;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import funskydev.pianocraft.util.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MultiblockPartBlock extends HorizontalDirectionalBlock {

    private final BlockPosEnum multiblockPartPos;
    private final MultiblockMainPartBlock mainBlock;

    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;

    public MultiblockPartBlock(BlockBehaviour.Properties settings, BlockPosEnum pos, MultiblockMainPartBlock mainBlock, VoxelShape shape) {

        super(settings.pushReaction(PushReaction.DESTROY).noLootTable().noOcclusion().noTerrainParticles());

        this.multiblockPartPos = pos;
        this.mainBlock = mainBlock;
        this.northShape = shape;
        this.eastShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.EAST);
        this.southShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.SOUTH);
        this.westShape = VoxelShapeUtil.rotateBoundingBox(northShape, Direction.WEST);

    }

    // Behaviors
    
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return mainBlock.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);

        destroyMultiblock(level, pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        switch (state.getValue(FACING)) {
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(mainBlock);
    }

    @Override
    public MutableComponent getName() {
        return mainBlock.getName();
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        super.attack(state, world, pos, player);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        return super.playerWillDestroy(world, pos, state, player);
    }

    // Facing

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // Registry

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return this.mainBlock.codec();
    }


    // Multiblock part methods

    public void destroyMultiblock(Level world, BlockPos pos, BlockState state) {

        Direction facing = state.getValue(FACING);
        BlockPos targetPos = MultiblockUtil.getMainBlock(pos, multiblockPartPos, facing);

        if(world.getBlockState(targetPos).is(mainBlock)) mainBlock.destroyMultiblockParts(world, targetPos, state);

    }

    public BlockPosEnum getMultiblockPartPos() {
        return multiblockPartPos;
    }

    public Block getMainBlock() {
        return this.mainBlock;
    }

}
