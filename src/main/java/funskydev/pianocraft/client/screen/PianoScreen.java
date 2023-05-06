package funskydev.pianocraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.PCMainClient;
import funskydev.pianocraft.network.PCPackets;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PianoScreen extends HandledScreen<PianoScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(PCMain.MOD_ID, "textures/gui/piano.png");

    private PressableWidget midiDeviceButton;
    private List<TextWidget> keyWidgets = new ArrayList<>();
    private TextWidget hideKeysText;
    private TextWidget arrowsText;

    private int octave = 3;
    private boolean showKeybindings;

    public PianoScreen(PianoScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.keyWidgets.clear();

        this.addDrawableChild(new TextWidget(10, 10, 120, 0, Text.of("Piano menu"), this.textRenderer));

        this.midiDeviceButton = new PressableWidget(10, 20, 120, 20, Text.of("Unknown")) {
            @Override
            protected void appendClickableNarrations(NarrationMessageBuilder builder) {

            }

            @Override
            public void onPress() {
                midiDeviceButtonPressed();
            }
        };

        this.addDrawableChild(this.midiDeviceButton);

        for (int i = 0; i < 12; i++) {

            boolean isSharp = NotesEnum.getNote(i).isSharp();

            // Keys spacing
            int spacing = 9;
            int x = 11 + (i * spacing) + (i > 4 ? spacing : 0) + (i > 11 ? spacing : 0);

            // Special spacing
            if (i == 1) x -= 2;
            if (i == 3) x += 2;
            if (i == 6) x -= 3;
            if (i == 10) x += 3;

            if (i > 4) x++;

            int y = isSharp ? 73 : 95;

            TextWidget keyWidget = new TextWidget(x, y, 10, 10, Text.of(""), this.textRenderer);
            keyWidget.setTextColor(0xFFAA00);

            this.addDrawableChild(keyWidget);
            this.keyWidgets.add(keyWidget);

        }

        this.hideKeysText = new TextWidget(10, 50, 120, 0, Text.of(""), this.textRenderer);
        this.addDrawableChild(this.hideKeysText);

        this.arrowsText = new TextWidget(10, 118, 120, 0, Text.of("Arrows - Change octave"), this.textRenderer);
        this.addDrawableChild(this.arrowsText);

        PCMainClient.ensureCurrentMidiDeviceIsAvailableOrSetToDefault();
        updateMidiDeviceButtonText();
        updateKeybindingsMenu();

    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        if (!showKeybindings) return;
        RenderSystem.setShaderTexture(0, TEXTURE);
        DrawableHelper.drawTexture(matrices, 6, 45, 0, 0, 0, 128, 64, 128, 64);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        InputUtil.Key pressedKey = InputUtil.fromKeyCode(keyCode, scanCode);

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_TAB) {
            toggleMenu();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            previousOctave();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            nextOctave();
            return true;
        }

        for(KeysEnum key : KeysEnum.values()) {
            if (pressedKey.getTranslationKey().equals(key.getTranslationKey())) {
                playNote(key.getNote(), key.getOctave());
            }
        }

        return true;
    }

    public void playNote(NotesEnum note, int octave) {
        playNote(note, octave, 1.0f);
    }

    public void playNote(NotesEnum note, int octave, float volume) {
        playNoteOnClient(note, octave, volume);
        sendNoteToServer(note, octave, volume);
    }

    private void playNoteOnClient(NotesEnum note, int octave, float volume) {

        float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);
        client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), volume, pitch);
        client.player.swingHand(Hand.values()[client.player.getRandom().nextInt(Hand.values().length)]);

    }

    private void sendNoteToServer(NotesEnum note, int octave, float volume) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(note.ordinal());
        buf.writeInt(octave);
        buf.writeFloat(volume);

        ClientPlayNetworking.send(PCPackets.KEY_PRESSED_PACKET_ID, buf);
    }

    private void midiDeviceButtonPressed() {
        PCMainClient.selectNextMidiDevice();
        updateMidiDeviceButtonText();
    }

    private void updateMidiDeviceButtonText() {

        String currentMidiDeviceName = PCMainClient.getCurrentMidiDeviceName();
        String tooltipText = "Click to switch to the next MIDI device";

        if (currentMidiDeviceName == null) {
            currentMidiDeviceName = "No MIDI device found";
            tooltipText = "Click to refresh MIDI devices";
        }

        this.midiDeviceButton.setMessage(Text.of(currentMidiDeviceName));
        this.midiDeviceButton.setTooltip(Tooltip.of(Text.of(tooltipText)));

    }

    private void updateKeyWidgets() {

        for (int i = 0; i < this.keyWidgets.size(); i++) {

            if (showKeybindings) this.keyWidgets.get(i).visible = true;
            else this.keyWidgets.get(i).visible = false;

            NotesEnum note = NotesEnum.getNote(i);

            KeysEnum key = KeysEnum.fromNoteAndOctave(note, this.octave);

            if (key == null) {
                this.keyWidgets.get(i).visible = false;
                continue;
            }

            InputUtil.Key keyboardKey = InputUtil.fromTranslationKey(key.getTranslationKey());

            this.keyWidgets.get(i).setMessage(Text.of(keyboardKey.getLocalizedText().getString().toUpperCase()));
            this.keyWidgets.get(i).setTooltip(Tooltip.of(Text.of(NotesEnum.getNote(i).getNoteName() + octave)));

        }

    }

    private void updateInfoTextWidget() {

        if (showKeybindings) {
            this.hideKeysText.setMessage(Text.of("TAB - Hide keybindings"));
            this.hideKeysText.setY(130);
        } else {
            this.hideKeysText.setMessage(Text.of("TAB - Show keybindings"));
            this.hideKeysText.setY(50);
        }

    }

    private void updateArrowsTextWidget() {

        this.arrowsText.visible = showKeybindings;

    }

    private void updateKeybindingsMenu() {
        updateKeyWidgets();
        updateInfoTextWidget();
        updateArrowsTextWidget();
    }

    private void toggleMenu() {
        this.showKeybindings = !this.showKeybindings;
        updateKeybindingsMenu();
    }

    private void previousOctave() {
        this.octave--;
        if (this.octave < 3) this.octave = 3;
        updateKeyWidgets();
    }

    private void nextOctave() {
        this.octave++;
        if (this.octave > 5) this.octave = 5;
        updateKeyWidgets();
    }

}
