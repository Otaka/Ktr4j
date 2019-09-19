package com.assimp4j.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class AiScene {

    private List<AiMaterial> materials = new ArrayList<>();
    private List<AiMesh> meshes = new ArrayList<>();
    private List<AiAnimation> animations = new ArrayList<>();
    private AiNode rootNode;

    public AiNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(AiNode rootNode) {
        this.rootNode = rootNode;
    }

    public List<AiMaterial> getMaterials() {
        return materials;
    }

    public List<AiMesh> getMeshes() {
        return meshes;
    }

    public List<AiAnimation> getAnimations() {
        return animations;
    }

    public void addAnimation(AiAnimation animation) {
        animations.add(animation);
    }

    public void addMaterial(AiMaterial material) {
        materials.add(material);
    }

    public void addMesh(AiMesh mesh) {
        meshes.add(mesh);
    }
}
