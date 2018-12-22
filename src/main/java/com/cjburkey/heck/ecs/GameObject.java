package com.cjburkey.heck.ecs;

import com.cjburkey.heck.ecs.components.Camera;
import com.cjburkey.heck.ecs.components.Transform;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;

/**
 * Created by CJ Burkey on 2018/12/08
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class GameObject {
    
    private Camera camera;
    
    @EqualsAndHashCode.Include
    public final UUID uuid = UUID.randomUUID();
    
    public final Scene scene;
    public final Transform transform;
    private boolean isActive;
    
    private final ObjectArrayFIFOQueue<Component> componentsToAdd = new ObjectArrayFIFOQueue<>();
    private final ObjectOpenHashSet<Class<? extends Component>> componentsToRem = new ObjectOpenHashSet<>();
    private final Object2ObjectArrayMap<Class<? extends Component>, Component> components = new Object2ObjectArrayMap<>();
    
    GameObject(@NotNull Scene scene, @Nullable Transform transform) {
        this.scene = scene;
        transform = addComponent(transform == null ? new Transform() : transform).orElse(null);
        this.transform = transform;
    }
    
    GameObject(@NotNull Scene scene) {
        this(scene, null);
    }
    
    public <T extends Component> Optional<T> addComponent(T component) {
        if (component == null || component.getIsAdded() || components.containsKey(component.getClass())) return Optional.empty();
        componentsToAdd.enqueue(component);
        component.setParent(this);
        component.setTransform(transform);
        component.onAdd();
        if (component instanceof Camera) {
            camera = (Camera) component;
            scene.addCamera(camera);
        }
        return Optional.of(component);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> getComponent(Class<T> type) {
        return Optional.ofNullable((T) components.getOrDefault(type, null));
    }
    
    public <T extends Component> boolean getHasComponent(Class<T> type) {
        return components.containsKey(type);
    }
    
    public <T extends Component> Optional<T> removeComponent(Class<T> type) {
        Optional<T> at = getComponent(type);
        at.ifPresent(aAt -> {
            aAt.onRemove();
            aAt.setTransform(null);
            aAt.setParent(null);
            componentsToRem.add(type);
            if (getHasCamera()) scene.removeCamera(camera);
        });
        return at;
    }
    
    public void forEach(Consumer<? super Component> consumer) {
        updateComponents();
        components.values().forEach(component -> { if (component.getIsActive()) consumer.accept(component); });
    }
    
    public void destroy() {
        scene.destroyGameObject(this);
    }
    
    void onDestroy() {
        forEach(component -> removeComponent(component.getClass()));
        updateComponents();
    }
    
    void updateComponents() {
        componentsToRem.forEach(component -> components.remove(component).onRemoved());
        componentsToRem.clear();
        while (!componentsToAdd.isEmpty()) {
            Component component = componentsToAdd.dequeue();
            components.put(component.getClass(), component);
            component.onAdded();
            forEach(component1 -> component1.onAdded(component));
        }
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        if (isActive) forEach(Component::onActivated);
        else forEach(Component::onDeactivated);
    }
    
    public boolean getIsActive() {
        return isActive;
    }
    
    public boolean getHasCamera() {
        return camera != null;
    }
    
    public Camera getCamera() {
        return camera;
    }
    
}
