package funskydev.pianocraft.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PCDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {

        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(PCLangGen::new);
        pack.addProvider(PCModelGen::new);

    }

}
