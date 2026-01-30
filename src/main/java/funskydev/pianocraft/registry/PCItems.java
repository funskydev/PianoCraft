package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class PCItems {

    private static <T extends Item> T register(String name, T item) {

        PCMain.LOGGER.debug("Registering item : " + name);
        return Registry.register(BuiltInRegistries.ITEM, new Identifier(PCMain.MOD_ID, name), item);

    }

    public static void registerItems(){
        PCMain.LOGGER.debug("Items registered");
    }

}
