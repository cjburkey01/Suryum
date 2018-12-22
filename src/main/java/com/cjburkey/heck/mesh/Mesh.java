package com.cjburkey.heck.mesh;

import com.cjburkey.heck.Log;
import com.cjburkey.heck.ecs.GameObject;
import com.cjburkey.heck.ecs.components.MeshRenderer;
import com.cjburkey.heck.material.Material;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.opengl.GL30.*;

/**
 * Created by CJ Burkey on 2018/12/02
 */
@SuppressWarnings("WeakerAccess")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Mesh {
    
    private static int currentVao = -1;
    
    @Getter
    private final boolean dynamic;
    private boolean built = false;
    
    @EqualsAndHashCode.Include
    @Getter
    private final int vao;
    
    @EqualsAndHashCode.Include
    private final int vbo;
    
    @EqualsAndHashCode.Include
    private final int ebo;
    
    @Getter
    private int triangles;
    
    @Getter(AccessLevel.PROTECTED)  // Meant to be type-overwritten
    @Setter
    private Material material;
    
    public Mesh(boolean dynamic) {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();
        this.dynamic = dynamic;
    }
    
    protected final void setMeshData(FloatBuffer vertices, ShortBuffer indices) {
        bind();
        
        triangles = indices.limit();
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        built = true;
    }
    
    public final void render() {
        if (!built || material == null) return;
        material.bind();
        bind();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glEnableVertexAttribArray(0);
        onPreRender();
        if (!onRender()) glDrawElements(GL_TRIANGLES, triangles, GL_UNSIGNED_SHORT, 0);
        onPostRender();
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    protected void onPreRender() { }
    protected boolean onRender() { return false; }
    protected void onPostRender() { }
    
    public final void destroy() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
        
        onDestroy();
    }
    
    protected abstract void onDestroy();
    
    public Optional<MeshRenderer> createRenderer(GameObject parent) {
        return Optional.empty();
    }
    
    public final void bind() {
        if (isBound()) return;
        currentVao = vao;
        glBindVertexArray(vao);
    }
    
    public boolean isBound() {
        return vao == currentVao;
    }
    
}
