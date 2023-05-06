package funskydev.pianocraft.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.PCMainClient;
import funskydev.pianocraft.client.screen.widgets.PianoKeyWidget;
import funskydev.pianocraft.network.PCPackets;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class PianoScreen extends HandledScreen<PianoScreenHandler> {

    static final Identifier TEXTURE = new Identifier(PCMain.MOD_ID, "textures/gui/piano.png");

    private PressableWidget midiDeviceButton;

    public PianoScreen(PianoScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(new TextWidget(10, 10, 120, 0, Text.of("Piano"), this.textRenderer));

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

        NotesEnum[] notes = NotesEnum.values();

        for (int i = 0; i < 12; i++) {

            KeysEnum key = KeysEnum.fromNoteAndOctave(notes[i], 3);
            InputUtil.Key keyboardKey = InputUtil.fromTranslationKey(key.getTranslationKey());

            boolean isSharp = notes[i].isSharp();
            // add the same amount of pixels to the x position except between E and F and B and C

            int spacing = 9;
            int x = 11 + (i * spacing) + (i > 4 ? spacing : 0) + (i > 11 ? spacing : 0);

            if (i == 1) x -= 2;
            if (i == 3) x += 2;
            if (i == 6) x -= 3;
            if (i == 10) x += 3;

            if (i > 4) x++;

            int y = isSharp ? 73 : 95;

            TextWidget keyWidget = new TextWidget(x, y, 10, 10, Text.of(keyboardKey.getLocalizedText().getString().toUpperCase()), this.textRenderer);
            keyWidget.setTextColor(0xFFAA00);
            keyWidget.setTooltip(Tooltip.of(Text.of(key.getNote().getNoteName())));
            this.addDrawableChild(keyWidget);

        }

        PCMainClient.ensureCurrentMidiDeviceIsAvailableOrSetToDefault();
        updateMidiDeviceButtonText();

    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
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
        //sendButtonPressPacket(NoteUtil.convertNoteAndOctaveToId(note, octave));

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(note.ordinal());
        buf.writeInt(octave);
        buf.writeFloat(volume);

        ClientPlayNetworking.send(PCPackets.KEY_PRESSED_PACKET_ID, buf);
    }

    private void sendButtonPressPacket(int id) {
        this.client.interactionManager.clickButton(this.handler.syncId, id);
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

}
