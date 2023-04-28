package funskydev.pianocraft.util;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class VoxelShapeUtil {

    public static VoxelShape rotateBoundingBox(VoxelShape shape, Direction facing) {

        if (facing == Direction.NORTH) return shape;

        VoxelShape rotatedShape = VoxelShapes.empty();

        for (Box box : shape.getBoundingBoxes()) {

            double[] rotatedCoords = rotateBoxCoords(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, facing);
            rotatedShape = VoxelShapes.combineAndSimplify(rotatedShape, VoxelShapes.cuboid(rotatedCoords[0], rotatedCoords[1], rotatedCoords[2], rotatedCoords[3], rotatedCoords[4], rotatedCoords[5]), BooleanBiFunction.OR);

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
