package com.cjburkey.heck.math;

import lombok.EqualsAndHashCode;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by CJ Burkey on 2018/12/20
 */
@EqualsAndHashCode
public class Ray {
    
    public final Vector3f origin = new Vector3f();
    public final Vector3f vector = new Vector3f();
    
    public Ray() {
    }
    
    public Ray(Vector3fc origin, Vector3fc vector) {
        this.origin.set(origin);
        this.vector.set(vector);
    }
    
}
