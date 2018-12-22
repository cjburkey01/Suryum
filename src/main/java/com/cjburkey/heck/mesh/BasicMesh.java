package com.cjburkey.heck.mesh;

import com.cjburkey.heck.Util;
import com.cjburkey.heck.ecs.GameObject;
import com.cjburkey.heck.ecs.components.MeshRenderer;
import com.cjburkey.heck.material.BasicMaterial;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/12/02
 */
public class BasicMesh extends Mesh {
    
    public BasicMesh(boolean dynamic) {
        super(dynamic);
    }
    
    public void set(float[] vertices, short[] indices) {
        setMeshData(Util.bufferFloat(vertices), Util.bufferShort(indices));
    }
    
    public BasicMaterial getMaterial() {
        return (BasicMaterial) super.getMaterial();
    }
    
    protected void onDestroy() {
    }
    
    public Optional<MeshRenderer> createRenderer(GameObject parent) {
        if (parent.getHasComponent(MeshRenderer.class)) return Optional.empty();
        MeshRenderer renderer = new MeshRenderer();
        renderer.setMesh(this);
        parent.addComponent(renderer);
        return Optional.of(renderer);
    }
    
}
