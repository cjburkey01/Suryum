package com.cjburkey.heck.game;

import com.cjburkey.heck.Time;
import com.cjburkey.heck.ecs.Component;
import com.cjburkey.heck.glfw.Input;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by CJ Burkey on 2018/12/11
 */
@SuppressWarnings("WeakerAccess")
public class KeyboardMove extends Component {
    
    public boolean stop = false;
    public float speed = 1.0f;
    
    private final Vector3f motion = new Vector3f();
    
    protected void onUpdate() {
        if (stop) return;
        motion.zero();
        
        float scaledSpeed = Time.deltaTime(speed);
        if (Input.getAnyKeysDown(GLFW_KEY_S, GLFW_KEY_DOWN)) motion.y -= scaledSpeed;
        if (Input.getAnyKeysDown(GLFW_KEY_W, GLFW_KEY_UP)) motion.y += scaledSpeed;
        if (Input.getAnyKeysDown(GLFW_KEY_D, GLFW_KEY_RIGHT)) motion.x += scaledSpeed;
        if (Input.getAnyKeysDown(GLFW_KEY_A, GLFW_KEY_LEFT)) motion.x -= scaledSpeed;
        if (!motion.equals(0.0f, 0.0f, 0.0f)) motion.set(getTransform().transformDir(motion.normalize()));
        
        getTransform().position.add(motion);
    }
    
}
