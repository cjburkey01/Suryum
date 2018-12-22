package com.cjburkey.heck;

import lombok.Setter;

/**
 * Created by CJ Burkey on 2018/12/13
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Time {
    
    private static long lastUpdate = System.nanoTime();
    private static long lastRender = System.nanoTime();
    private static float deltaTime = 0.0f;
    private static float deltaRenderTime = 0.0f;
    
    @Setter
    private static float timeScale = 1.0f;
    
    public static float deltaTime() {
        return deltaTime * timeScale();
    }
    
    public static float deltaTime(float input) {
        return deltaTime() * input;
    }
    
    public static float deltaRenderTime() {
        return deltaRenderTime * timeScale();
    }
    
    public static float deltaRenderTime(float input) {
        return deltaRenderTime() * input;
    }
    
    public static float deltaTimeUnscaled() {
        return deltaTime;
    }
    
    public static float deltaTimeUnscaled(float input) {
        return deltaTimeUnscaled() * input;
    }
    
    public static float deltaRenderTimeUnscaled() {
        return deltaRenderTime;
    }
    
    public static float deltaRenderTimeUnscaled(float input) {
        return deltaRenderTimeUnscaled() * input;
    }
    
    public static float ups() {
        return 1.0f / deltaTimeUnscaled();
    }
    
    public static float fps() {
        return 1.0f / deltaRenderTimeUnscaled();
    }
    
    public static float timeScale() {
        return timeScale;
    }
    
    static void update() {
        long now = System.nanoTime();
        deltaTime = (now - lastUpdate) / 1_000_000_000.0f;
        lastUpdate = now;
    }
    
    static void render() {
        long now = System.nanoTime();
        deltaRenderTime = (now - lastRender) / 1_000_000_000.0f;
        lastRender = now;
    }
    
}
