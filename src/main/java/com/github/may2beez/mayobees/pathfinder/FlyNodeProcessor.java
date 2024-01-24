package com.github.may2beez.mayobees.pathfinder;

import cc.polyfrost.oneconfig.libs.checker.nullness.qual.Nullable;
import com.github.may2beez.mayobees.util.BlockUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.pathfinder.NodeProcessor;

public class FlyNodeProcessor extends NodeProcessor {

    @Override
    public PathPoint getPathPointTo(Entity entityIn) {
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }

    @Override
    public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double z) {
        return this.openPoint(MathHelper.floor_double(x - (entityIn.width / 2.0D)), MathHelper.floor_double(y - (entityIn.height / 2.0D)), MathHelper.floor_double(z - (entityIn.width / 2.0D)));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int i = 0;
        for (EnumFacing enumfacing : EnumFacing.values()) {
            PathPoint pathpoint = this.getAirNode(currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());
            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
                pathOptions[i++] = pathpoint;
        }
        return i;
    }

    @Nullable
    private PathPoint getAirNode(int startX, int startY, int startZ) {
        for (int x = startX; x < startX + entitySizeX; ++x) {
            for (int y = startY; y < startY + entitySizeY; ++y) {
                for (int z = startZ; z < startZ + entitySizeZ; ++z) {
                    if (!BlockUtils.isFree(x, y, z, blockaccess)) {
                        return null;
                    }
                }
            }
        }
        return this.openPoint(startX, startY, startZ);
    }
}