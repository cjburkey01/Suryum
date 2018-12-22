package com.cjburkey.heck;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by CJ Burkey on 2018/11/25
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Util {
    
    public static Optional<String> readResource(String name) {
        name = name.replaceAll(Pattern.quote("\""), "/").trim();
        while (name.startsWith("/")) name = name.substring(1);
        while (name.endsWith("/")) name = name.substring(0, name.length() - 1);
        name = '/' + name;
        InputStream stream = Util.class.getResourceAsStream(name);
        if (stream == null) {
            Log.exception(new FileNotFoundException("Failed to locate resource: " + name));
            return Optional.empty();
        }
        return readStream(stream);
    }
    
    public static Optional<String> readStream(InputStream stream) {
        if (stream == null) return Optional.empty();
        StringWriter output = new StringWriter();
        try {
            IOUtils.copy(stream, output, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.exception(e);
            return Optional.empty();
        }
        return Optional.of(output.toString().trim());
    }
    
    public static FloatBuffer bufferVec3(Collection<Vector3f> vectors) {
        FloatBuffer buffer = memAllocFloat(vectors.size() * 3);
        vectors.forEach(vector -> vector.get(buffer));
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer bufferVec3(Vector3f[] vectors) {
        FloatBuffer buffer = memAllocFloat(vectors.length * 3);
        for (Vector3f vector : vectors) vector.get(buffer);
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer bufferVec2(Collection<Vector2f> vectors) {
        FloatBuffer buffer = memAllocFloat(vectors.size() * 2);
        vectors.forEach(vector -> vector.get(buffer));
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer bufferVec2(Vector2f[] vectors) {
        FloatBuffer buffer = memAllocFloat(vectors.length * 2);
        for (Vector2f vector : vectors) vector.get(buffer);
        buffer.flip();
        return buffer;
    }
    
    public static ShortBuffer bufferShort(Collection<Short> shorts) {
        ShortBuffer buffer = memAllocShort(shorts.size());
        shorts.forEach(buffer::put);
        buffer.flip();
        return buffer;
    }
    
    public static ShortBuffer bufferShort(short[] shorts) {
        ShortBuffer buffer = memAllocShort(shorts.length);
        for (short s : shorts) buffer.put(s);
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer bufferFloat(Collection<Float> floats) {
        FloatBuffer buffer = memAllocFloat(floats.size());
        floats.forEach(buffer::put);
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer bufferFloat(float[] floats) {
        FloatBuffer buffer = memAllocFloat(floats.length);
        for (float f : floats) buffer.put(f);
        buffer.flip();
        return buffer;
    }
    
    public static float lerp(float start, float goal, float progress) {
        return start + (goal - start) * progress;
    }
    
    public static Vector2f lerp(Vector2fc start, Vector2fc goal, float progress) {
        return new Vector2f(lerp(start.x(), goal.x(), progress),
                lerp(start.y(), goal.y(), progress));
    }
    
    public static Vector3f lerp(Vector3fc start, Vector3fc goal, float progress) {
        return new Vector3f(lerp(start.x(), goal.x(), progress),
                lerp(start.y(), goal.y(), progress),
                lerp(start.z(), goal.z(), progress));
    }
    
    public static Vector4f lerp(Vector4fc start, Vector4fc goal, float progress) {
        return new Vector4f(lerp(start.x(), goal.x(), progress),
                lerp(start.y(), goal.y(), progress),
                lerp(start.z(), goal.z(), progress),
                lerp(start.w(), goal.w(), progress));
    }
    
    public static float dampSpringCrit(float target, float current, float[] velocity, float springConstant, float timestep) {
        if (velocity.length != 1) return Float.NaN;
        
        float currentToTarget = target - current;
        float springForce = currentToTarget * springConstant;
        float dampingForce = -velocity[0] * 2 * (float) Math.sqrt(springConstant);
        float force = springForce + dampingForce;
        velocity[0] += force * timestep;
        float displacement = velocity[0] * timestep;
        return current + displacement;
    }
    
    public static float dampSpringCrit(float target, float current, FloatBuffer velocity, float springConstant, float timestep) {
        float vel = velocity.get(0);
        float currentToTarget = target - current;
        float springForce = currentToTarget * springConstant;
        float dampingForce = -vel * 2 * (float) Math.sqrt(springConstant);
        float force = springForce + dampingForce;
        velocity.put(0, vel + force * timestep);
        float displacement = velocity.get(0) * timestep;
        return current + displacement;
    }
    
    public static Vector2f dampSpringCrit(Vector2fc target, Vector2fc current, Vector2f velocity, float springConstant, float timestep) {
        float valueX;
        float valueY;
        try (MemoryStack stack = stackPush()) {
            FloatBuffer xVelocity = stack.mallocFloat(1);
            FloatBuffer yVelocity = stack.mallocFloat(1);
            xVelocity.put(velocity.x);
            yVelocity.put(velocity.y);
            
            valueX = dampSpringCrit(target.x(), current.x(), xVelocity, springConstant, timestep);
            valueY = dampSpringCrit(target.y(), current.y(), yVelocity, springConstant, timestep);
            
            velocity.set(xVelocity.get(0), yVelocity.get(0));
        }
        return new Vector2f(valueX, valueY);
    }
    
    public static Vector3f dampSpringCrit(Vector3fc target, Vector3fc current, Vector3f velocity, float springConstant, float timestep) {
        float valueX;
        float valueY;
        float valueZ;
        try (MemoryStack stack = stackPush()) {
            FloatBuffer xVelocity = stack.mallocFloat(1);
            FloatBuffer yVelocity = stack.mallocFloat(1);
            FloatBuffer zVelocity = stack.mallocFloat(1);
            xVelocity.put(velocity.x);
            yVelocity.put(velocity.y);
            zVelocity.put(velocity.z);
            
            valueX = dampSpringCrit(target.x(), current.x(), xVelocity, springConstant, timestep);
            valueY = dampSpringCrit(target.y(), current.y(), yVelocity, springConstant, timestep);
            valueZ = dampSpringCrit(target.z(), current.z(), zVelocity, springConstant, timestep);
            
            velocity.set(xVelocity.get(0), yVelocity.get(0), zVelocity.get(0));
        }
        return new Vector3f(valueX, valueY, valueZ);
    }
    
}
