package com.kotor4j.utils;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.ui.Picture;
import com.jme3.util.BufferUtils;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

/**
 * @author Dmitry
 */
public class PictureTextureUtils {

    public static Texture2D textureFromImage(AWTLoader loader, BufferedImage image, String name) {
        Texture2D texture = new Texture2D();
        Image jmeImage = loader.load(image, true);

        texture.setImage(jmeImage);
        texture.setName(name);
        return texture;
    }
    
    public static Texture2D textureFromImage(BufferedImage image, String name) {
        return textureFromImage(new AWTLoader(), image, name);
    }

    public static boolean isEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.isEmpty();
    }

    public static void mirrorPictureHorizontally(Picture pic) {
        Mesh mesh = pic.getMesh();
        VertexBuffer vertexBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        FloatBuffer floatBuffer = (FloatBuffer) vertexBuffer.getData();
        float[] f = BufferUtils.getFloatArray(floatBuffer);
        float t1, t2;
        t1 = f[0];
        t2 = f[1];
        f[0] = f[2];
        f[1] = f[3];
        f[2] = t1;
        f[3] = t2;

        t1 = f[6];
        t2 = f[7];
        f[6] = f[4];
        f[7] = f[5];
        f[4] = t1;
        f[5] = t2;

        vertexBuffer.updateData(BufferUtils.createFloatBuffer(f));
    }

    public static void mirrorPictureVertically(Picture pic) {
        Mesh mesh = pic.getMesh();
        VertexBuffer vertexBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        FloatBuffer floatBuffer = (FloatBuffer) vertexBuffer.getData();
        float[] f = BufferUtils.getFloatArray(floatBuffer);
        float t1, t2;
        t1 = f[0];
        t2 = f[1];
        f[0] = f[6];
        f[1] = f[7];
        f[6] = t1;
        f[7] = t2;

        t1 = f[2];
        t2 = f[3];
        f[2] = f[4];
        f[3] = f[5];
        f[4] = t1;
        f[5] = t2;

        vertexBuffer.updateData(BufferUtils.createFloatBuffer(f));
    }

    public static void rotatePicture270(Picture pic) {
        Mesh mesh = pic.getMesh();
        VertexBuffer vertexBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        FloatBuffer floatBuffer = (FloatBuffer) vertexBuffer.getData();
        float[] f = BufferUtils.getFloatArray(floatBuffer);
        float t1, t2;
        t1 = f[0];
        t2 = f[1];
        f[0] = f[6];
        f[1] = f[7];
        f[6] = f[4];
        f[7] = f[5];

        f[4] = f[2];
        f[5] = f[3];

        f[2] = t1;
        f[3] = t2;

        vertexBuffer.updateData(BufferUtils.createFloatBuffer(f));
    }
    
    public static void rotatePicture90(Picture pic) {
        Mesh mesh = pic.getMesh();
        VertexBuffer vertexBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        FloatBuffer floatBuffer = (FloatBuffer) vertexBuffer.getData();
        float[] f = BufferUtils.getFloatArray(floatBuffer);
        float t1, t2;
        t1 = f[0];
        t2 = f[1];
        
        f[0] = f[2];
        f[1] = f[3];
        
        f[2] = f[4];
        f[3] = f[5];

        f[4] = f[6];
        f[5] = f[7];

        f[6] = t1;
        f[7] = t2;

        vertexBuffer.updateData(BufferUtils.createFloatBuffer(f));
    }
}
