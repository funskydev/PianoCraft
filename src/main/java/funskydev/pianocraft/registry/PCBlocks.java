package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.PianoBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PCBlocks {

    public static final Block PIANO = register("piano", new PianoBlock());

    private static <T extends Block> T register(String name, T block){
        PCMain.LOGGER.info("Registering block : " + name);
        return Registry.register(Registries.BLOCK, new Identifier(PCMain.MOD_ID, name), block);
    }

    public static void registerBlocks(){
        PCMain.LOGGER.info("Blocks registered");
    }

}