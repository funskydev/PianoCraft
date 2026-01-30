package funskydev.pianocraft.screen;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.registry.PCScreenHandlers;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PianoScreenHandler extends AbstractContainerMenu {

    private final Container inventory;
    private BlockPos pos;

    /*public PianoScreenHandler(int syncId) {
        this(syncId, new SimpleInventory(1));
    }*/

    public PianoScreenHandler(int syncId, Container inventory) {
        super(PCScreenHandlers.PIANO_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
    }

    public PianoScreenHandler(int syncId, Container inventory, BlockPos pos) {
        super(PCScreenHandlers.PIANO_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.pos = pos;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {

        if (player.getWorld() instanceof ServerLevel serverWorld) {

            NotesEnum note = NoteUtil.getNoteFromId(id);
            int octave = NoteUtil.getOctaveFromId(id);
            float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);

            serverWorld.playSound(player, player.blockPosition(), SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.RECORDS, 1.0f, pitch);

            return true;
        }

        return super.clickMenuButton(player, id);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    public ItemStack getSoundItemStack() {
        return this.inventory.getItem(0);
    }

    public BlockPos getPianoPos() {
        return this.pos;
    }

}
