package funskydev.pianocraft.datagen;

import funskydev.pianocraft.PCMain;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.Nullable;

public class PCDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {

        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(PCLangGen::new);
        pack.addProvider(PCModelGen::new);
        pack.addProvider(PCBlockTagGen::new);
        pack.addProvider(PCLootGen::new);
        pack.addProvider(PCRecipeGen::new);

    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return PCMain.MOD_ID;
    }

}
