package com.cjburkey.heck.ecs.components;

import com.cjburkey.heck.Heck;
import com.cjburkey.heck.Transformation;
import com.cjburkey.heck.ecs.Component;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;

/**
 * Created by CJ Burkey on 2018/12/11
 */
@SuppressWarnings("WeakerAccess")
public class Camera extends Component {
    
    private float fovDegrees = 75.0f;
    private final Vector2f viewBounds = new Vector2f(0.5f, 500.0f);
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f tmpMatrix = new Matrix4f();
    
    public Camera() {
        forceUpdateProjectionMatrix();
    }
    
    public void setFov(float degrees) {
        fovDegrees = degrees;
        forceUpdateProjectionMatrix();
    }
    
    public float getFov() {
        return fovDegrees;
    }
    
    public void setNearPlane(float near) {
        viewBounds.x = near;
        forceUpdateProjectionMatrix();
    }
    
    public float getNearPlane() {
        return viewBounds.x;
    }
    
    public void setFarPlane(float far) {
        viewBounds.y = far;
        forceUpdateProjectionMatrix();
    }
    
    public float getFarPlane() {
        return viewBounds.y;
    }
    
    public void forceUpdateProjectionMatrix() {
        projectionMatrix.set(Transformation.getProjectionMatrix(fovDegrees,
                Heck.instance.getWindow().getWidth(),
                Heck.instance.getWindow().getHeight(),
                viewBounds.x,
                viewBounds.y));
    }
    
    public Matrix4fc getProjectionMatrix() {
        return projectionMatrix;
    }
    
    public Matrix4fc getProjectionViewMatrix() {
        return projectionMatrix.mul(getTransform().getViewMatrix(), tmpMatrix);
    }
    
}
