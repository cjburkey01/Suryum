package com.cjburkey.heck;

/**
 * Created by CJ Burkey on 2018/11/25
 */
@SuppressWarnings("WeakerAccess")
public class GameLoopTimer {
    
    private long lastUpdate;
    private double updateDelta;
    
    public void update() {
        long now = System.nanoTime();
        updateDelta = (now - lastUpdate) / 1000000000.0d;
        lastUpdate = now;
    }
    
    public double getUpdateDelta() {
        return updateDelta;
    }
    
}
