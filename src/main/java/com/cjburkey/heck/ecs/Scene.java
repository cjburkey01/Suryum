package com.cjburkey.heck.ecs;

import com.cjburkey.heck.ecs.components.Camera;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Created by CJ Burkey on 2018/12/08
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class Scene {
    
    private final ObjectOpenHashSet<GameObject> objectsToAdd = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<GameObject> objectsToRem = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<GameObject> objects = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<Camera> cameras = new ObjectOpenHashSet<>();
    
    @Getter
    private Camera mainCamera;
    
    public GameObject instantiate() {
        GameObject obj = new GameObject(this);
        objectsToAdd.add(obj);
        obj.updateComponents();
        return obj;
    }
    
    public boolean destroyGameObject(GameObject object) {
        objectsToAdd.remove(object);
        return objects.contains(object) && objectsToRem.add(object);
    }
    
    public void forEach(Consumer<? super Component> consumer) {
        updateObjects();
        objects.forEach(object -> { if (object.getIsActive()) object.forEach(consumer); });
    }
    
    public void update() {
        forEach(Component::onEarlyUpdate);
        forEach(Component::onUpdate);
        forEach(Component::onLateUpdate);
    }
    
    public void render() {
        forEach(Component::onEarlyRender);
        forEach(Component::onRender);
        forEach(Component::onLateRender);
    }
    
    public void cleanup() {
        objects.forEach(this::destroyGameObject);
        updateObjects();
    }
    
    private void updateObjects() {
        if (objectsToAdd.size() > 0) {
            objectsToAdd.forEach(object -> {
                objects.add(object);
                object.setActive(true);
            });
            objectsToAdd.clear();
        }
        if (objectsToRem.size() > 0) {
            objectsToRem.forEach(object -> {
                object.onDestroy();
                objects.remove(object);
                object.setActive(false);
            });
            objectsToRem.clear();
        }
    }
    
    void addCamera(Camera camera) {
        if (camera == null) return;
        if (mainCamera == null) mainCamera = camera;
        cameras.add(camera);
    }
    
    void removeCamera(Camera camera) {
        if (camera == null) return;
        if (camera.equals(mainCamera)) {
            if (cameras.size() > 0) mainCamera = cameras.iterator().next();
            else mainCamera = null;
        }
        cameras.remove(camera);
    }
    
    public Stream<Camera> getCameras() {
        return cameras.stream();
    }
    
}
