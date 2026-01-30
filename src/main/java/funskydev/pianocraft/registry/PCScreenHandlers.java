package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.screen.PianoScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class PCScreenHandlers {

    public static final MenuType<PianoScreenHandler> PIANO_SCREEN_HANDLER = registerScreenHandler("piano", PianoScreenHandler::new);

    private static <S extends AbstractContainerMenu> MenuType<S> registerScreenHandler(String name, MenuType.MenuSupplier<S> screenHandlerFactory) {
        return Registry.register(BuiltInRegistries.MENU,
                new Identifier(PCMain.MOD_ID, "piano"),
                new MenuType<>(screenHandlerFactory,FeatureFlags.VANILLA_SET));
    }

    public static void registerScreenHandlers() {
        PCMain.LOGGER.debug("Screen handlers registered");
    }

}
