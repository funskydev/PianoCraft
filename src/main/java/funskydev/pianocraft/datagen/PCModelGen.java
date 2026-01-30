package funskydev.pianocraft.datagen;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.model.Model;
import net.minecraft.resources.Identifier;
import java.util.Optional;

public class PCModelGen extends FabricModelProvider {

    private static final Identifier PIANO_MODEL_ID = Identifier.fromNamespaceAndPath(PCMain.MOD_ID, "block/piano");

    public PCModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

        /*blockModelGenerators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                        PCBlocks.PIANO,
                                BlockModelGenerators.plainVariant(PIANO_MODEL_ID)

                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));*/

        blockModelGenerators.createNonTemplateHorizontalBlock(PCBlocks.PIANO);

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

        /*itemModelGenerators.itemModelOutput.accept(PCBlocks.PIANO.asItem(),
                new Model(PIANO_MODEL_ID, Optional.empty(), new TextureKey[0]));*/

    }

}
