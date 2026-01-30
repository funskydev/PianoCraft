package funskydev.pianocraft.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class MidiDeviceSelectorButton extends Button.Plain {

    protected MidiDeviceSelectorButton(int x, int y, int height, int width, Component component, OnPress onPress) {
        super(x, y, height, width, component, onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    public boolean shouldTakeFocusAfterInteraction() {
        return false;
    }
}
