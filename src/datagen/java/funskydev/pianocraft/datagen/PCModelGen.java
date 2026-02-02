package funskydev.pianocraft.datagen;

import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.world.level.block.Blocks;

public class PCModelGen extends FabricModelProvider {

    public PCModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

        blockModelGenerators.createNonTemplateHorizontalBlock(PCBlocks.PIANO);

        PCBlocks.MULTIBLOCKS.keySet()
                .forEach(multiblockPartBlock -> blockModelGenerators.createNonTemplateModelBlock(multiblockPartBlock, Blocks.AIR));

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

        /*itemModelGenerators.itemModelOutput.accept(PCBlocks.PIANO.asItem(),
                new Model(PIANO_MODEL_ID, Optional.empty(), new TextureKey[0]));*/

    }

}
