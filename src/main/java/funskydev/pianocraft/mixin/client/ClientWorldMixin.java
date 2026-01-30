package funskydev.pianocraft.mixin.client;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.util.MultiblockUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.security.auth.callback.Callback;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin {

    @Inject(at = @At("RETURN"), method = "destroyBlockProgress")
    private void setBlockBreakingInfo(int entityId, BlockPos pos, int progress, CallbackInfo info) {

        ClientLevel world = (ClientLevel) (Object) this;

        if (world.getBlockState(pos).getBlock() instanceof MultiblockPartBlock multiblockPartBlock) {

            world.destroyBlockProgress(
                    entityId,
                    MultiblockUtil.getMainBlock(
                            pos,
                            multiblockPartBlock.getMultiblockPartPos(),
                            world.getBlockState(pos).getValue(HorizontalDirectionalBlock.FACING)
                    ),
                    progress
            );

        }

    }

}
