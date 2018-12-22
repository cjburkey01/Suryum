package com.cjburkey.heck.game.camera;

import com.cjburkey.heck.ecs.Component;
import com.cjburkey.heck.ecs.components.SmoothMove;
import com.cjburkey.heck.glfw.Input;
import com.cjburkey.heck.math.Plane;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static com.cjburkey.heck.Transformation.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by CJ Burkey on 2018/12/20
 */
@SuppressWarnings("WeakerAccess")
public class MouseMove extends Component {
    
    @Getter
    private final Plane plane = new Plane(new Vector3f(), new Vector3f(0.0f, 1.0f, 0.0f));
    public final IntOpenHashSet activatingMouseButtons = new IntOpenHashSet();
    public boolean lock = false;
    
    private final Vector2f cursorPos = new Vector2f();
    private final Vector2f prevCursorPos = new Vector2f();
    private final Vector3f cursorWorldPos = new Vector3f();
    private final Vector3f prevCursorWorldPos = new Vector3f();
    private final Vector3f deltaPosition = new Vector3f();
    private SmoothMove smoothMove;
    
    public MouseMove() {
        activatingMouseButtons.add(GLFW_MOUSE_BUTTON_MIDDLE);
    }
    
    protected void onAdded(Component component) {
        if (component instanceof SmoothMove) {
            smoothMove = (SmoothMove) component;
        }
    }
    
    protected void onUpdate() {
        cursorPos.set(Input.mousePos());
        
        if (!lock && Input.getAnyMousePressed(activatingMouseButtons)) {
            // Get current cursor position in the world
            Vector3fc pos = screenToPlane(mainCamera(), cursorPos, plane);
            if (pos == null) {
                return;
            }
            cursorWorldPos.set(pos);
            
            // Get previous cursor position in the world
            pos = screenToPlane(mainCamera(), prevCursorPos, plane);
            if (pos == null) {
                pos = new Vector3f(getTransform().position);
            }
            prevCursorWorldPos.set(pos);
            
            // Move the camera the distance between the previous and current world points
            smoothMove.goalPosition.sub(cursorWorldPos.sub(prevCursorWorldPos, deltaPosition));
        }
        
        prevCursorPos.set(cursorPos);
    }
    
}
