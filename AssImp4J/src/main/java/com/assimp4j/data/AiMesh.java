package com.assimp4j.data;

/**
 * @author Dmitry
 */
public class AiMesh {

    private int materialIndex;
    private String meshName;
    private float[] positions;
    private int[] faces;
    private float[] normals;
    private float[] uv;
    private float[] vertexColors;
    private AiBone[] bones;

    public void createBonesArray(int size) {
        bones = new AiBone[size];
    }

    public void setBoneByIndex(int index, AiBone bone) {
        bones[index] = bone;
    }

    public AiBone[] getBones() {
        return bones;
    }

    public void setVertexColors(float[] vertexColors) {
        this.vertexColors = vertexColors;
    }

    public void setMeshName(String meshName) {
        this.meshName = meshName;
    }

    public String getMeshName() {
        return meshName;
    }

    public void setMaterialIndex(int materialIndex) {
        this.materialIndex = materialIndex;
    }

    public int getMaterialIndex() {
        return materialIndex;
    }

    public void setUv(float[] uv) {
        this.uv = uv;
    }

    public void setFaces(int[] faces) {
        this.faces = faces;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return getMeshName();
    }
}
