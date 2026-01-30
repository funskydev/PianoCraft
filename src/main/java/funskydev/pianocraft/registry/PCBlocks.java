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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class PCBlocks {

    public static final Block PIANO = registerMultiblockWithBlockItem("piano", PianoBlock::new, BlockBehaviour.Properties.of());

    public static Map<MultiblockPartBlock, MultiblockMainPartBlock> MULTIBLOCKS = new LinkedHashMap<>();

    private static <T extends MultiblockMainPartBlock> T registerMultiblockWithBlockItem(String name,
                                                                                         Function<BlockBehaviour.Properties, T> blockFactory,
                                                                                         BlockBehaviour.Properties blockSettings) {
        T mainPartBlock = registerBlock(name, blockFactory, blockSettings);
        registerBlockItem(name,
                (itemSettings) -> new MultiblockItem(mainPartBlock, itemSettings),
                new Item.Properties());

        return mainPartBlock;
    }

    private static <T extends Block> T registerBlock(String name,
                                                     Function<BlockBehaviour.Properties, T> blockFactory,
                                                     BlockBehaviour.Properties settings) {

        ResourceKey<Block> blockKey = keyOfBlock(name);
        T block = blockFactory.apply(settings.setId(blockKey));

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static void registerBlockItem(String name,
                                          Function<Item.Properties, BlockItem> blockItemFactory,
                                          Item.Properties settings) {

        ResourceKey<Item> itemKey = keyOfItem(name);
        BlockItem blockItem = blockItemFactory.apply(settings.setId(itemKey).useBlockDescriptionPrefix());
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.addAfter(Items.JUKEBOX, blockItem));
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(PCMain.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PCMain.MOD_ID, name));
    }

    public static void registerAllBlocks(){
        registerAllMultiblocks();

        PCMain.LOGGER.debug("Blocks registered");
    }

    public static void registerAllMultiblocks() {

        for(MultiblockEnum multiblock : MultiblockEnum.values()) {

            for(BlockPosEnum pos : multiblock.getBlocksPosList()) {

                MultiblockMainPartBlock mainBlock = multiblock.getMainBlock();

                // if the main block is null, skip registering the multiblock
                if (mainBlock == null) break;

                MultiblockPartBlock multiblockPartBlock = registerBlock(multiblock.getName(pos),
                        (blockSettings) -> new MultiblockPartBlock(blockSettings, pos, mainBlock, multiblock.getShapeForBlock(pos)),
                        BlockBehaviour.Properties.of());

                MULTIBLOCKS.put(multiblockPartBlock, mainBlock);

            }

        }

    }

}