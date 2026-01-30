package funskydev.pianocraft.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeUtil {

    public static VoxelShape rotateBoundingBox(VoxelShape shape, Direction facing) {

        if (facing == Direction.NORTH) return shape;

        VoxelShape rotatedShape = Shapes.empty();

        for (AABB box : shape.toAabbs()) {

            double[] rotatedCoords = rotateBoxCoords(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, facing);
            rotatedShape = Shapes.join(rotatedShape, Shapes.box(rotatedCoords[0], rotatedCoords[1], rotatedCoords[2], rotatedCoords[3], rotatedCoords[4], rotatedCoords[5]), BooleanOp.OR);

        }

        return rotatedShape;

    }

    public static double[] rotateBoxCoords(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Direction direction) {

        double xSize = maxX - minX;
        double zSize = maxZ - minZ;

        switch (direction) {
            case EAST -> {
                return new double[] {1 - maxZ, minY, minX, 1 - minZ, maxY, minX + xSize};
            }
            case SOUTH -> {
                return new double[] {1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ};
            }
            case WEST -> {
                return new double[] {minZ, minY, 1 - maxX, maxZ, maxY, 1 - minX};
            }
            default -> {
                return new double[] {minX, minY, minZ, maxX, maxY, maxZ};
            }
        }

    }

}
