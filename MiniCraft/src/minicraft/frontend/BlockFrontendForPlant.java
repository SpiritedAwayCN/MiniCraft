package minicraft.frontend;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class BlockFrontendForPlant extends Mesh {
	private float width;
    private float height;

    //框架成型，数据待修改
    public void updateGeometry(float width, float height) {
        this.width = width;
        this.height = height;
        setBuffer(Type.Position, 3, new float[]{0,      0,      0,
                                                width,  0,      0,
                                                width,  height, 0,
                                                0,      height, 0
                                                });

        setBuffer(Type.TexCoord, 2, new float[]{0, 0,
                                                1, 0,
                                                1, 1,
                                                0, 1});
        
        setBuffer(Type.Normal, 3, new float[]{0, 0, 1,
                                              0, 0, 1,
                                              0, 0, 1,
                                              0, 0, 1});
        
        setBuffer(Type.Index, 3, new short[]{0, 1, 2,
                                             0, 2, 3});
        
        
        updateBound();
        setStatic();
    }
}
