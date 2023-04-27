package funskydev.pianocraft.datagen;

import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class PCLangGen extends FabricLanguageProvider {

    protected PCLangGen(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {

        translationBuilder.add(PCBlocks.PIANO, "Piano");

    }

}
