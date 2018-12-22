package com.cjburkey.heck.material;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by CJ Burkey on 2018/12/05
 */
@SuppressWarnings("unused")
public class BasicMaterial extends Material {
    
    private boolean transforms = false;
    private final Matrix4f transformation = new Matrix4f().identity();
    private final Vector3f albedo = new Vector3f();
    
    public BasicMaterial(BasicShader shader) {
        super(shader);
    }
    
    public void setTransforms(boolean transforms) {
        this.transforms = transforms;
    }
    
    public boolean getTransforms() {
        return transforms;
    }
    
    public void setTransformation(Matrix4fc transformation) {
        this.transformation.set(transformation);
    }
    
    public Matrix4fc getTransformation() {
        return transformation;
    }
    
    public void setAlbedo(float r, float g, float b) {
        albedo.set(r, g, b);
    }
    
    public void setAlbedo(Vector3fc albedo) {
        this.albedo.set(albedo);
    }
    
    public Vector3fc getAlbedo() {
        return albedo;
    }
    
    protected void onUpdateData() {
        getShader().setUniform("transforms", transforms);
        if (transforms) getShader().setUniform("transformation", transformation);
        getShader().setUniform("albedo", albedo);
    }
    
}
