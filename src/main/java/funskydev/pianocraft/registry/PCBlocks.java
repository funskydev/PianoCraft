package funskydev.pianocraft.registry;

import funskydev.pianocraft.item.MultiblockItem;
import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockMainPartBlock;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.block.PianoBlock;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockEnum;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class PCBlocks {

    public static final MultiblockMainPartBlock PIANO = registerMultiblockWithItem("piano", new PianoBlock());

    public static Map<MultiblockPartBlock, MultiblockMainPartBlock> MULTIBLOCKS = new HashMap<>();

    public static void registerMultiblocks() {

        for(MultiblockEnum multiblock : MultiblockEnum.values()) {

            for(BlockPosEnum pos : multiblock.getBlocksPosList()) {

                MultiblockMainPartBlock mainBlock = multiblock.getMainBlock();

                // if the main block is null, skip registering the multiblock
                if (mainBlock == null) break;

                MultiblockPartBlock mbpBlock = new MultiblockPartBlock(pos, mainBlock, multiblock.getShapeForBlock(pos));
                MULTIBLOCKS.put(registerBlock(multiblock.getName(pos), mbpBlock, false), mainBlock);

            }

        }

    }

    private static <T extends Block> T registerBlock(String name, T block) {

        return registerBlock(name, block, true);

    }

    private static <T extends Block> T registerBlock(String name, T block, boolean registerBlockItem) {

        if (registerBlockItem) registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(PCMain.MOD_ID, name), block);

    }

    private static <T extends Block> T registerBlockWithBlockItem(String name, T block, BlockItem blockItem) {

        registerBlockItem(name, blockItem);
        return registerBlock(name, block, false);

    }

    private static <T extends MultiblockMainPartBlock> T registerMultiblockWithItem(String name, T block) {

        return registerBlockWithBlockItem(name, block, new MultiblockItem(block, new FabricItemSettings(), block.getMultiblockType()));

    }

    private static void registerBlockItem(String name, Block block) {

        registerBlockItem(name, new BlockItem(block, new FabricItemSettings()));

    }

    private static void registerBlockItem(String name, BlockItem blockItem) {

        Item item = Registry.register(Registries.ITEM, new Identifier(PCMain.MOD_ID, name), blockItem);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.addAfter(Items.JUKEBOX, item));

    }

    public static void registerBlocks(){
        registerMultiblocks();

        PCMain.LOGGER.info("Blocks registered");
    }

}