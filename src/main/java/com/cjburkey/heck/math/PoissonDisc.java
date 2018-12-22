package com.cjburkey.heck.math;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Created by CJ Burkey on 2018/12/22
 */
@SuppressWarnings("WeakerAccess")
public final class PoissonDisc {
    
    private static final Random random = new Random(System.nanoTime());
    
    public final int pointLimit;
    public final int maxIter;
    public final float minDistance;
    public final float minDistanceSq;
    public final float gridSize;
    
    private Object2ObjectOpenHashMap<Vector2i, Vector2f> points = new Object2ObjectOpenHashMap<>();
    
    public PoissonDisc(int pointLimit, int maxIter, float minDistance) {
        this.pointLimit = pointLimit;
        this.maxIter = maxIter;
        this.minDistance = minDistance;
        minDistanceSq = minDistance * minDistance;
        gridSize = minDistance / (float) Math.sqrt(2);
    }
    
    public void sample() {
        points.clear();
        
        final ObjectArrayList<Vector2f> addedPoints = new ObjectArrayList<>();
        addedPoints.add(new Vector2f(random.nextFloat() * 2.0f - 1.0f, random.nextFloat() * 2.0f - 1.0f));
        
        final Vector2f addedPoint = new Vector2f();
        final Vector2i addedCell = new Vector2i();
        final Vector2i checkCell = new Vector2i();
        
        while (points.size() < pointLimit) {
            Vector2f point = addedPoints.get(random.nextInt(addedPoints.size()));
            int i = 0;
            boolean didAddPoint = false;
            while (i < maxIter && !didAddPoint) {
                addedPoint.set(pointInAnnulus(minDistance, minDistance * 2)).add(point);
                addedCell.set(getCell(addedPoint, gridSize));
                boolean tooClose = false;
                for (int x = addedCell.x - 1; x <= addedCell.x + 1 && !tooClose; x ++) {
                    for (int y = addedCell.y - 1; y <= addedCell.y + 1 && !tooClose; y ++) {
                        if (points.containsKey(checkCell.set(x, y)) && addedPoint.distanceSquared(points.get(checkCell)) < minDistanceSq) {
                            tooClose = true;
                        }
                    }
                }
                if (!tooClose) {
                    addedPoints.add(addedPoint);
                    points.put(addedCell, addedPoint);
                    didAddPoint = true;
                }
                i ++;
            }
            if (!didAddPoint) {
                addedPoints.remove(point);
            }
        }
    }
    
    public Vector2f[] getPoints() {
        return points.values().toArray(new Vector2f[0]);
    }
    
    private static Vector2i getCell(Vector2f point, float gridSize) {
        return new Vector2i((int) (Math.floor(point.x / gridSize) * gridSize), (int) (Math.floor(point.y / gridSize) * gridSize));
    }
    
    private static Vector2f pointInAnnulus(float min, float max) {
        float theta = (float) Math.PI * 2.0f * random.nextFloat();
        float dist = (float) Math.sqrt(random.nextFloat() * (max * max - min * min) + min * min);
        return new Vector2f(dist * (float) Math.cos(theta), dist * (float) Math.sin(theta));
    }
    
}
