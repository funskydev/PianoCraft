package funskydev.pianocraft.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;

public class MultiblockSettings extends FabricBlockSettings {

    protected MultiblockSettings(Material material, MapColor color) {
        super(material, color);
    }

    public static MultiblockSettings of(Material material) {
        MapColor color = material.getColor();
        return new MultiblockSettings(createMultiblockMaterial(color), color);
    }

    public static Material createMultiblockMaterial(MapColor color) {
        return new Material.Builder(color).blocksPistons().build();
    }

}
