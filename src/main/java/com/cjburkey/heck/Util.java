package com.cjburkey.heck;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by CJ Burkey on 2018/11/25
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Util {
    
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
    
}
