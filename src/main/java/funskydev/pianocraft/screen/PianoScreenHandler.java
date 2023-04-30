package funskydev.pianocraft.screen;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.registry.PCScreenHandlers;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class PianoScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public PianoScreenHandler(int syncId) {
        this(syncId, new SimpleInventory(1));
    }

    public PianoScreenHandler(int syncId, Inventory inventory) {

        super(PCScreenHandlers.PIANO_SCREEN_HANDLER, syncId);
        this.inventory = inventory;

    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {

        if (player.world instanceof ServerWorld serverWorld) {

            NotesEnum note = NoteUtil.getNoteFromId(id);
            int octave = NoteUtil.getOctaveFromId(id);
            float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);

            serverWorld.playSound(player, player.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundCategory.RECORDS, 1.0f, pitch);

            PCMain.LOGGER.info("Note played on server (note: " + note.getNoteName() + ", octave: " + octave + ", pitch: " + pitch + ")");

            return true;
        }

        return super.onButtonClick(player, id);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack getSoundItemStack() {
        return this.inventory.getStack(0);
    }

}
