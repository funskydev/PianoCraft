package funskydev.pianocraft.mixin.client;

import funskydev.pianocraft.screen.PianoScreenHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(at = @At("HEAD"), method = "getHandRenderType", cancellable = true)
    private static void getHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> info) {

        if (player.currentScreenHandler instanceof PianoScreenHandler) info.setReturnValue(HeldItemRenderer.HandRenderType.RENDER_BOTH_HANDS);

    }

    @Inject(at = @At("HEAD"), method = "renderFirstPersonItem", cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {

        if (hand != Hand.MAIN_HAND && item.isEmpty() && player.currentScreenHandler instanceof PianoScreenHandler) {

            matrices.push();
            ((HeldItemRenderer) (Object) this).renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, player.getMainArm().getOpposite());
            matrices.pop();
            info.cancel();
            
        }
    }

}
