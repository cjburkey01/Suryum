package com.cjburkey.heck.material;

import com.cjburkey.heck.Util;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by CJ Burkey on 2018/12/05
 */
public class BasicShader extends Shader {
    
    public BasicShader(String name) {
        addShader(GL_VERTEX_SHADER, Util.readResource("shader/" + name + ".vs").orElse(null));
        addShader(GL_FRAGMENT_SHADER, Util.readResource("shader/" + name + ".fs").orElse(null));
        finish();
    }
    
}
