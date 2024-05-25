package funskydev.pianocraft.mixin;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.block.PianoBlock;
import funskydev.pianocraft.util.NoteUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(at = @At("RETURN"), method = "playStepSounds")
    private void playStepSounds(BlockPos pos, BlockState state, CallbackInfo info) {

        Entity entity = (Entity) (Object) this;

        Block block = state.getBlock();

        if (block instanceof MultiblockPartBlock multiblockPartBlock) {

            if(multiblockPartBlock.getMultiblockPartPos().isTop()) return;
            block = multiblockPartBlock.getMainBlock();
        }

        if(block instanceof PianoBlock) {

            Random random = new Random();

            int note = random.nextInt(12);
            int octave = random.nextInt(3) + 3;
            float volume = random.nextFloat() + 0.5f;

            float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);

            entity.getWorld().playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundCategory.RECORDS, volume, pitch);
        }
    }

}
