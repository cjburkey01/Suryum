package com.cjburkey.heck.ecs;

import com.cjburkey.heck.Heck;
import com.cjburkey.heck.ecs.components.Camera;
import com.cjburkey.heck.ecs.components.Transform;
import java.util.UUID;
import lombok.EqualsAndHashCode;

/**
 * Created by CJ Burkey on 2018/12/08
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Component {
    
    @EqualsAndHashCode.Include
    public final UUID uuid = UUID.randomUUID();
    
    private GameObject parent;
    private Transform transform;
    private boolean isAdded;
    private boolean isActive = true;
    
    protected void onActivated() { }
    protected void onDeactivated() { }
    protected void onAdd() { }
    protected void onAdded() { }
    protected void onRemove() { }
    protected void onRemoved() { }
    
    protected void onEarlyUpdate() { }
    protected void onUpdate() { }
    protected void onLateUpdate() { }
    
    protected void onEarlyRender() { }
    protected void onRender() { }
    protected void onLateRender() { }
    
    public void destroy() {
        parent.removeComponent(getClass());
    }
    
    void setParent(GameObject parent) {
        isAdded = true;
        this.parent = parent;
    }
    
    public GameObject getParent() {
        return parent;
    }
    
    public boolean getIsAdded() {
        return isAdded;
    }
    
    void setTransform(Transform transform) {
        this.transform = transform;
    }
    
    public Transform getTransform() {
        return transform;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        if (isActive) onActivated();
        else onDeactivated();
    }
    
    public boolean getIsActive() {
        return isActive;
    }
    
    public Camera mainCamera() {
        return parent.scene.getMainCamera();
    }
    
}
