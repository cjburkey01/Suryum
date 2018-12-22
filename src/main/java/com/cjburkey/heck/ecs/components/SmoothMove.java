package com.cjburkey.heck.ecs.components;

import com.cjburkey.heck.Time;
import com.cjburkey.heck.Util;
import com.cjburkey.heck.ecs.Component;
import org.joml.Vector3f;

/**
 * Created by CJ Burkey on 2018/12/21
 */
@SuppressWarnings("WeakerAccess")
public class SmoothMove extends Component {
    
    public boolean move = true;
    public float springConst = 450.0f;
    public final Vector3f goalPosition = new Vector3f();
    
    private final Vector3f velocity = new Vector3f();
    
    protected void onAdded() {
        goalPosition.set(getTransform().position);
    }
    
    protected void onUpdate() {
        if (move) {
            getTransform().position.set(Util.dampSpringCrit(goalPosition, getTransform().position, velocity, springConst, Time.deltaTime()));
        }
    }
    
}
