package funskydev.pianocraft.datagen;

import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.VanillaRecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class PCRecipeGen extends FabricRecipeProvider {

    public PCRecipeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, PCBlocks.PIANO)
                .input('P', ItemTags.PLANKS)
                .input('N', Blocks.NOTE_BLOCK)
                .input('S', Items.STRING)
                .pattern("PPP")
                .pattern("SSS")
                .pattern("PNP")
                .criterion("has_note_block", VanillaRecipeProvider.conditionsFromItem(Blocks.NOTE_BLOCK))
                .offerTo(exporter);

    }
}
