package funskydev.pianocraft.datagen;

import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class PCRecipeGen extends FabricRecipeProvider {

    public PCRecipeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {

                shaped(RecipeCategory.DECORATIONS, PCBlocks.PIANO)
                        .define('P', ItemTags.PLANKS)
                        .define('N', Blocks.NOTE_BLOCK)
                        .define('S', Items.STRING)
                        .pattern("PPP")
                        .pattern("SSS")
                        .pattern("PNP")
                        .unlockedBy(getHasName(Blocks.NOTE_BLOCK), has(Blocks.NOTE_BLOCK))
                        .save(output);

            }
        };
    }

    @Override
    public String getName() {
        return "PianoCraftRecipeProvider";
    }
}
