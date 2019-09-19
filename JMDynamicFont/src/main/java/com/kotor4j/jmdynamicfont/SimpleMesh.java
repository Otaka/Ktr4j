package com.kotor4j.jmdynamicfont;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

/**
 * @author Dmitry
 */
public class SimpleMesh extends Mesh {

    public void setVertexData(int vertexCount, float[] positionBuffer, float[] normalBuffer, int[] indiciesBuffer, float[] textureBuffer) {
        if (positionBuffer.length / 3 != vertexCount) {
            throw new IllegalArgumentException("Position buffer should be [" + vertexCount + "*3 = " + (vertexCount * 3) + "] but received [" + positionBuffer.length + "]");
        }
        if (normalBuffer != null) {
            if (normalBuffer.length / 3 != vertexCount) {
                throw new IllegalArgumentException("Normal buffer should be [" + vertexCount + "*3 = " + (vertexCount * 3) + "] but received [" + normalBuffer.length + "]");
            }
        }
        
        if (textureBuffer != null) {
            if (textureBuffer.length / 2 != vertexCount) {
                throw new IllegalArgumentException("Texture buffer should be [" + vertexCount + "*2 = " + (vertexCount * 2) + "] but received [" + textureBuffer.length + "]");
            }
        }

        setBuffer(VertexBuffer.Type.Position, 3, positionBuffer);
        if (normalBuffer != null) {
            setBuffer(VertexBuffer.Type.Normal, 3, normalBuffer);
        }
        if (textureBuffer != null) {
            setBuffer(VertexBuffer.Type.TexCoord, 2, textureBuffer);
        }

        setBuffer(VertexBuffer.Type.Index, 3, indiciesBuffer);
        setStatic();
        updateBound();
        updateCounts();
    }
}
