package com.cjburkey.heck.math;

import lombok.EqualsAndHashCode;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by CJ Burkey on 2018/12/20
 */
@SuppressWarnings("unused")
@EqualsAndHashCode
public class Plane {
    
    public final Vector3f center = new Vector3f();
    public final Vector3f normal = new Vector3f();
    
    private final Vector3f tmp = new Vector3f();
    
    public Plane() {
    }
    
    public Plane(Vector3fc center, Vector3fc normal) {
        this.center.set(center);
        this.normal.set(normal);
    }
    
    public Vector3fc getIntersectionPoint(Ray ray) {
        return getIntersectionPoint(ray.origin, ray.vector);
    }
    
    public Vector3fc getIntersectionPoint(Vector3fc rayOrigin, Vector3fc rayDir) {
        float rDotn = rayDir.dot(normal);
        
        // If the plane is parallel to the ray, there is no point of intersection
        if (((Float) rDotn).equals(0.0f)) {
            return null;
        }
        
        float dp = normal.dot(center.sub(rayOrigin, tmp)) / rDotn;
        return rayOrigin.add(rayDir.mul(dp, tmp), tmp);
    }
    
}
