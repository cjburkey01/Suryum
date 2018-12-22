package com.cjburkey.heck.material;

import com.cjburkey.heck.Log;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.nio.FloatBuffer;
import org.joml.Matrix3fc;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;
import org.joml.Vector3fc;
import org.joml.Vector3ic;
import org.joml.Vector4fc;
import org.joml.Vector4ic;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by CJ Burkey on 2018/11/25
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Shader {
    
    private static int currentProgram = -1;
    
    private final Int2IntOpenHashMap shaders = new Int2IntOpenHashMap();
    private final Object2IntOpenHashMap<String> uniforms = new Object2IntOpenHashMap<>();
    private int program;
    private boolean done = false;
    
    public Shader() {
        program = glCreateProgram();
    }
    
    protected final void addShader(int type, String source) {
        if (done) throw new IllegalStateException("Cannot add shader to a finished shader program");
        if (source == null) throw new NullPointerException("Shader source was null");
        if (shaders.containsKey(type)) return;
        int shader = glCreateShader(type);
        if (shader <= 0) return;
        glShaderSource(shader, source);
        shaders.put(type, shader);
    }
    
    protected final void finish() {
        if (done) throw new IllegalStateException("Cannot finish a finished shader program");
        for (int shader : shaders.values()) {
            glCompileShader(shader);
            String info = glGetShaderInfoLog(shader).trim();
            if (!info.isEmpty()) {
                Log.error("Failed to compile shader: {}", info);
                return;
            }
            glAttachShader(program, shader);
        }
        glLinkProgram(program);
        String info = glGetProgramInfoLog(program).trim();
        if (!info.isEmpty()) {
            Log.error("Failed to link shader program: {}", info);
            return;
        }
        glValidateProgram(program);
        info = glGetProgramInfoLog(program).trim();
        if (!info.isEmpty()) Log.warn("Failed to validate shader program: {}", info);
        for (int shader : shaders.values()) {
            glDetachShader(program, shader);
            glDeleteShader(shader);
        }
        shaders.clear();
        done = true;
    }
    
    public void destroy() {
        if (shaders.size() > 0) {
            for (int shader : shaders.values()) glDeleteShader(shader);
        }
        glDeleteProgram(program);
        done = false;
    }
    
    public void bind() {
        if (!done) throw new IllegalStateException("Cannot bind an unfinished shader program");
        if (isBound()) return;
        currentProgram = program;
        glUseProgram(program);
    }
    
    public boolean isBound() {
        return currentProgram == program;
    }
    
    // Uniforms
    
    public void setUniform(String name, float value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform1f(at, value);
    }
    
    public void setUniform(String name, Vector2fc value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform2f(at, value.x(), value.y());
    }
    
    public void setUniform(String name, Vector3fc value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform3f(at, value.x(), value.y(), value.z());
    }
    
    public void setUniform(String name, Vector4fc value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform4f(at, value.x(), value.y(), value.z(), value.w());
    }
    
    public void setUniform(String name, boolean value) {
        setUniform(name, value ? 1 : 0);
    }
    
    public void setUniform(String name, int value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform1i(at, value);
    }
    
    public void setUniform(String name, Vector2ic value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform2i(at, value.x(), value.y());
    }
    
    public void setUniform(String name, Vector3ic value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform3i(at, value.x(), value.y(), value.z());
    }
    
    public void setUniform(String name, Vector4ic value) {
        int at = getUniformLocation(name);
        if (at >= 0) glUniform4i(at, value.x(), value.y(), value.z(), value.w());
    }
    
    public void setUniform(String name, Matrix3fc value) {
        int at = getUniformLocation(name);
        if (at < 0) return;
        FloatBuffer buff = memAllocFloat(9);
        glUniformMatrix3fv(at, false, value.get(buff));
        memFree(buff);
    }
    
    public void setUniform(String name, Matrix4fc value) {
        int at = getUniformLocation(name);
        if (at < 0) return;
        FloatBuffer buff = memAllocFloat(16);
        glUniformMatrix4fv(at, false, value.get(buff));
        memFree(buff);
    }
    
    private int getUniformLocation(String name) {
        bind();
        if (uniforms.containsKey(name)) return uniforms.getInt(name);
        int at = glGetUniformLocation(program, name);
        uniforms.put(name, at);
        if (at < 0) Log.error("Uniform \"{}\" could not be found", name);
        return at;
    }
    
}
