package funskydev.pianocraft.datagen;

import funskydev.pianocraft.block.MultiblockMainPartBlock;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class PCBlockTagGen extends FabricTagProvider.BlockTagProvider {

    public PCBlockTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        FabricTagBuilder axeMineableTag = getOrCreateTagBuilder(BlockTags.AXE_MINEABLE);

        addMultiblockToTag(axeMineableTag, PCBlocks.PIANO);

    }

    private static void addMultiblockToTag(FabricTagBuilder tag, MultiblockMainPartBlock mainBlock) {

        tag.add(mainBlock);

        for(MultiblockPartBlock partBlock : PCBlocks.MULTIBLOCKS.keySet()) {

            if (PCBlocks.MULTIBLOCKS.get(partBlock).equals(mainBlock)) tag.add(partBlock);

        }

    }

}
