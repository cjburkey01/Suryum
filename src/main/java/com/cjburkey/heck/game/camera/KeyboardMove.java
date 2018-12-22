package com.cjburkey.heck.game.camera;

import com.cjburkey.heck.Time;
import com.cjburkey.heck.ecs.Component;
import com.cjburkey.heck.ecs.components.SmoothMove;
import com.cjburkey.heck.glfw.Input;
import com.cjburkey.heck.math.Plane;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by CJ Burkey on 2018/12/11
 */
@SuppressWarnings("WeakerAccess")
public class KeyboardMove extends Component {
    
    public float nearLimit = 2.0f;
    public boolean stop = false;
    public float speed = 10.0f;
    public float speedMod = 3.5f;
    
    private final Vector3f motion = new Vector3f();
    private SmoothMove smoothMove;
    
    protected void onAdded(Component component) {
        if (component instanceof SmoothMove) {
            smoothMove = (SmoothMove) component;
        }
    }
    
    protected void onUpdate() {
        if (stop) return;
        motion.zero();
        
        Plane near = new Plane(getTransform().forward().negate(new Vector3f()).mul(nearLimit),
                getTransform().forward().negate(new Vector3f()));
        float distIn = near.getIntersectionPoint(smoothMove.goalPosition, getTransform().forward()).distance(smoothMove.goalPosition);
        
        if (Input.getAnyKeysDown(GLFW_KEY_S, GLFW_KEY_DOWN)) motion.y -= 1.0f;
        if (Input.getAnyKeysDown(GLFW_KEY_W, GLFW_KEY_UP)) motion.y += 1.0f;
        if (Input.getAnyKeysDown(GLFW_KEY_D, GLFW_KEY_RIGHT)) motion.x += 1.0f;
        if (Input.getAnyKeysDown(GLFW_KEY_A, GLFW_KEY_LEFT)) motion.x -= 1.0f;
        if (!motion.equals(0.0f, 0.0f, 0.0f)) {
            motion.set(getTransform().transformDir(motion.normalize())).mul(Time.deltaTime(distIn * speedMod + speed));
        }
        
        smoothMove.goalPosition.add(motion);
    }
    
}
