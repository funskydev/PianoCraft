package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.screen.PianoScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class PCScreenHandlers {

    public static final ScreenHandlerType<PianoScreenHandler> PIANO_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(PCMain.MOD_ID, "piano"), PianoScreenHandler::new);

    public static void registerScreenHandlers() {

    }

}
