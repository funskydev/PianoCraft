package funskydev.pianocraft.mixin.client;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class SoundSystemMixin {

    @Inject(at = @At("RETURN"), method = "calculatePitch", cancellable = true)
    private void getAdjustedPitch(SoundInstance sound, CallbackInfoReturnable<Float> info) {
        // Allow to have a larger pitch range
        info.setReturnValue(Mth.clamp(sound.getPitch(), 0.05f, 10.0f));
    }

}
