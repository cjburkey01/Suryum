package com.cjburkey.heck.material;

import lombok.Getter;

/**
 * Created by CJ Burkey on 2018/12/02
 */
@SuppressWarnings("WeakerAccess")
public abstract class Material {
    
    @Getter
    private Shader shader;
    
    public Material(Shader shader) {
        setShader(shader);
    }
    
    public final void bind() {
        if (shader == null) return;
        shader.bind();
        onUpdateData();
    }
    
    protected final void setShader(Shader shader) {
        this.shader = shader;
    }
    
    protected abstract void onUpdateData();
    
}
