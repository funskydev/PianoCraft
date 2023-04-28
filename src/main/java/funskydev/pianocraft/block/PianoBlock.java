package funskydev.pianocraft.block;

import funskydev.pianocraft.util.MultiblockEnum;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PianoBlock extends MultiblockMainPart {

    public PianoBlock() {

        super(MultiblockSettings.of(Material.WOOD).strength(1.0f, 3.0f).nonOpaque().sounds(BlockSoundGroup.WOOD), MultiblockEnum.PIANO);

    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return super.isTransparent(state, world, pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hand, hit);
    }

}
