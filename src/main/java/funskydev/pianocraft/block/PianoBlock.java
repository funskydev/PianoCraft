package funskydev.pianocraft.block;

import com.mojang.serialization.MapCodec;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import funskydev.pianocraft.util.NoteUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PianoBlock extends MultiblockMainPartBlock {

    public static final MapCodec<PianoBlock> CODEC = PianoBlock.simpleCodec(PianoBlock::new);

    private static final Component CONTAINER_TITLE = Component.translatable("container.pianocraft.piano");

    public PianoBlock(BlockBehaviour.Properties settings) {
        super(settings
                .mapColor(Blocks.SPRUCE_PLANKS.defaultMapColor())
                .strength(1.0f, 3.0f)
                .noOcclusion()
                .sound(SoundType.WOOD),
                MultiblockEnum.PIANO);
    }

    // Behavior

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {

        BlockState mainBlockState = state;
        BlockPos mainBlockPos = pos;

        // If the block is a part of a multiblock, get the main block state and position
        if (state.getBlock() instanceof MultiblockPartBlock multiblockPart) {

            BlockPosEnum multiblockPartPos = multiblockPart.getMultiblockPartPos();

            if (multiblockPartPos.isTop()) return InteractionResult.PASS;

            mainBlockPos = MultiblockUtil.getMainBlock(pos, multiblockPartPos, state.getValue(FACING));
            mainBlockState = world.getBlockState(mainBlockPos);
        }

        if (!(mainBlockState.getBlock() instanceof MultiblockMainPartBlock)) return InteractionResult.PASS;

        if (hit.getDirection() == mainBlockState.getValue(FACING) || hit.getDirection() == Direction.UP) {

            if (world.isClientSide) {
                Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.OFF_HAND);
                return InteractionResult.SUCCESS;
            }

            player.openMenu(mainBlockState.getMenuProvider(world, mainBlockPos));
            // piano stat ?
            return InteractionResult.CONSUME;

        }

        return InteractionResult.PASS;

    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return new SimpleMenuProvider(
                ((syncId, inventory, player) -> new PianoScreenHandler(syncId, inventory, pos)),
                CONTAINER_TITLE
        );
    }

    // Rendering

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0f;
    }

    // Registry

    @Override
    public MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

}
