package com.cjburkey.heck.ecs.components;

import com.cjburkey.heck.Transformation;
import com.cjburkey.heck.ecs.Component;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by CJ Burkey on 2018/12/07
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Transform extends Component {
    
    public final Vector3f position = new Vector3f();
    public final Quaternionf rotation = new Quaternionf();
    public final Vector3f scale = new Vector3f(1.0f);
    
    public Transform() { }
    
    public Transform(Vector3fc position, Quaternionfc rotation, Vector3fc scale) {
        this.position.set(position);
        this.rotation.set(rotation);
        this.scale.set(scale);
    }
    
    public Matrix4fc getViewMatrix() {
        return Transformation.getViewMatrix(position, rotation);
    }
    
    public Matrix4fc getModelMatrix() {
        return Transformation.getModelMatrix(position, rotation, scale);
    }
    
    public Vector3fc transformPoint(Vector3fc point) {
        return Transformation.transformPoint(rotation, point);
    }
    
    public Vector3fc transformDir(Vector3fc dir) {
        return Transformation.transformDir(rotation, dir);
    }
    
    public Vector3fc right() {
        return Transformation.transformDir(rotation, new Vector3f(1.0f, 0.0f, 0.0f));
    }
    
    public Vector3fc up() {
        return Transformation.transformDir(rotation, new Vector3f(0.0f, 1.0f, 0.0f));
    }
    
    public Vector3fc forward() {
        return Transformation.transformDir(rotation, new Vector3f(0.0f, 0.0f, -1.0f));
    }
    
}
