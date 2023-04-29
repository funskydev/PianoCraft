package funskydev.pianocraft.mixin.client;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {

    @Inject(at = @At("RETURN"), method = "getAdjustedPitch", cancellable = true)
    private void getAdjustedPitch(SoundInstance sound, CallbackInfoReturnable<Float> info) {
        info.setReturnValue(MathHelper.clamp(sound.getPitch(), 0.05f, 10.0f));
    }

}
