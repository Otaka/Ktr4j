package com;

import com.assimp4j.AssImp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Dmitry
 */
public class MainAssimp4j {

    public static void main(String[] args) throws IOException {
        System.out.println("Started AssImp4j");
        byte[] bytes = Files.readAllBytes(Paths.get("f:\\temp\\tempModels\\testModelWithAnimation.fbx"));
        AssImp assImp = AssImp.get();
        File modelFile = new File("f:\\temp\\tempModels\\testModelWithAnimation.fbx");
        assImp.loadMesh(new BufferedInputStream( new FileInputStream(modelFile)), (int) modelFile.length(), "fbx", AssImp.PROP_GenSmoothNormals | AssImp.PROP_JoinIdenticalVertices | AssImp.PROP_OptimizeMeshes | AssImp.PROP_Triangulate);
        assImp.loadMesh(new File("f:\\temp\\tempModels\\testModelWithAnimation.fbx"), AssImp.PROP_GenSmoothNormals | AssImp.PROP_JoinIdenticalVertices | AssImp.PROP_OptimizeMeshes | AssImp.PROP_Triangulate);
    }
}
