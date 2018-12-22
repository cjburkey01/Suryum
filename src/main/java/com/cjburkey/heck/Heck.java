package com.cjburkey.heck;

import com.cjburkey.heck.ecs.GameObject;
import com.cjburkey.heck.ecs.ISceneHandler;
import com.cjburkey.heck.ecs.Scene;
import com.cjburkey.heck.ecs.components.Camera;
import com.cjburkey.heck.game.KeyboardMove;
import com.cjburkey.heck.game.MouseMove;
import com.cjburkey.heck.game.MouseZoom;
import com.cjburkey.heck.glfw.Window;
import com.cjburkey.heck.material.BasicMaterial;
import com.cjburkey.heck.material.BasicShader;
import com.cjburkey.heck.mesh.BasicMesh;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by CJ Burkey on 2018/11/25
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Heck implements ISceneHandler {
    
    public static final Heck instance = new Heck();
    
    private static boolean mainRun = false;
    private static boolean running = false;
    
    @Getter
    @Setter
    private Scene currentScene = new Scene();
    
    private static Window window;
    private static BasicShader shader;
    private static final GameLoopTimer gameLoopTimer = new GameLoopTimer();
    
    public static void main(String[] args) {
        if (!mainRun) Thread.setDefaultUncaughtExceptionHandler((t, e) -> Log.exception(e));
        else throw new IllegalStateException("Cannot run main method after game has started");
        mainRun = true;
        instance.run();
    }
    
    private void run() {
        createWindow();
        createShader();
        startGameLoop();
        cleanup();
    }
    
    private void createWindow() {
        window = new Window("Heck 0.0.1", 300, 300);
        window.setHalfMonitorSize();
        window.setCenter();
        window.setVsync(true);
        window.show();
    }
    
    private void createShader() {
        shader = new BasicShader("basic");
    }
    
    private void startGameLoop() {
        // Main camera
        GameObject cameraObj = createCamera();
        cameraObj.transform.rotation.rotateX((float) Math.PI / -2.0f);
        cameraObj.transform.position.y += 5.0f;
//        cameraObj.transform.position.z += 12.0f;
        
        initTestObj();
        
        running = true;
        while (running) {
            // Update delta time
            gameLoopTimer.update();
            
            // Get input data and update game
            window.prepareUpdate();
            Time.update();
            update();
            
            // Clear the screen and draw to it
            window.prepareFrame();
            Time.render();
            render();
            
            // Display the new screen
            window.finishFrame();
            
            // Exit game if the player clicks the close button
            if (window.getShouldClose()) stop();
        }
        
        currentScene.cleanup();
    }
    
    private void cleanup() {
        if (shader != null) shader.destroy();
        if (window != null) window.destroy(true);
    }
    
    private GameObject createCamera() {
        GameObject cameraObj = currentScene.instantiate();
        cameraObj.addComponent(new Camera());
        cameraObj.addComponent(new MouseMove());
        cameraObj.addComponent(new KeyboardMove());
        cameraObj.addComponent(new MouseZoom());
        return cameraObj;
    }
    
    private void initTestObj() {
        // Test mesh
        final BasicMesh mesh = new BasicMesh(false);
        mesh.setMaterial(new BasicMaterial(shader));
        mesh.getMaterial().setAlbedo(1.0f, 1.0f, 1.0f);
        mesh.getMaterial().setTransforms(true);
        mesh.set(new float[] {
                0.5f, 0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        }, new short[] {
                0, 1, 2,
                0, 2, 3,
        });
        GameObject test1 = currentScene.instantiate();
//        GameObject test2 = currentScene.instantiate();
        test1.transform.rotation.rotateX((float) Math.PI / -2.0f);
//        test2.transform.position.x += 10.0f;
        mesh.createRenderer(test1);
//        mesh.createRenderer(test2);
    }
    
    private void update() {
        currentScene.update();
    }
    
    private void render() {
        currentScene.render();
    }
    
    public void stop() {
        running = false;
    }
    
    public boolean getRunning() {
        return running;
    }
    
    public Window getWindow() {
        return window;
    }
    
    public BasicShader getShader() {
        return shader;
    }
    
    public float getUpdateDelta() {
        return (float) gameLoopTimer.getUpdateDelta();
    }
    
}
