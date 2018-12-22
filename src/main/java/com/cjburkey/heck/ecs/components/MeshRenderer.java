package com.cjburkey.heck.ecs.components;

import com.cjburkey.heck.Heck;
import com.cjburkey.heck.ecs.Component;
import com.cjburkey.heck.mesh.BasicMesh;
import lombok.Getter;
import lombok.Setter;

import static com.cjburkey.heck.Transformation.*;

/**
 * Created by CJ Burkey on 2018/12/11
 */
public class MeshRenderer extends Component {
    
    @Getter
    @Setter
    private BasicMesh mesh;
    
    protected void onRender() {
        if (mesh == null) return;
        if (mesh.getMaterial().getTransforms()) {
            mesh.getMaterial().setTransformation(getCompleteMatrix(
                    Heck.instance.getCurrentScene().getMainCamera().getProjectionViewMatrix(),
                    getTransform().getModelMatrix()));
        }
        mesh.render();
    }
    
    protected void onRemoved() {
        if (mesh != null) mesh.destroy();
    }
    
}
