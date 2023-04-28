package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PCItems {

    private static <T extends Item> T register(String name, T item) {

        PCMain.LOGGER.info("Registering item : " + name);
        return Registry.register(Registries.ITEM, new Identifier(PCMain.MOD_ID, name), item);

    }

    public static void registerItems(){
        PCMain.LOGGER.info("Items registered");
    }

}
