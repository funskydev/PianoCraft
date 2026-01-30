package funskydev.pianocraft.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import funskydev.pianocraft.screen.PianoScreenHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(at = @At("HEAD"), method = "evaluateWhichHandsToRender", cancellable = true)
    private static void evaluateWhichHandsToRender(LocalPlayer player, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> info) {

        if (player.containerMenu instanceof PianoScreenHandler) info.setReturnValue(ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS);

    }

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void renderArmWithItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector nodeCollector, int light, CallbackInfo info) {

        if (hand != InteractionHand.MAIN_HAND && item.isEmpty() && player.containerMenu instanceof PianoScreenHandler) {

            matrices.pushPose();
            ((ItemInHandRenderer) (Object) this).renderPlayerArm(matrices, nodeCollector, light, equipProgress, swingProgress, player.getMainArm().getOpposite());
            matrices.popPose();
            info.cancel();
            
        }
    }

}
