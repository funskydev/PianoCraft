package funskydev.pianocraft.datagen;

import funskydev.pianocraft.block.MultiblockMainPartBlock;
import funskydev.pianocraft.block.MultiblockPartBlock;
import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class PCBlockTagGen extends FabricTagProvider.BlockTagProvider {

    public PCBlockTagGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {

        addMultiblockToTag(BlockTags.MINEABLE_WITH_AXE, PCBlocks.PIANO);

    }

    private void addMultiblockToTag(TagKey<Block> tag, Block mainBlock) {

        valueLookupBuilder(tag).add(mainBlock);

        for(MultiblockPartBlock partBlock : PCBlocks.MULTIBLOCKS.keySet()) {

            if (PCBlocks.MULTIBLOCKS.get(partBlock).equals(mainBlock)) valueLookupBuilder(tag).add(partBlock);

        }

    }

}
