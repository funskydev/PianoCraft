package funskydev.pianocraft.datagen;

import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class PCLootGen extends FabricBlockLootTableProvider {

    protected PCLootGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {

        dropSelf(PCBlocks.PIANO);

    }

}
