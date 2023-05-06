package funskydev.pianocraft.block;

import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockEnum;
import funskydev.pianocraft.util.MultiblockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PianoBlock extends MultiblockMainPartBlock {

    private static Text TITLE = Text.translatable("container.pianocraft.piano");

    public PianoBlock() {
        super(MultiblockSettings.of(Material.WOOD).strength(1.0f, 3.0f).nonOpaque().sounds(BlockSoundGroup.WOOD), MultiblockEnum.PIANO);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return super.isTransparent(state, world, pos);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        BlockState mainBlockState = state;
        BlockPos mainBlockPos = pos;

        // If the block is a part of a multiblock, get the main block state and position
        if (state.getBlock() instanceof MultiblockPartBlock multiblockPart) {

            BlockPosEnum multiblockPartPos = multiblockPart.getMultiblockPartPos();

            if (multiblockPartPos.isTop()) return ActionResult.PASS;

            mainBlockPos = MultiblockUtil.getMainBlock(pos, multiblockPartPos, state.get(FACING));
            mainBlockState = world.getBlockState(mainBlockPos);

        }

        if (hit.getSide() == mainBlockState.get(FACING) || hit.getSide() == Direction.UP) {

            if (world.isClient) {
                MinecraftClient.getInstance().gameRenderer.firstPersonRenderer.resetEquipProgress(Hand.OFF_HAND);
                return ActionResult.SUCCESS;
            }

            player.openHandledScreen(mainBlockState.createScreenHandlerFactory(world, mainBlockPos));
            // piano stat ?
            return ActionResult.CONSUME;

        }

        return ActionResult.PASS;

    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                ((syncId, inventory, player) -> {
                    return new PianoScreenHandler(syncId, inventory, pos);
                }),
                TITLE
        );
    }
}
