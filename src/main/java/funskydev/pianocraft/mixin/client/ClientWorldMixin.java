package funskydev.pianocraft.mixin.client;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.util.MultiblockUtil;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.security.auth.callback.Callback;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Inject(at = @At("RETURN"), method = "setBlockBreakingInfo")
    private void setBlockBreakingInfo(int entityId, BlockPos pos, int progress, CallbackInfo info) {

        ClientWorld world = (ClientWorld) (Object) this;

        if (world.getBlockState(pos).getBlock() instanceof MultiblockPartBlock multiblockPartBlock) {

            world.setBlockBreakingInfo(
                    entityId,
                    MultiblockUtil.getMainBlock(
                            pos,
                            multiblockPartBlock.getMultiblockPartPos(),
                            world.getBlockState(pos).get(HorizontalFacingBlock.FACING)
                    ),
                    progress
            );

        }

    }

}
