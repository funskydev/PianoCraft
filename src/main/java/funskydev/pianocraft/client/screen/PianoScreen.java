package funskydev.pianocraft.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.PCMainClient;
import funskydev.pianocraft.network.PianoKeyPressedPayload;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PianoScreen extends AbstractContainerScreen<PianoScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(PCMain.MOD_ID, "textures/gui/piano.png");

    private AbstractButton midiDeviceButton;
    private List<StringWidget> keyWidgets = new ArrayList<>();
    private StringWidget hideKeysText;
    private StringWidget arrowsText;

    private int octave = 3;
    private boolean showKeybindings;

    public PianoScreen(PianoScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.keyWidgets.clear();

        this.addRenderableWidget(new StringWidget(10, 10, 120, 0, Component.nullToEmpty("Piano menu"), this.font));

        this.midiDeviceButton = new AbstractButton(10, 20, 120, 20, Component.nullToEmpty("Unknown")) {
            @Override
            protected void updateWidgetNarration(NarrationElementOutput builder) {

            }

            @Override
            public void onPress() {
                midiDeviceButtonPressed();
            }
        };

        this.addRenderableWidget(this.midiDeviceButton);

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

            StringWidget keyWidget = new StringWidget(x, y, 10, 10, Component.nullToEmpty(""), this.font);
            keyWidget.setTextColor(0xFFAA00);

            this.addRenderableWidget(keyWidget);
            this.keyWidgets.add(keyWidget);

        }

        this.hideKeysText = new StringWidget(10, 50, 120, 0, Component.nullToEmpty(""), this.font);
        this.addRenderableWidget(this.hideKeysText);

        this.arrowsText = new StringWidget(10, 118, 120, 0, Component.nullToEmpty("Arrows - Change octave"), this.font);
        this.addRenderableWidget(this.arrowsText);

        PCMainClient.searchForMidiDeviceIfNoneSelected();
        PCMainClient.ensureCurrentMidiDeviceIsAvailableAndReady();

        updateMidiDeviceButtonText();
        updateKeybindingsMenu();

    }

    @Override
    public void onClose() {
        super.onClose();

        PCMainClient.closeCurrentMidiDevice();
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        if (!showKeybindings) return;
        context.blit(TEXTURE, 6, 45, 0, 0, 0, 128, 64, 128, 64);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        InputConstants.Key pressedKey = InputConstants.getKey(keyCode, scanCode);

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_TAB) {
            toggleMenu();
            return true;
        }

        if (showKeybindings) {

            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                previousOctave();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                nextOctave();
                return true;
            }

        }

        for(KeysEnum key : KeysEnum.values()) {
            if (pressedKey.getName().equals(key.getTranslationKey())) {
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
        minecraft.player.playSound(SoundEvents.NOTE_BLOCK_HARP.value(), volume, pitch);
        minecraft.player.swing(InteractionHand.values()[minecraft.player.getRandom().nextInt(InteractionHand.values().length)]);

    }

    private void sendNoteToServer(NotesEnum note, int octave, float volume) {

        ClientPlayNetworking.send(new PianoKeyPressedPayload(note.ordinal(), octave, volume));

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

        this.midiDeviceButton.setMessage(Component.nullToEmpty(currentMidiDeviceName));
        this.midiDeviceButton.setTooltip(Tooltip.create(Component.nullToEmpty(tooltipText)));

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

            InputConstants.Key keyboardKey = InputConstants.getKey(key.getTranslationKey());

            this.keyWidgets.get(i).setMessage(Component.nullToEmpty(keyboardKey.getDisplayName().getString().toUpperCase()));
            this.keyWidgets.get(i).setTooltip(Tooltip.create(Component.nullToEmpty(NotesEnum.getNote(i).getNoteName() + octave)));

        }

    }

    private void updateInfoTextWidget() {

        if (showKeybindings) {
            this.hideKeysText.setMessage(Component.nullToEmpty("TAB - Hide keybindings"));
            this.hideKeysText.setY(130);
        } else {
            this.hideKeysText.setMessage(Component.nullToEmpty("TAB - Show keybindings"));
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
