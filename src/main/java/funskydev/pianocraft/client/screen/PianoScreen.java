package funskydev.pianocraft.client.screen;

import com.google.common.collect.Lists;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.screen.widgets.PianoKeyWidget;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class PianoScreen extends HandledScreen<PianoScreenHandler> {

    static final Identifier TEXTURE = new Identifier(PCMain.MOD_ID, "textures/gui/piano.png");

    private final List<ClickableWidget> buttons = Lists.newArrayList();

    public PianoScreen(PianoScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 25;
        this.backgroundHeight = 219;

        handler.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                PCMain.LOGGER.info("Slot updated (slotId: " + slotId + ")");
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                PCMain.LOGGER.info("Property updated (property: " + property + ", value: " + value + ")");
            }
        });
    }

    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();

        for (NotesEnum note : NotesEnum.values()) {

            //this.addButton(new PianoKeyWidget(this.titleX + 10 + (note.ordinal() * 40), this.titleY + 10, note, 4));

        }

        /*this.addButton(new PressableWidget(this.titleX, this.titleY, 40, 40, Text.of("test")) {
            @Override
            protected void appendClickableNarrations(NarrationMessageBuilder builder) {

            }

            @Override
            public void onPress() {
                PCMain.LOGGER.info("Button pressed");

                sendButtonPressPacket(0);
            }
        });*/
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        //super.drawForeground(matrices, mouseX, mouseY);
        this.textRenderer.draw(matrices, Text.of("Piano"), 0, 0, 0xffffff);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        InputUtil.Key pressedKey = InputUtil.fromKeyCode(keyCode, scanCode);

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }

        for(KeysEnum key : KeysEnum.values()) {
            if (pressedKey.getTranslationKey().equals(key.getTranslationKey())) {
                playNote(key.getNote(), key.getOctave());
            }
        }

        return true;
    }

    private void playNote(NotesEnum note, int octave) {
        playNote(note, octave, 1.0f);
    }

    private void playNote(NotesEnum note, int octave, float volume) {
        playNoteOnClient(note, octave, volume);
        sendNoteToServer(note, octave);
    }

    private void playNoteOnClient(NotesEnum note, int octave, float volume) {

        float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);
        client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), volume, pitch);

        PCMain.LOGGER.info("Note played on client (note: " + note.getNoteName() + ", octave: " + octave + ", pitch: " + pitch + ")");

    }

    private void sendNoteToServer(NotesEnum note, int octave) {
        sendButtonPressPacket(NoteUtil.convertNoteAndOctaveToId(note, octave));
    }

    private void sendButtonPressPacket(int id) {
        this.client.interactionManager.clickButton(this.handler.syncId, id);
    }

}
