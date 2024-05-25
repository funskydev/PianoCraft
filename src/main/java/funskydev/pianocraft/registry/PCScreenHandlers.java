package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.screen.PianoScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class PCScreenHandlers {

    public static final ScreenHandlerType<PianoScreenHandler> PIANO_SCREEN_HANDLER = registerScreenHandler("piano", PianoScreenHandler::new);

    private static <S extends ScreenHandler> ScreenHandlerType<S> registerScreenHandler(String name, ScreenHandlerType.Factory<S> screenHandlerFactory) {
        return Registry.register(Registries.SCREEN_HANDLER,
                new Identifier(PCMain.MOD_ID, "piano"),
                new ScreenHandlerType<>(screenHandlerFactory,FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerScreenHandlers() {
        PCMain.LOGGER.debug("Screen handlers registered");
    }

}
