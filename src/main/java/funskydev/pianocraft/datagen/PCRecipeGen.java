package funskydev.pianocraft.datagen;

import funskydev.pianocraft.registry.PCBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.VanillaRecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;

import java.util.function.Consumer;

public class PCRecipeGen extends FabricRecipeProvider {

    public PCRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, PCBlocks.PIANO)
                .input(Character.valueOf('P'), ItemTags.PLANKS)
                .input(Character.valueOf('N'), Blocks.NOTE_BLOCK)
                .input(Character.valueOf('S'), Items.STRING)
                .pattern("PPP")
                .pattern("SSS")
                .pattern("PNP")
                .criterion("has_note_block", VanillaRecipeProvider.conditionsFromItem(Blocks.NOTE_BLOCK))
                .offerTo(exporter);


    }

}
