package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.block.MultiblockMainPartBlock;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.block.PianoBlock;
import funskydev.pianocraft.item.MultiblockItem;
import funskydev.pianocraft.util.BlockPosEnum;
import funskydev.pianocraft.util.MultiblockEnum;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.LinkedHashMap;
import java.util.Map;

public class PCBlocks {

    public static final MultiblockMainPartBlock PIANO = registerMultiblockWithItem("piano", new PianoBlock(BlockBehaviour.Properties.of()));

    public static Map<MultiblockPartBlock, MultiblockMainPartBlock> MULTIBLOCKS = new LinkedHashMap<>();

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
        return Registry.register(BuiltInRegistries.BLOCK, new Identifier(PCMain.MOD_ID, name), block);

    }

    private static <T extends Block> T registerBlockWithBlockItem(String name, T block, BlockItem blockItem) {

        registerBlockItem(name, blockItem);
        return registerBlock(name, block, false);

    }

    private static <T extends MultiblockMainPartBlock> T registerMultiblockWithItem(String name, T block) {

        return registerBlockWithBlockItem(name, block, new MultiblockItem(block, new Item.Properties(), block.getMultiblockType()));

    }

    private static void registerBlockItem(String name, Block block) {

        registerBlockItem(name, new BlockItem(block, new Item.Properties()));

    }

    private static void registerBlockItem(String name, BlockItem blockItem) {

        Item item = Registry.register(BuiltInRegistries.ITEM, new Identifier(PCMain.MOD_ID, name), blockItem);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.addAfter(Items.JUKEBOX, item));

    }

    public static void registerBlocks(){
        registerMultiblocks();

        PCMain.LOGGER.debug("Blocks registered");
    }

}