package funskydev.pianocraft.datagen;

import funskydev.pianocraft.block.MultiblockMainPartBlock;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.registry.*;
import net.minecraft.tags.BlockTags;
import java.util.concurrent.CompletableFuture;

public class PCBlockTagGen extends FabricTagProvider.BlockTagProvider {

    public PCBlockTagGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {

        FabricTagBuilder axeMineableTag = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE);

        addMultiblockToTag(axeMineableTag, PCBlocks.PIANO);

    }

    private static void addMultiblockToTag(FabricTagBuilder tag, MultiblockMainPartBlock mainBlock) {

        tag.add(mainBlock);

        for(MultiblockPartBlock partBlock : PCBlocks.MULTIBLOCKS.keySet()) {

            if (PCBlocks.MULTIBLOCKS.get(partBlock).equals(mainBlock)) tag.add(partBlock);

        }

    }

}
