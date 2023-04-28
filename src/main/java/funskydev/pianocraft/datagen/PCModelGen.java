package funskydev.pianocraft.datagen;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class PCModelGen extends FabricModelProvider {

    public PCModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGen) {

        blockStateModelGen.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(
                        PCBlocks.PIANO,
                        BlockStateVariant.create().put(VariantSettings.MODEL, new Identifier(PCMain.MOD_ID, "block/piano"))
                ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGen) {

        itemModelGen.register(PCBlocks.PIANO.asItem(),
                new Model(Optional.of(new Identifier(PCMain.MOD_ID, "block/piano")), Optional.empty(), new TextureKey[0]));

    }

}
