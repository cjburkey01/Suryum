package com.cjburkey.heck.game.camera;

import com.cjburkey.heck.ecs.Component;
import com.cjburkey.heck.ecs.components.SmoothMove;
import com.cjburkey.heck.glfw.Input;
import com.cjburkey.heck.math.Plane;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static com.cjburkey.heck.Transformation.*;

/**
 * Created by CJ Burkey on 2018/12/21
 */
@SuppressWarnings("WeakerAccess")
public class MouseZoom extends Component {
    
    public float nearLimit = 2.0f;
    public float farLimit = 100.0f;
    public float zoomSpeed = 0.5f;
    public float zoomSpeedMod = 0.15f;
    
    private SmoothMove smoothMove;
    private final Vector3f motion = new Vector3f();
    
    protected void onAdded(Component other) {
        if (other instanceof SmoothMove) {
            smoothMove = (SmoothMove) other;
        }
    }
    
    protected void onUpdate() {
        Vector3fc ray = screenToWorldRay(mainCamera(), Input.mousePos());
        motion.set(ray);
        float scroll = Input.scrollY();
        Plane near = new Plane(getTransform().forward().negate(new Vector3f()).mul(nearLimit),
                getTransform().forward().negate(new Vector3f()));
        float distIn = near.getIntersectionPoint(smoothMove.goalPosition, getTransform().forward()).distance(smoothMove.goalPosition);
        if (scroll > 0.0f) {
            float pointMouseDist = near.getIntersectionPoint(smoothMove.goalPosition, ray).distance(smoothMove.goalPosition);
            motion.mul(Math.min((zoomSpeed + distIn * zoomSpeedMod) * scroll, pointMouseDist));
        } else if (scroll < 0.0f) {
            Plane far = new Plane(getTransform().forward().negate(new Vector3f()).mul(farLimit), getTransform().forward());
            float pointMouseDist = far.getIntersectionPoint(smoothMove.goalPosition, ray).distance(smoothMove.goalPosition);
            motion.mul(Math.max((zoomSpeed + distIn * zoomSpeedMod) * scroll, -pointMouseDist));
        } else {
            return;
        }
        
        smoothMove.goalPosition.add(motion);
    }
    
}
